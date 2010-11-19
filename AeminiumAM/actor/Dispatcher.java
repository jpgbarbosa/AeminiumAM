package actor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

import aeminium.runtime.Body;
import aeminium.runtime.DataGroup;
import aeminium.runtime.Runtime;
import aeminium.runtime.Task;

public class Dispatcher {
	
	public static void handle(final Actor actor, final String name, final Object msg) {
		final Method m;
		final Class<?> c = actor.getClass();
		
		if ((m = checkMethod(c, name)) != null) {
			
			ArrayList<TypeVar> varUsed = ByteCodeOpASM.getWritableFields(name);
			
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

				getFuncDependencies(actor, varUsed, t1);
				
				AeminiumRuntime.rt.schedule(t1, Runtime.NO_PARENT,
						Runtime.NO_DEPS);

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

				getFuncDependencies(actor, varUsed, t1);
				
				AeminiumRuntime.rt.schedule(t1, Runtime.NO_PARENT,
						Runtime.NO_DEPS);
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
	
	private static boolean methodIsWritable(Actor a, String methodName, ArrayList<TypeVar> varUsed) {
				
		for (Field f: a.getClass().getFields()) {
			for(TypeVar s : varUsed){
				if(f.getName().equals(s.name)){
					for (Annotation an : f.getAnnotations()) {
						if(an instanceof writable && ((writable) an).isWritable() == true){
							return false;
						}
					}
				}
			}
		}
		return true;
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
	private static Collection<Task> getFuncDependencies(Actor actor, ArrayList<TypeVar> usedVars, Task task){

		ArrayList<Task> deps = new ArrayList<Task>();
		
		for(TypeVar var: usedVars){
			for(DependencyTask t:actor.varDep.get(var.name)){
				deps.add(t.task);
			}
		}
		
		for(TypeVar var: usedVars){
			refreshVarDeps(actor.varDep.get(var.name), task);
		}
	
		return deps;
	}
	
	private static void refreshVarDeps(Vector<DependencyTask> v, Task task){
	
	}
}
