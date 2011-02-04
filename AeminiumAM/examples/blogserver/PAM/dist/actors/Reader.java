package examples.blogserver.PAM.dist.actors;

import actor.Actor;
import annotations.writable;

public class Reader extends Actor{
	Posts posts;
	long workTime=0;

	public Reader(Posts posts, long workTime, int numCopies){
		this.posts = posts;
		this.workTime = workTime;
	}
	
	@Override
	protected void react(Object obj) {
		posts.sendMessage(obj);
	}
	
	private void work(){
		long sleepTime = workTime; // convert to nanoseconds
	    long startTime = System.nanoTime();
	    while ((System.nanoTime() - startTime) < sleepTime) {}
	}

}
