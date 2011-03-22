package actor;

import java.util.Collection;
import java.util.LinkedList;

import aeminium.runtime.Body;
import aeminium.runtime.Hints;
import aeminium.runtime.Runtime;
import aeminium.runtime.Task;

public aspect ActorAspect {
	//private aeminium.runtime.Runtime rt;
	
	// don't access to public fields => public fields get unusable
	declare warning: (   get(public * actor.Actor+.*) 
	               || set(public * actor.Actor+.*) ) 
	               && !withincode(new(..))
	               : "Cannot access public fields on an Actor.";
    // don't access to public fields => public fields get unusable
	declare warning: (   get(protected * actor.Actor+.*) 
				    || set(protected * actor.Actor+.*))
				   && !within(actor.Actor) 
				   && !within(actor.ActorAspect)  : "Cannot access protected fields on an Actor.";
	// don't allow protected methods
	declare error:    within(actor.Actor+)
	               && execution(protected * actor.Actor+.*(..)): "Actor cannot have protected methods.";
	// check that Read methods do not contain field assignments
	declare warning:    withincode(@annotations.Read * actor.Actor+.*(..)) 
	               && set(* actor.Actor+.*) : "Cannot write fields in read only methods,";
	// check that Read methods do not contain method calls to write methods
	declare warning:    withincode(@annotations.Read * actor.Actor+.*(..)) 
	               && call(@annotations.Write * actor.Actor+.*(..)) : "Cannot call field writing methods in read only methods,";
	// check that all methods of Actors have Read|Write annotations
	declare error:    within(actor.Actor+) 
	               && execution( !@(actor.annotations.*) * actor.Actor+.*(..)) 
	               && !execution(static * actor.Actor+.*(..))
	               && !execution(public * actor.Actor(..)) : "Actor method need annotations.";
	// make sure that actor has no static methods except main
	declare error:    within(actor.Actor+) 
	               && execution(static * actor.Actor+.*(..))
	               && !execution(static * actor.Actor+.main(String[])): "Actor cannot have static methods";
	// don't call public methods from internal ones (bad practice)
	declare warning:    within(actor.Actor+) 
	               && withincode(private * actor.Actor+.*(..))
	               && call(public * actor.Actor+.*(..)): "You should not call public methods from an internal method.";

	
	// All public read methods of actors
	pointcut PublicReadActorMethods() :	(execution (@annotations.Read public void  actor.Actor+.*(..)) 
											&& !(execution (static * actor.Actor+.*(..))));
	// All public write methods of actors
	pointcut PublicWriteActorMethods() : (execution (@annotations.Write public void  actor.Actor+.*(..)) 
											&& !(execution (static * actor.Actor+.*(..))));
    // the main methods
	pointcut MainMethod(): (execution(public static void *.main(String[])));
	
	
	// replace all public read methods calls
	void around (): PublicReadActorMethods() {
		Actor actor = (Actor)thisJoinPoint.getTarget();
		synchronized (actor) {
			Task task = actor.rt.createNonBlockingTask(new Body() {
				@Override
				public void execute(Runtime rt, Task current) throws Exception {
					proceed();				
				}
				
				@Override 
				public String toString() {
					return "Reader";
				}
			}, Hints.NO_HINTS);
			long temp; 
			temp=System.nanoTime();
			Collection<Task> deps = Runtime.NO_DEPS;
			if ( actor.previousTasksAreWriters ) {
				deps = actor.previousTasks;
				actor.previousTasks = new LinkedList<Task>();
				actor.previousTasks.add(task);
				actor.previousTasksAreWriters = false;
			} else {
				actor.previousTasks.add(task);
				deps = actor.latestWriters;
			}
			actor.rt.schedule(task, Runtime.NO_PARENT, deps);
			actor.avg+=System.nanoTime()-temp;
			actor.ctr++;
		}
	}
	
	// replace all public write methods calls
	void around (): PublicWriteActorMethods() {
		long tempOut, tempIn; 

		final Actor actor = (Actor)thisJoinPoint.getTarget();
		
		tempOut = System.nanoTime();
		
		synchronized (actor) {
			actor.idCounter++;
			
			final int boxVar = actor.idCounter; 
			
			tempIn = System.nanoTime();
			
			Task task = actor.rt.createNonBlockingTask(new Body() {
				@Override
				public void execute(Runtime rt, Task current) throws Exception {
					long temp = 0;
					int id = 0;
					try{
						String m = current.toString();
						
						id = Integer.parseInt(m.substring(m.indexOf('<')+1, m.indexOf('>')));
						
						// Save time before task is scheduled
						temp = actor.mapActor.get(id);
						
						if(temp!=0)
							actor.mapActor.put(id,System.nanoTime()-temp);
						
					} catch (Exception e){
						System.out.println("Error inside aspectJ - execute - writes: "+e+" "+temp+" "+id);
					}
					
					proceed();
				}
				
				@Override 
				public String toString() {
					return boxVar+"";
				}
			}, Hints.NO_HINTS);
				
			
			Collection<Task> deps = actor.previousTasks;
			actor.previousTasks = new LinkedList<Task>();
			actor.previousTasks.add(task);
			actor.latestWriters = actor.previousTasks;
			actor.previousTasksAreWriters = true;
			
			// Save time before task is scheduled
			
			long t = System.nanoTime();
			actor.mapActor.put(actor.idCounter,t);
			
			
			actor.rt.schedule(task, Runtime.NO_PARENT, deps);
			
			actor.insideSideLock += System.nanoTime()-tempIn;
			
		}
		
		actor.outSideLock += System.nanoTime()-tempOut;
		
		actor.ctr++;
		

		

	}
	
	// TODO: remove comment after benchmarks! 
	/*
	// setup before we start
	before(): MainMethod() {
		rt = aeminium.runtime.implementations.Factory.getRuntime();
		rt.init();
	}
	
	// cleanup after the program has finished
	after(): MainMethod() {
		rt.shutdown();
	}
	*/
}
