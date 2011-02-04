package examples.blogserver.normal.Serial.actors;

import actor.Actor;
import actor.annotations.*;

public class Receiver extends Actor{

	long workTime;
	
	boolean useSpin = false;

	
	public Receiver( boolean useSpin){

		this.useSpin = useSpin;

	}

	@Write
	public void work(){
		long sleepTime = workTime; // convert to nanoseconds
	    long startTime = System.nanoTime();
	    while ((System.nanoTime() - startTime) < sleepTime) {}
	}

	@Write
	public void sendMessage(String msg) {
		if(useSpin){
			long sleepTime = workTime; // convert to nanoseconds
		    long startTime = System.nanoTime();
		    while ((System.nanoTime() - startTime) < sleepTime) {}
		}
		//System.out.println(msg);
	}

}
