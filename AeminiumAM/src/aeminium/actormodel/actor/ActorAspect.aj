package aeminium.actormodel.actor;

import java.util.ArrayList;
import java.util.Collection;

import aeminium.runtime.Body;
import aeminium.runtime.Hints;
import aeminium.runtime.Runtime;
import aeminium.runtime.Task;

public aspect ActorAspect {
	private aeminium.runtime.Runtime rt;
	private long startTime;

	// don't access to public fields => public fields get unusable
	declare warning: (   get(public * aeminium.actormodel.actor.Actor+.*) 
	               || set(public * aeminium.actormodel.actor.Actor+.*) ) 
	               && !withincode(new(..))
	               : "Cannot access public fields on an Actor.";
	// don't access to public fields => public fields get unusable
	declare warning: (   get(protected * aeminium.actormodel.actor.Actor+.*) 
				    || set(protected * aeminium.actormodel.actor.Actor+.*))
				   && !within(aeminium.actormodel.actor.Actor) 
				   && !within(aeminium.actormodel.actor.ActorAspect)  : "Cannot access protected fields on an Actor.";
	// don't allow protected methods
	declare error:    within(aeminium.actormodel.actor.Actor+)
	               && execution(protected * aeminium.actormodel.actor.Actor+.*(..)): "Actor cannot have protected methods.";
	// check that Read methods do not contain field assignments
	declare warning:    withincode(@aeminium.actormodel.annotations.Read * aeminium.actormodel.actor.Actor+.*(..)) 
	               && set(* aeminium.actormodel.actor.Actor+.*) : "Cannot write fields in read only methods,";
	// check that Read methods do not contain method calls to write methods
	declare warning:    withincode(@aeminium.actormodel.annotations.Read * aeminium.actormodel.actor.Actor+.*(..)) 
	               && call(@aeminium.actormodel.annotations.Write * aeminium.actormodel.actor.Actor+.*(..)) : "Cannot call field writing methods in read only methods,";
	// check that all methods of Actors have Read|Write annotations
	declare error:    within(aeminium.actormodel.actor.Actor+) 
	               && execution( !@(aeminium.actormodel.annotations.*) * aeminium.actormodel.actor.Actor+.*(..))
	               && !execution(static * aeminium.actormodel.actor.Actor+.*(..)) : "Actor method need aeminium.actormodel.annotations";
	// make sure that actor has no static methods except main
	declare error:    within(aeminium.actormodel.actor.Actor+) 
	               && execution(static * aeminium.actormodel.actor.Actor+.*(..))
	               && !execution(static * aeminium.actormodel.actor.Actor+.main(String[])): "Actor cannot have static methods";
	// don't call public methods from internal ones (bad practice)
	declare warning:    within(aeminium.actormodel.actor.Actor+) 
	               && withincode(private * aeminium.actormodel.actor.Actor+.*(..))
	               && call(public * aeminium.actormodel.actor.Actor+.*(..)): "You should not call public methods from an internal method.";

	// All public read methods of actors
	pointcut PublicReadActorMethods() :	(execution (@aeminium.actormodel.annotations.Read public void aeminium.actormodel.actor.Actor+.*(..)) 
											&& !(execution (static * aeminium.actormodel.actor.Actor+.*(..))));

	// All public write methods of actors
	pointcut PublicWriteActorMethods() : (execution (@aeminium.actormodel.annotations.Write public void  aeminium.actormodel.actor.Actor+.*(..)) 
											&& !(execution (static * aeminium.actormodel.actor.Actor+.*(..))));

	// the main methods
	pointcut MainMethod(): (execution(public static void *.main(String[])));

	// replace all public read methods calls
	void around(): PublicReadActorMethods() {
		Actor actor = (Actor) thisJoinPoint.getTarget();

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
		synchronized(actor) {
			if (actor.previousTaskWasWriter) {
				actor.lastReaders.clear();
				actor.previousTaskWasWriter = false;
			}
			actor.lastReaders.add(task);
			
			deps = new ArrayList<Task>(1);
			deps.add(actor.lastWriter);
		}
		rt.schedule(task, Runtime.NO_PARENT, deps);
	}

	// replace all public write methods calls
	void around(): PublicWriteActorMethods() {
		Actor actor = (Actor) thisJoinPoint.getTarget();

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

		Collection<Task> deps = Runtime.NO_DEPS;
		synchronized (actor) {
			if (actor.previousTaskWasWriter) {
				deps = new ArrayList<Task>(1);
				deps.add(actor.lastWriter);
			} else {
				deps = new ArrayList<Task>(actor.lastReaders);
			}
			actor.previousTaskWasWriter = true;
			actor.lastWriter = task;
		}
		rt.schedule(task, Runtime.NO_PARENT, deps);

	}

	before(): MainMethod() { 
		rt = aeminium.runtime.implementations.Factory.getRuntime();
		rt.init();
		startTime = System.nanoTime();
	}
	
	after(): MainMethod() {
		rt.shutdown();
		System.out.println("Took (nanoseconds): " + (System.nanoTime() - startTime));
	}
}
