package examples.blogserver.PAM.normal.actors;

import actor.Actor;
import annotations.writable;

public class Receiver extends Actor{
	
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
