package examples.blogserver.dist.Serial.actors;

import actor.Actor;
import actor.annotations.*;
import aeminium.runtime.Runtime;

public class Reader extends Actor{
	Posts posts;
	long workTime=0;
	
	boolean useSpin = false;

	
	public Reader(Posts posts, long workTime, int numCopies, Runtime rt2, boolean useSpin){
		this.useSpin = useSpin;
		this.posts = posts;
		this.workTime = workTime;
		
		this.rt = (Runtime) rt2;
	}
	@Write
	public void work(){
		long sleepTime = workTime; // convert to nanoseconds
	    long startTime = System.nanoTime();
	    while ((System.nanoTime() - startTime) < sleepTime) {}
	}

	@Write
	public void reqReadPost(int id, String user) {
		if(useSpin){
			long sleepTime = workTime; // convert to nanoseconds
		    long startTime = System.nanoTime();
		    while ((System.nanoTime() - startTime) < sleepTime) {}
		}
		posts.readPost(id,user);
	}

}
