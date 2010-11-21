package byteCodeOperations;

import java.util.*;
import java.io.IOException;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class ByteCodeOpASM implements Opcodes {
    
    public static HashMap<String,Boolean> getWritableFields(String methodName){
	    ClassReader cr = null;
		try {
			cr = new ClassReader("main/TestActor");
		} catch (IOException e) {
			e.printStackTrace();
		}
	    ClassNode cn = new ClassNode();
	    cr.accept(cn,ClassReader.SKIP_DEBUG);
	    
	    @SuppressWarnings("rawtypes")
		List methods = cn.methods;
	    
	    HashMap<String,Boolean> usedVarHash = null;
	    
        for (int i = 0; i < methods.size(); ++i) {

        	MethodNode method = (MethodNode) methods.get(i);
        	
        	if(method.name.equals(methodName)){
        		
        		usedVarHash = new HashMap<String,Boolean>();
        		
        		for (int x = 0; x < method.instructions.size(); x++) {
        			
        			AbstractInsnNode  insn = method.instructions.get(x);
        			
    	           int insnType = insn.getType();
    	           if (insnType == AbstractInsnNode.FIELD_INSN) {
    	               int opcode = insn.getOpcode();
    	               String varName = ((FieldInsnNode) insn).name;
    	               if (opcode == ASTORE || opcode == DSTORE || opcode == LSTORE
    	            		   || opcode == ISTORE || opcode == FSTORE || opcode == PUTFIELD) {
    	            	   usedVarHash.put(varName, true);     	                   
    	               } else {
    	            	   usedVarHash.put(varName, false);
    	               }
    	           }
        		}
            	break;
    		}
        }
        return usedVarHash;
    }
}
