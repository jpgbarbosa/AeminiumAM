package examples.blogserver.dist.PAM.extreme.actors;

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
	
	@Read
	public void work(){
		long sleepTime = workTime; // convert to nanoseconds
	    long startTime = System.nanoTime();
	    while ((System.nanoTime() - startTime) < sleepTime) {}
	}

	@Read
	public void reqReadPost(int id, String user) {
		if(useSpin){
			long sleepTime = workTime; // convert to nanoseconds
		    long startTime = System.nanoTime();
		    while ((System.nanoTime() - startTime) < sleepTime) {}
		}
		posts.readPost(id,user);
	}

}
