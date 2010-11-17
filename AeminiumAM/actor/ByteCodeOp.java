package actor;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;


import com.sun.org.apache.bcel.internal.classfile.ClassFormatException;
import com.sun.org.apache.bcel.internal.classfile.ClassParser;
import com.sun.org.apache.bcel.internal.classfile.Method;
import com.sun.org.apache.bcel.internal.generic.ClassGen;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.GETFIELD;
import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
import com.sun.org.apache.bcel.internal.generic.InstructionHandle;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.LDC;
import com.sun.org.apache.bcel.internal.generic.MethodGen;
import com.sun.org.apache.bcel.internal.generic.Type;

public class ByteCodeOp {
	
	static public ArrayList<String> getFields(String methodName, Actor actor){
        ClassGen cGen;
		try {
			
			ClassLoader loader = actor.getClass().getClassLoader();
			InputStream inputs = loader.getResourceAsStream(new StringBuilder().append(actor.getClass().getName().replace('.', '/')).append(".class").toString());
			
			cGen = new ClassGen(new ClassParser(inputs,null).parse());
		} catch (ClassFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		ConstantPoolGen cPoolGen = cGen.getConstantPool();
		
		ArrayList<String> usedFieldsName = new ArrayList<String>();
		
		for(Field f : actor.getClass().getFields()){
            //System.out.println("Searching int field -> " + f.getName() + " -> extended: "+f);
            
            for(Method m : cGen.getMethods()){
            	
            	if(m.getName().equals(methodName)){
            		//System.out.println(m.getName()+" || "+methodName);
            		
                    MethodGen methGen = new MethodGen(m, cGen.getClassName(), cPoolGen);
                    InstructionList iList = methGen.getInstructionList();
                    
                    InstructionHandle[] iHandles = iList.getInstructionHandles();
                                    
                    for(int i = 0; i < iHandles.length; i++){
                        if((iHandles[i].getInstruction() instanceof GETFIELD)){
                        	//System.out.println(((GETFIELD)iHandles[i].getInstruction()).getFieldName(cPoolGen) +"||  name: "+f.getName());
                            	 
							if(((GETFIELD)iHandles[i].getInstruction()).getFieldName(cPoolGen).equals(f.getName()) && !usedFieldsName.contains(f.getName())){
								//System.out.println(iHandles[i]+" :xxxxx: "+f.getName());
								//System.out.println(iHandles[i + 1]);
								//if(iHandles[i + 1].getInstruction() instanceof INVOKEVIRTUAL){
									//System.out.println("Found INVOKEVIRTUAL");
									usedFieldsName.add(f.getName());
								//}		
							}
                        }
                    }
                    //System.out.println(usedFieldsName.size());
            	}
            }
		}
		return usedFieldsName;
	}
}