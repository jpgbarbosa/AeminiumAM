package actor;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;


import aeminium.runtime.Runtime;
import aeminium.runtime.*;
import annotations.writable;

import actor.AeminiumRuntime;


public abstract class Actor{
	
	private ArrayList<String> methods = null;
	private Hashtable<String,Vector<DependencyTask>> varDep = null;
	private Hashtable<String,HashMap<String,Boolean>> methodsWrites = null;
	
	//private DataGroup myDataGroup;
	
	public Actor() {
		methods = new ArrayList<String>();
		
		methodsWrites = new Hashtable<String, HashMap<String,Boolean>>(4);
		
		//myDataGroup = AeminiumRuntime.rt.createDataGroup();
		
		Method [] m = this.getClass().getDeclaredMethods();
		
		for(int i=0; i<m.length ; i++){
			if(!m[i].getName().equals("react") &&  !m[i].getName().equals("sendMessage")
					&&  !m[i].getName().equals("canBeParallelized")
					&&  !m[i].getName().equals("getvarDep")
					&&  !m[i].getName().equals("getMethodsName")){
				methods.add(m[i].getName());
			}
		}
		
		int counter =0;
		for(@SuppressWarnings("unused") Field f :this.getClass().getDeclaredFields()){
			counter++;
		}
		
		varDep = new Hashtable<String,Vector<DependencyTask>>(counter*2);
		
		for(Field f :this.getClass().getDeclaredFields()){
			varDep.put(f.getName(), new Vector<DependencyTask>());
		}
	}

	protected abstract void react(Object obj);

	public void sendMessage(final Object obj) {

		
		if(canBeParallelized()){
			
			//System.out.println("Actor main is going to be par");
			
			Task t1 = AeminiumRuntime.rt.createNonBlockingTask(new Body(){	
				@Override
				public void execute(Runtime rt, Task current)
						throws Exception {
					
					react(obj);
					
				}}, Runtime.NO_HINTS);

			AeminiumRuntime.rt.schedule(t1, Runtime.NO_PARENT, Runtime.NO_DEPS);
			
		} else{
			//System.out.println("Actor main is going to be an Atomic task");
			
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
	
	protected ArrayList<String> getMethodsName(){
		return methods;
	}
	
	protected Hashtable<String,HashMap<String,Boolean>> getMethodsWrites(){
		return methodsWrites;
	}
}
