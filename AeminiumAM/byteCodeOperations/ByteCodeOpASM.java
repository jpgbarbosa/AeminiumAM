package byteCodeOperations;

import java.util.*;
import java.io.IOException;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.MethodInsnNode;

import Constants.Constants;


import actor.Actor;

public class ByteCodeOpASM implements Opcodes {
	
	static int [] opcodes = {AASTORE, ASTORE,BASTORE, CASTORE, DASTORE, DSTORE, FASTORE, FSTORE, IASTORE, ISTORE, LASTORE, LSTORE, SASTORE};
    
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
	    
        for (int i = 0; i < methods.size(); ++i) {

        	MethodNode method = (MethodNode) methods.get(i);
        	
        	if(method.name.equals(methodName)){
        		int opcode, insnType;
        		String varName;
        		usedVarHash = new HashMap<String,Boolean>();
        		String loadedVar=null;
        		
        		for (int x = 0; x < method.instructions.size(); x++) {
        			
					AbstractInsnNode  insn = method.instructions.get(x);
					opcode = insn.getOpcode();
					insnType = insn.getType();
					
					if(Constants.debug_asm){
						System.out.println("opcode: "+opcode+ "; type: " + insnType);
					}
					
					if(loadedVar!=null && isStoreInsn(opcode)){
						if(Constants.debug_asm_tracking){
							System.out.println("was catch as W: "+loadedVar);
						}
						usedVarHash.put(loadedVar, true);
						loadedVar = null;
					}
					
    	            if (insnType == AbstractInsnNode.FIELD_INSN) {
    	            	varName = ((FieldInsnNode) insn).name;
    	            	if(Constants.debug_asm){
    						System.out.println("opcode: "+opcode+ "; name: " + varName);
    					}
    	            	if(opcode == GETSTATIC || opcode == GETFIELD){
    	            		loadedVar = varName;
    	            		if(Constants.debug_asm_tracking){
    	            			System.out.println("GET*:"+varName);
    	            		}
    	            	}
    	            	
    	            	if (opcode == ASTORE || opcode == DSTORE || opcode == LSTORE
    	            			|| opcode == ISTORE || opcode == FSTORE || opcode == PUTFIELD || opcode == PUTSTATIC) {
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
    
    static boolean isStoreInsn(int opcode){
    	
    	for(int i =0 ; i< opcodes.length; i++){
    		if(opcodes[i]==opcode)
    			return true;
    	}
    	return false;
    	/*int min = 0;
    	int max = opcodes.length-1;
    	int mid;
    	
    	do{
	      mid = min + (max-min)/2;
	      if( opcode > opcodes[mid])
	        min = mid + 1;
	      else 
	        max = mid - 1;
    	}while ((opcodes[mid] == opcode) || (min > max));
    	
    	if(min>max)
    		return false;
    	return true;*/
    }
}
