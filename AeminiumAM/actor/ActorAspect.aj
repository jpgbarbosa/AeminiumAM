package actor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import aeminium.runtime.Body;
import aeminium.runtime.Hints;
import aeminium.runtime.Runtime;
import aeminium.runtime.Task;

public aspect ActorAspect {
	// private aeminium.runtime.Runtime rt;

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
	
	// All public write methods of actors
	pointcut PublicEndActorMethods() : (execution (@annotations.End public void  actor.Actor+.*(..)) 
											&& !(execution (static * actor.Actor+.*(..))));


	// the main methods
	pointcut MainMethod(): (execution(public static void *.main(String[])));

	// replace all public read methods calls
	void around(): PublicReadActorMethods() {
		Actor actor = (Actor) thisJoinPoint.getTarget();
		
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
		
		try {
			actor.queue.put(task);
		} catch (Exception e) {
			System.out.println("queue put in aspectJ reader");
			e.printStackTrace();
		}
		
	}

	// replace all public write methods calls
	void around(): PublicWriteActorMethods() {
		Actor actor = (Actor) thisJoinPoint.getTarget();
		
		Task task = actor.rt.createNonBlockingTask(new Body() {
			@Override
			public void execute(Runtime rt, Task current) throws Exception {
					System.out.println("here before W");
					proceed();
			}

			@Override
			public String toString() {
				return "Writer";
			}
		}, Hints.NO_HINTS);
		
		try {
			actor.queue.put(task);
		} catch (InterruptedException e) {
			System.out.println("queue put in aspectJ writer");
			e.printStackTrace();
		}

	}
	
	// replace all public write methods calls
	void around(): PublicEndActorMethods() {
		Actor actor = (Actor) thisJoinPoint.getTarget();

		
		Task task = actor.rt.createNonBlockingTask(new Body() {
			@Override
			public void execute(Runtime rt, Task current) throws Exception {
				System.out.println("here before E");
				proceed();
			}

			@Override
			public String toString() {
				return "End";
			}
		}, Hints.NO_HINTS);
		
		try {
			actor.queue.put(task);
		} catch (InterruptedException e) {
			System.out.println("queue put in aspectJ End");
			e.printStackTrace();
		}

	}

	// TODO: remove comment after benchmarks!
	/*
	 * // setup before we start before(): MainMethod() { rt =
	 * aeminium.runtime.implementations.Factory.getRuntime(); rt.init(); }
	 * 
	 * // cleanup after the program has finished after(): MainMethod() {
	 * rt.shutdown(); }
	 */
}
