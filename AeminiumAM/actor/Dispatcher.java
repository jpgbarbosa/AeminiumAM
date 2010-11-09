package actor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

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
						Object [] oa = new Object[1];
						oa[0] = object;
						
						for(int i=0; i<actor.getClass().getDeclaredMethods().length; i++){
							//.getDeclaredMethod(name, new Class[]{Object[].class}).invoke(actor, oa);
							//for(int x=0; x<actor.getClass().getDeclaredMethods()[i].getTypeParameters().length; x++){
								System.out.println("nome: "+actor.getClass().getDeclaredMethods()[i].getTypeParameters().length);
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
