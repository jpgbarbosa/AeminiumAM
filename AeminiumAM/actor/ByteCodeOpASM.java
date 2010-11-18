package actor;

import java.util.*;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.objectweb.asm.Opcodes.*;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.BasicVerifier;
import org.objectweb.asm.tree.analysis.SourceInterpreter;
import org.objectweb.asm.tree.analysis.SourceValue;
import org.objectweb.asm.tree.analysis.Frame;
import org.objectweb.asm.util.TraceMethodVisitor;

public class ByteCodeOpASM implements Opcodes {
    
    //read 'new' super class for necessary constructor bridge calls
    public static ArrayList<String> getWritableFields(String methodName){
	    ClassReader cr = null;
		try {
			cr = new ClassReader("actor/TestActor");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    ClassNode cn = new ClassNode();
	    cr.accept(cn,ClassReader.SKIP_DEBUG);
	    
	    List methods = cn.methods;
	    
	    ArrayList<String> usedVars = new ArrayList<String>();
	    
        for (int i = 0; i < methods.size(); ++i) {

        	MethodNode method = (MethodNode) methods.get(i);
        	//System.out.println(method.name);
        	
        	if(method.name.equals(methodName)){
        		/*
        		 Analyzer a = new Analyzer(new SourceInterpreter());
        	     try {
					Frame[] frames = a.analyze(cn.name, method);
				} catch (AnalyzerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("'"+method.instructions.size()+"'");
				*/
        		for (int x = 0; x < method.instructions.size(); x++) {
        			String fieldName=null;
        			//System.out.println("x: "+x);
                    Object insn = method.instructions.get(x);
                    
                    int opcode = ((AbstractInsnNode) insn).getOpcode();
                    
					try{
						fieldName  = ((FieldInsnNode) insn).name;
					}catch(Exception e){
						//e.printStackTrace();
					}
                    
					if(fieldName!=null && !usedVars.contains(fieldName))
						usedVars.add(fieldName);
                    //System.out.println(fieldName+" || "+opcode);
        		}
            	break;
    		}
        }
        return usedVars;
    }
}
