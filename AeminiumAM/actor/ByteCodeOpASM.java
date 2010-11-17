package actor;

import java.util.*;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.objectweb.asm.Opcodes.*;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.ClassNode;



public class ByteCodeOpASM implements Opcodes {
    String super_class = "Actor";
    
    //read 'new' super class for necessary constructor bridge calls
    public void getWritableFields(){
	    ClassReader cr = null;
		try {
			cr = new ClassReader(super_class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    ClassNode cn = new ClassNode();
	    cr.accept(cn,ClassReader.SKIP_DEBUG);
	    
	    
    }
    
}
