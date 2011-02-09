package examples.blogserver.normal.pam.extreme.actors;

import actor.Actor;
import actor.annotations.*;

public class Receiver extends Actor{

	long workTime;
	
	boolean useSpin;
	
	public Receiver(boolean useSpin) {
		this.useSpin = useSpin;
	}

	@Read
	public void sendMessage(String msg) {
		if(useSpin){
			long sleepTime = workTime; // convert to nanoseconds
		    long startTime = System.nanoTime();
		    while ((System.nanoTime() - startTime) < sleepTime) {}
		}
		//System.out.println(msg);
	}

}
