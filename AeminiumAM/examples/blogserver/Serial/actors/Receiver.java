package examples.blogserver.Serial.actors;

import actor.Actor;
import actor.annotations.*;

public class Receiver extends Actor{

	long workTime;
	
	@Write
	public void work(){
		long sleepTime = workTime; // convert to nanoseconds
	    long startTime = System.nanoTime();
	    while ((System.nanoTime() - startTime) < sleepTime) {}
	}

	@Write
	public void sendMessage(String msg) {
		//System.out.println(msg);
	}

}
