package aeminium.actor;

import java.util.Collection;
import java.util.LinkedList;

import aeminium.runtime.Body;
import aeminium.runtime.Hints;
import aeminium.runtime.Runtime;
import aeminium.runtime.Task;

public aspect ActorAspect {
	private aeminium.runtime.Runtime rt;
	
	// don't access to public fields => public fields get unusable
	declare error:    get(public * aeminium.actor.Actor+.*) 
	               || set(public * aeminium.actor.Actor+.*): "Cannot access public fields on an Actor.";
    // don't access to public fields => public fields get unusable
	declare error: (   get(protected * aeminium.actor.Actor+.*) 
				    || set(protected * aeminium.actor.Actor+.*))
				   && !within(aeminium.actor.Actor) 
				   && !within(aeminium.actor.ActorAspect)  : "Cannot access protected fields on an Actor.";
	// don't allow protected methods
	declare error:    within(aeminium.actor.Actor+)
	               && execution(protected * aeminium.actor.Actor+.*(..)): "Actor cannot have protected methods.";
	// check that Read methods do not contain field assignments
	declare error:    withincode(@aeminium.actor.annotations.Read * aeminium.actor.Actor+.*(..)) 
	               && set(* aeminium.actor.Actor+.*) : "Cannot write fields in read only methods,";
	// check that Read methods do not contain method calls to write methods
	declare error:    withincode(@aeminium.actor.annotations.Read * aeminium.actor.Actor+.*(..)) 
	               && call(@aeminium.actor.annotations.Write * aeminium.actor.Actor+.*(..)) : "Cannot call field writing methods in read only methods,";
	// check that all methods of Actors have Read|Write annotations
	declare error:    within(aeminium.actor.Actor+) 
	               && execution( !@(aeminium.actor.annotations.*) * aeminium.actor.Actor+.*(..)) 
	               && !execution(static * aeminium.actor.Actor+.*(..)): "Actor method need annotations.";
	// make sure that actor has no static methods except main
	declare error:    within(aeminium.actor.Actor+) 
	               && execution(static * aeminium.actor.Actor+.*(..))
	               && !execution(static * aeminium.actor.Actor+.main(String[])): "Actor cannot have static methods";
	// don't call public methods from internal ones (bad practice)
	declare warning:    within(aeminium.actor.Actor+) 
	               && withincode(private * aeminium.actor.Actor+.*(..))
	               && call(public * aeminium.actor.Actor+.*(..)): "You should not call public methods from an internal method.";

	
	// All public read methods of actors
	pointcut PublicReadActorMethods() : (execution (@aeminium.actor.annotations.Read public void  aeminium.actor.Actor+.*(..)) && !(execution (static * aeminium.actor.Actor+.*(..))));
	// All public write methods of actors
	pointcut PublicWriteActorMethods() : (execution (@aeminium.actor.annotations.Write public void  aeminium.actor.Actor+.*(..)) && !(execution (static * aeminium.actor.Actor+.*(..))));
    // the main methods
	pointcut MainMethod(): (execution(public static void *.main(String[])));
	
	
	// replace all public read methods calls
	void around (): PublicReadActorMethods() {
		Actor actor = (Actor)thisJoinPoint.getTarget();
		synchronized (actor) {
			Task task = rt.createNonBlockingTask(new Body() {
				@Override
				public void execute(Runtime rt, Task current) throws Exception {
					proceed();				
				}
				
				@Override 
				public String toString() {
					return "Reader";
				}
			}, Hints.NO_HINTS);
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
			rt.schedule(task, Runtime.NO_PARENT, deps);
		}
	}
	
	// replace all public write methods calls
	void around (): PublicWriteActorMethods() {
		Actor actor = (Actor)thisJoinPoint.getTarget();
		synchronized (actor) {
			Task task = rt.createNonBlockingTask(new Body() {
				@Override
				public void execute(Runtime rt, Task current) throws Exception {
					proceed();				
				}
				
				@Override 
				public String toString() {
					return "Writer";
				}
			}, Hints.NO_HINTS);
			Collection<Task> deps = actor.previousTasks;
			actor.previousTasks = new LinkedList<Task>();
			actor.previousTasks.add(task);
			actor.latestWriters = actor.previousTasks;
			actor.previousTasksAreWriters = true;
			rt.schedule(task, Runtime.NO_PARENT, deps);
		}
	}
	
	// setup before we start
	before(): MainMethod() {
		rt = aeminium.runtime.implementations.Factory.getRuntime();
		rt.init();
	}
	
	// cleanup after the program has finished
	after(): MainMethod() {
		rt.shutdown();
	}
}
