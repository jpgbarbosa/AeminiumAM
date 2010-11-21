package actor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Vector;

import byteCodeOperations.ByteCodeOpASM;

import aeminium.runtime.Body;
import aeminium.runtime.DataGroup;
import aeminium.runtime.Runtime;
import aeminium.runtime.Task;

public class Dispatcher {
	
	public static void handle(Actor actor, String name, final Object msg) {
		final Method m;
		final Class<?> c = actor.getClass();
		
		if ((m = checkMethod(c, name)) != null) {
			
			HashMap<String,Boolean> varUsed = ByteCodeOpASM.getWritableFields(name, actor);
			
			//temos as variaveis escritas e temos a lista, é só obter as deps e passar
			if (!methodIsWritable(actor,name,varUsed)) {
				System.out.println(m.getName()+" is going to be par from dispatcher");

				Task t1 = AeminiumRuntime.rt.createNonBlockingTask(new Body() {
					@Override
					public void execute(Runtime rt, Task current)
							throws Exception {
						try {
							Object t = c.newInstance();

							try {
								m.setAccessible(true);

								m.invoke(t, msg);

								// Handle any exceptions thrown by method to be
								// invoked.
							} catch (InvocationTargetException x) {
								x.getCause();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, Runtime.NO_HINTS);
				
				AeminiumRuntime.rt.schedule(t1, Runtime.NO_PARENT,
						getFuncDependencies(actor, varUsed, t1));

			} else {
				System.out.println(m.getName()+" is going to be an Atomic task from dispatcher");

				/* Useless Datagroup created to pass as arg in createAtomicTask */
				DataGroup dg = AeminiumRuntime.rt.createDataGroup();

				Task t1 = AeminiumRuntime.rt.createAtomicTask(new Body() {
					@Override
					public void execute(Runtime rt, Task current)
							throws Exception {
						try {
							Object t = c.newInstance();

							try {
								m.setAccessible(true);

								m.invoke(t, msg);

								// Handle any exceptions thrown by method to be
								// invoked.
							} catch (InvocationTargetException x) {
								x.getCause();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, dg, Runtime.NO_HINTS);
				
				AeminiumRuntime.rt.schedule(t1, Runtime.NO_PARENT,
						getFuncDependencies(actor, varUsed, t1));
			}
		} else {
			System.out.println("Inexistent method '"+name+"'.");
		}
	}
	
	/*
	private static boolean methodCanBeParallelized(Class<?> c, Method m, Actor a) {
		
		for (Annotation an : m.getAnnotations()) {
			if (an instanceof VarUsed) {
				String [] vars = ((VarUsed) an).varNames().split(" ");

				for(int i=0; i<vars.length; i++){
					try {
						for(Annotation ca :a.getClass().getDeclaredField(vars[i]).getAnnotations()){
							if(ca instanceof writable && ((writable) ca).isWritable()){
								return false;
							}
						}
					} catch (SecurityException e) {
						System.out.println("In Dispatcher.java, methodCanBeParallelized. Security problem!");
						e.printStackTrace();
					} catch (NoSuchFieldException e) {
						System.out.println("In Dispatcher.java, methodCanBeParallelized. Field not found!");
						e.printStackTrace();
					}
				}
				return true;
			}
		}
		return false;
		}
	*/
	
	private static boolean methodIsWritable(Actor a, String methodName, HashMap<String,Boolean> varUsed) {
		for (Field f: a.getClass().getFields()) {
		    for (Entry<String, Boolean> entry : varUsed.entrySet()) {
				if(f.getName().equals(entry.getKey()) && entry.getValue() == true){
					return true;
				}
			}
		}
		return false;
	}

	private static Method checkMethod(Class<?> c, String name) {

		Method[] allMethods = c.getDeclaredMethods();
		for (Method m : allMethods) {
			if (m.getName() == name)
				return m;
		}

		return null;
	}
	
	/*TODO: must be synchronized*/
	private static Collection<Task> getFuncDependencies(Actor actor, HashMap<String, Boolean> usedVars, Task task){
				
		ArrayList<Task> deps = new ArrayList<Task>();
		
		for (Entry<String, Boolean> entry : usedVars.entrySet()) {
			String varName = entry.getKey();
			
			/* Get all the current tasks that this task is dependent */
			if(actor.getvarDep().containsKey(varName)){
				for(DependencyTask t :actor.getvarDep().get(varName)){
					deps.add(t.task);
				}
				/* Actualize the variable dependencies according to this new task */
				refreshVarDeps(actor.getvarDep().get(varName), task, entry.getValue());
			}
		}
		
		return deps;
	}
	
	private static void refreshVarDeps(Vector<DependencyTask> v, Task task, Boolean isWritable){	
		if(v.isEmpty()){
			v.add(new DependencyTask(task,isWritable));
		}
			else{
			if(isWritable || v.get(v.size()-1).isWritable){
				v.clear();
				v.add(new DependencyTask(task,isWritable));
			} else {
				v.add(new DependencyTask(task,isWritable));
			}
		}		
	}
}
