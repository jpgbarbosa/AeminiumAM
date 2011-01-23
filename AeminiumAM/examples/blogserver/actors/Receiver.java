package examples.blogserver.actors;

import actor.Actor;
import annotationsVar.writable;

public class Receiver extends Actor{
	@writable
	int x;
	
	long workTime;
	
	@Override
	protected void react(Object obj) {
		//System.out.println(obj);
	}
	
	private void work(){
		long sleepTime = workTime; // convert to nanoseconds
	    long startTime = System.nanoTime();
	    while ((System.nanoTime() - startTime) < sleepTime) {}
	}

}
