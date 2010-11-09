package actor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

import main.Test.TestActor;

import aeminium.runtime.Body;
import aeminium.runtime.DataGroup;
import aeminium.runtime.Runtime;
import aeminium.runtime.Task;

public class Dispatcher {
	static Object object;
	
	public static void dispatcToAM(final Actor actor, final String name, final Object msg){
		object = msg;
		
		if(canBeParallelized(actor)){
			System.out.println("going to be par from dispatcher");
			
			Task t1 = AeminiumRuntime.rt.createNonBlockingTask(new Body(){	
				@Override
				public void execute(Runtime rt, Task current)
						throws Exception {
					try{						
						Class<?> c = actor.getClass();
						Object t = c.newInstance();
						
						Method[] allMethods = c.getDeclaredMethods();
					    for (Method m : allMethods) {
					    	if(m.getName() == name){
					    		try {
					    		    m.setAccessible(true);
					    		    Object ob = new Object();
					    		    ob = object;
					    		    
					    		    m.invoke(t, ob);

					    		// Handle any exceptions thrown by method to be invoked.
					    		} catch (InvocationTargetException x) {
					    		    x.getCause();
					    		}
					    	}
					    }	
					} catch(Exception e){
						e.printStackTrace();
					}
				}}, Runtime.NO_HINTS);

			AeminiumRuntime.rt.schedule(t1, Runtime.NO_PARENT, Runtime.NO_DEPS);
			
		} else{
			System.out.println("is going to be an Atomic task from dispatcher");
			
			/* Useless Datagroup created to pass as arg in createAtomicTask */
			DataGroup dg = AeminiumRuntime.rt.createDataGroup();
			
			Task t1 = AeminiumRuntime.rt.createAtomicTask(new Body(){		
				@Override
				public void execute(Runtime rt, Task current)
						throws Exception {
					actor.getClass().getMethod(name, (Class<?>[]) object);
					
				}},dg, Runtime.NO_HINTS);
			
			AeminiumRuntime.rt.schedule(t1, Runtime.NO_PARENT, Runtime.NO_DEPS);
		}
	}
	

	
	private static boolean canBeParallelized(Actor actor){
		
		for (Field f: actor.getClass().getFields()) {
			if(f.getAnnotations().length == 0){
				return false;
			}
			else{
				for (Annotation an : f.getAnnotations()) {
					if(an instanceof readOnly && ((readOnly) an).isReadOnly() == false){
						return false;
					}
				}
			}
		}
		return true;
	}
}
