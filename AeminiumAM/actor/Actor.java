package actor;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.lang.System;
import java.util.Hashtable;
import java.util.Vector;

import aeminium.runtime.Runtime;
import aeminium.runtime.*;
import annotations.writable;

import actor.AeminiumRuntime;


public abstract class Actor{
	
	private Hashtable<String,Vector<DependencyTask>> varDep = null;
	
	public Actor() {
		varDep = new Hashtable<String,Vector<DependencyTask>>();
		
		for(Field f :this.getClass().getDeclaredFields()){
			varDep.put(f.getName(), new Vector<DependencyTask>());
		}
	}

	protected abstract void react(Object obj);

	public void sendMessage(final Object obj) {

		
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
		for (Field f: this.getClass().getDeclaredFields()) {
			for (Annotation an : f.getAnnotations()) {
				if(an instanceof writable && ((writable) an).isWritable() == true){
					return false;
				}
			}
		}
		return true;
	}
	
	protected Hashtable<String,Vector<DependencyTask>> getvarDep(){
		return varDep;
	}
}
