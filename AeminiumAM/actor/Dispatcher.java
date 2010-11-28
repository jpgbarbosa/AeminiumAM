package actor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Vector;

import aeminium.runtime.Body;
import aeminium.runtime.DataGroup;
import aeminium.runtime.Runtime;
import aeminium.runtime.Task;
import annotations.VarType;

public class Dispatcher {
	
	public static void handle(Actor actor, String name, final Object msg) {
		final Method m;
		final Class<?> c = actor.getClass();
		
		if ((m = checkMethod(c, name)) != null) {
			
			HashMap<String,Boolean> varUsed = getFieldsType(m, actor);
			
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
	
	private static HashMap<String, Boolean> getFieldsType(Method m,
			Actor actor) {
		
		String [] writeVars = m.getAnnotation(VarType.class).isWritable().split(" ");
		String [] readVars = m.getAnnotation(VarType.class).isReadOnly().split(" ");
		
		HashMap<String, Boolean> hash = new HashMap<String, Boolean>();
		int i;
		
		for(i=0; i<writeVars.length; i++){
			hash.put(writeVars[i], true);
		}
		
		for(i=0; i<readVars.length; i++){
			hash.put(readVars[i], false);
		}
		
		return hash;
	}
	
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
