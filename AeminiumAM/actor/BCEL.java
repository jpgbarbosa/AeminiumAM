package actor;

import java.io.IOException;
import java.util.ArrayList;


import com.sun.org.apache.bcel.internal.classfile.ClassFormatException;
import com.sun.org.apache.bcel.internal.classfile.ClassParser;
import com.sun.org.apache.bcel.internal.classfile.Field;
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

public class BCEL {
	
	static public ArrayList<String> checkFields(String className, String methodName){

	
        ClassGen cGen;
		try {
			cGen = new ClassGen(new ClassParser(className+".class").parse());
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
		
		for(Field f : cGen.getFields())
		{
		       // if(f.getType().equals(Type.INT))
		        //{
		                System.out.println("Searching int field -> " + f);
		                for(Method m : cGen.getMethods())
		                {
		                	if(m.getName().equals(methodName)){
		                        MethodGen methGen = new MethodGen(m, cGen.getClassName(), cPoolGen);
		                        InstructionList iList = methGen.getInstructionList();
		                        
		                        InstructionHandle[] iHandles = iList.getInstructionHandles();
		                                        
		                        for(int i = 0; i < iHandles.length; i++) 
		                        {
		                                if((iHandles[i].getInstruction() instanceof GETFIELD) && 
		                                        ((GETFIELD)iHandles[i].getInstruction()).getFieldName(cPoolGen).equals(f.getName())) 
		                                {
		                                        System.out.println(iHandles[i]);
		                                        System.out.println(iHandles[i + 1]);
		                                        if(iHandles[i + 1].getInstruction() instanceof INVOKEVIRTUAL)
		                                        {
		                                                System.out.println("Found INVOKEVIRTUAL");
		                                                usedFieldsName.add(f.getName());
		                                             //   chickenStickField = f;
		                                                break;
		                                        }
		                                }
		                        }
		                      //  if(chickenStickField != null)
		                        //        break;
		                	}
		                }
		        //}
		        //if(chickenStickField != null)
		         //       break;
		}
		return usedFieldsName;       
		//System.out.println("Field found -> " + chickenStickField);

	}
}