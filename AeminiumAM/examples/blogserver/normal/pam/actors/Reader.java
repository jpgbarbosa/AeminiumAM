package examples.blogserver.normal.pam.actors;

import actor.Actor;
import actor.annotations.*;
import aeminium.runtime.Runtime;

public class Reader extends Actor{
	Posts posts;
	long workTime=0;
	
	boolean useSpin;

	public Reader(Posts posts, long workTime, Runtime rt, boolean useSpin){
		super();
		this.useSpin = useSpin;
		this.rt = rt;
		this.posts = posts;
		this.workTime = workTime;
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
