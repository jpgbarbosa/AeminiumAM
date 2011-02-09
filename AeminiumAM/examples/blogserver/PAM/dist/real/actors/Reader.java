package examples.blogserver.PAM.dist.real.actors;

import actor.Actor;

public class Reader extends Actor{
	Posts posts;
	long workTime=0;
	boolean useSpin;

	public Reader(Posts posts, long workTime, int numCopies, boolean useSpin){
		this.useSpin = useSpin;
		this.posts = posts;
		this.workTime = workTime;
	}
	
	@Override
	protected void react(Object obj) {
		if(useSpin){
			work();
		}
		posts.sendMessage(obj);
	}
	
	private void work(){
		long sleepTime = workTime; // convert to nanoseconds
	    long startTime = System.nanoTime();
	    while ((System.nanoTime() - startTime) < sleepTime) {}
	}

}
