package actor;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Vector;


import aeminium.runtime.Runtime;
import aeminium.runtime.*;
import annotationsVar.writable;

import actor.AeminiumRuntime;
import actor.annotations.Read;


public abstract class Actor{
	
	/* AspectJ vars*/
	protected Collection<Task> previousTasks;
	protected Collection<Task> latestWriters;
	protected boolean previousTasksAreWriters;
		
	private ArrayList<String> methods = null;
	private Hashtable<String,Vector<DependencyTask>> varDep = null;
	private Hashtable<String,HashMap<String,Boolean>> methodsWrites = null;
	private boolean hasWrites = false;
	
	private DataGroup myDataGroup;
	
	public Actor() {
		previousTasks           = new LinkedList<Task>();
		latestWriters           = previousTasks;
		previousTasksAreWriters = true;
		
		methods = new ArrayList<String>();
		
		methodsWrites = new Hashtable<String, HashMap<String,Boolean>>(4);
		
		myDataGroup = AeminiumRuntime.rt.createDataGroup();
		
		Method [] m = this.getClass().getDeclaredMethods();
		
		for(int i=0; i<m.length ; i++){
			if(!m[i].getName().equals("react") &&  !m[i].getName().equals("sendMessage")
					&&  !m[i].getName().equals("canBeParallelized")
					&&  !m[i].getName().equals("getvarDep")
					&&  !m[i].getName().equals("getMethodsName")){
				methods.add(m[i].getName());
			}
		}
		
		int counter = this.getClass().getDeclaredFields().length;
		
		varDep = new Hashtable<String,Vector<DependencyTask>>(counter*2);
		
		for(Field f :this.getClass().getDeclaredFields()){
			varDep.put(f.getName(), new Vector<DependencyTask>());
			
			for (Annotation an : f.getAnnotations()) {
				if(an instanceof writable && ((writable) an).isWritable() == true){
					hasWrites = true;
					break;
				}
			}
		}
	}

	protected abstract void react(Object obj);

	@Read
	public void sendMessage(final Object obj) {
		if(!hasWrites){
			
			Task t1 = AeminiumRuntime.rt.createNonBlockingTask(new Body(){	
				@Override
				public void execute(Runtime rt, Task current)
						throws Exception {
					
					react(obj);
					
				}}, Runtime.NO_HINTS);

			AeminiumRuntime.rt.schedule(t1, Runtime.NO_PARENT, Runtime.NO_DEPS);
			
		} else{	
			Task t1 = AeminiumRuntime.rt.createAtomicTask(new Body(){		
				@Override
				public void execute(Runtime rt, Task current)
						throws Exception {
					
					react(obj);
					
				}},myDataGroup, Runtime.NO_HINTS);
			
			AeminiumRuntime.rt.schedule(t1, Runtime.NO_PARENT, Runtime.NO_DEPS);
		}
	}
	
	@Read
	public Hashtable<String,Vector<DependencyTask>> getvarDep(){
		return varDep;
	}
	
	@Read
	public ArrayList<String> getMethodsName(){
		return methods;
	}
	
	@Read
	public Hashtable<String,HashMap<String,Boolean>> getMethodsWrites(){
		return methodsWrites;
	}
}
