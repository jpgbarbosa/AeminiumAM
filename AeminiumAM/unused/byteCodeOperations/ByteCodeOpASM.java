package unused.byteCodeOperations;

import java.util.*;
import java.io.IOException;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.MethodInsnNode;

<<<<<<< HEAD:AeminiumAM/byteCodeOperations/ByteCodeOpASM.java
import constants.*;
=======
import unused.constants.Constants;

>>>>>>> 7f239224b33f6559dfb5c69ea485b478f97ebd43:AeminiumAM/unused/byteCodeOperations/ByteCodeOpASM.java



import actor.Actor;

public class ByteCodeOpASM implements Opcodes {
	
	static int [] opcodes = {AASTORE, ASTORE,BASTORE, CASTORE, DASTORE, DSTORE, FASTORE, FSTORE, IASTORE, ISTORE, LASTORE, LSTORE, SASTORE};
    
	//Get used fields specifications (writable or not) inside methodName 
    public static HashMap<String,Boolean> getWritableFields(String methodName, Actor actor){
	    ClassReader cr = null;
		try {
			/* construct a ClassReader instance by specifying the inputstream */
			ClassLoader cl = actor.getClass().getClassLoader();
			cr = new ClassReader(cl.getResourceAsStream(actor.getClass().getName().replace('.', '/') + ".class"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	    ClassNode cn = new ClassNode();
	    cr.accept(cn,ClassReader.SKIP_DEBUG);
	    
	    
		@SuppressWarnings("rawtypes")
		List methods = cn.methods;
	    
	    HashMap<String,Boolean> usedVarHash = null;
	    
	    //Search methodName
        for (int i = 0; i < methods.size(); ++i) {

        	MethodNode method = (MethodNode) methods.get(i);
        	
        	if(method.name.equals(methodName)){
        		int opcode, insnType;
        		String varName;
        		usedVarHash = new HashMap<String,Boolean>();
        		String loadedVar=null;
        		
        		//Iterate over all the instructions
        		for (int x = 0; x < method.instructions.size(); x++) {
        			
					AbstractInsnNode  insn = method.instructions.get(x);
					opcode = insn.getOpcode();
					insnType = insn.getType();
					
					if(constants.Constants.debug_asm){
						System.out.println("opcode: "+opcode+ "; type: " + insnType);
					}
					
					/* If we found a variable after tracking another one,
					 * that's because it wasn't writable
					 */
					if(loadedVar!=null && isStoreInsn(opcode)){
						if(constants.Constants.debug_asm_tracking){
							System.out.println("was catch as W: "+loadedVar);
						}
						usedVarHash.put(loadedVar, true);
						loadedVar = null;
					}
					
    	            if (insnType == AbstractInsnNode.FIELD_INSN) {
    	            	varName = ((FieldInsnNode) insn).name;
    	            	if(constants.Constants.debug_asm){
    						System.out.println("opcode: "+opcode+ "; name: " + varName);
    					}
    	            	//Track variable to check if after loaded they are written
    	            	if(opcode == GETSTATIC || opcode == GETFIELD){
    	            		if(varName!=null && !usedVarHash.containsKey(varName)){
    	            			usedVarHash.put(varName, false);
    	            		}
    	            		loadedVar = varName;
    	            		
    	            		if(constants.Constants.debug_asm_tracking){
    	            			System.out.println("GET*:"+varName);
    	            		}
    	            	}
    	            	
    	            	else if (opcode == ASTORE || opcode == DSTORE || opcode == LSTORE
    	            			|| opcode == ISTORE || opcode == FSTORE || opcode == PUTFIELD || opcode == PUTSTATIC) {
    	            		usedVarHash.put(varName, true);
    	            	} else if(!usedVarHash.containsKey(varName)){
    	            		usedVarHash.put(varName, false);
    	            	}
    	            }
        		}
            	break;
    		}
        }
        return usedVarHash;
    }
    
    //get methods name if they are used inside thisMethodName
    public static ArrayList<String> getUsedMethods(String thisMethodName, Actor actor){
    	
	    ClassReader cr = null;
		try {
			/* construct a ClassReader instance by specifying the inputstream */
			ClassLoader cl = actor.getClass().getClassLoader();
			cr = new ClassReader(cl.getResourceAsStream(actor.getClass().getName().replace('.', '/') + ".class"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	    ClassNode cn = new ClassNode();
	    cr.accept(cn,ClassReader.SKIP_DEBUG);
	    
	    @SuppressWarnings("rawtypes")
		List methods = cn.methods;
	    ArrayList<String> usedMethods = new ArrayList<String>();
	    
        for (int i = 0; i < methods.size(); ++i) {

        	MethodNode method = (MethodNode) methods.get(i);
        	
        	if(method.name.equals(thisMethodName)){
        		int insnType;
        		
        		for (int x = 0; x < method.instructions.size(); x++) {
        			AbstractInsnNode  insn = method.instructions.get(x);
					insnType = insn.getType();
    	            if (insnType == AbstractInsnNode.METHOD_INSN) {
    	            	usedMethods.add(((MethodInsnNode) insn).name);
    	            }
        		}
        	}
        }
    	return usedMethods;
    }
    
    //TODO: check if worth to improve this search
    static boolean isStoreInsn(int opcode){
    	for(int i =0 ; i< opcodes.length; i++){
    		if(opcodes[i]==opcode)
    			return true;
    	}
    	return false;
    }
}
