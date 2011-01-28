package examples.blogserver.normal.pam.actors;

import actor.Actor;
import actor.annotations.*;

public class Reader extends Actor{
	Posts posts;
	long workTime=0;

	public Reader(Posts posts, long workTime){
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
		posts.readPost(id,user);
	}

}
