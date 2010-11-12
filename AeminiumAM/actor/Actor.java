package actor;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.lang.System;

import aeminium.runtime.Runtime;
import aeminium.runtime.*;

import actor.AeminiumRuntime;


public abstract class Actor{
	
	public Actor() {}

	public abstract void react(Object obj);

	synchronized public void sendMessage(final Object obj) {

		
		if(canBeParallelized()){
			System.out.println("Actor main is going to be par");
			
			Task t1 = AeminiumRuntime.rt.createNonBlockingTask(new Body(){	
				@Override
				public void execute(Runtime rt, Task current)
						throws Exception {
					
					react(obj);
					
				}}, Runtime.NO_HINTS);

			AeminiumRuntime.rt.schedule(t1, Runtime.NO_PARENT, Runtime.NO_DEPS);
			
		} else{
			System.out.println("Actor main is going to be an Atomic task");
			
			/* Useless Datagroup created to pass as arg in createAtomicTask */
			DataGroup dg = AeminiumRuntime.rt.createDataGroup();
			
			Task t1 = AeminiumRuntime.rt.createAtomicTask(new Body(){		
				@Override
				public void execute(Runtime rt, Task current)
						throws Exception {
					
					react(obj);
					
				}},dg, Runtime.NO_HINTS);
			
			AeminiumRuntime.rt.schedule(t1, Runtime.NO_PARENT, Runtime.NO_DEPS);
		}
	}
	
	private boolean canBeParallelized(){
		
		if(this.getClass().getFields().length == 0)
			return true;
				
		for (Field f: this.getClass().getDeclaredFields()) {
			for (Annotation an : f.getAnnotations()) {
				if(an instanceof writable && ((writable) an).isWritable() == true){
					return false;
				}
			}
		}
		return true;
	}
}
