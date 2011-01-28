package examples.blogserver.Serial.normal.actors;

import actor.Actor;
import annotations.writable;

public class Reader extends Actor{
	@writable
	Posts posts;
	@writable
	long workTime=0;

	public Reader(Posts posts, long workTime){
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
