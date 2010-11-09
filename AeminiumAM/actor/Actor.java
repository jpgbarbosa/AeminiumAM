package actor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import aeminium.runtime.Runtime;
import aeminium.runtime.*;

import actor.AeminiumRuntime;


public abstract class Actor{
	@readOnly
	public Object object;
	
	public Actor() {}

	public abstract void react(Object obj);

	synchronized public void sendMessage(Object obj) {

		object = obj;
		
		if(canBeParallelized()){
			System.out.println("going to be par");
			
			Task t1 = AeminiumRuntime.rt.createNonBlockingTask(new Body(){	
				@Override
				public void execute(Runtime rt, Task current)
						throws Exception {
					
					react(object);
					
				}}, Runtime.NO_HINTS);

			AeminiumRuntime.rt.schedule(t1, Runtime.NO_PARENT, Runtime.NO_DEPS);
			
		} else{
			System.out.println("is going to be an Atomic task");
			
			/* Useless Datagroup created to pass as arg in createAtomicTask */
			DataGroup dg = AeminiumRuntime.rt.createDataGroup();
			
			Task t1 = AeminiumRuntime.rt.createAtomicTask(new Body(){		
				@Override
				public void execute(Runtime rt, Task current)
						throws Exception {
					
					react(object);
					
				}},dg, Runtime.NO_HINTS);
			
			AeminiumRuntime.rt.schedule(t1, Runtime.NO_PARENT, Runtime.NO_DEPS);
		}
	}
	
	private boolean canBeParallelized(){
		
		for (Field f: this.getClass().getFields()) {			
			if(f.getAnnotations().length == 0){
				return false;
			}
			else{
				for (Annotation an : f.getAnnotations()) {
					if(an instanceof readOnly && ((readOnly) an).isReadOnly() == false){
					}
				}
			}
		}
		return true;
	}

}
