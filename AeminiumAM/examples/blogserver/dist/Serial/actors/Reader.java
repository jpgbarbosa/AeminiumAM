package examples.blogserver.dist.Serial.actors;

import actor.Actor;
import actor.annotations.*;
import aeminium.runtime.Runtime;

public class Reader extends Actor{
	Posts posts;
	long workTime=0;

	public Reader(Posts posts, long workTime, int numCopies, Runtime rt2){
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
		posts.readPost(id,user);
	}

}