package examples.blogserver.PAM.dist.real.actors;

import actor.Actor;

public class Receiver extends Actor{
	
	long workTime;
	
	boolean useSpin = false;

	public Receiver(){};
	
	public Receiver(boolean useSpin) {
		this.useSpin = useSpin;
	}

	@Override
	protected void react(Object obj) {
		if(useSpin){
			work();
		}
		//System.out.println(obj);
	}
	
	private void work(){
		long sleepTime = workTime; // convert to nanoseconds
	    long startTime = System.nanoTime();
	    while ((System.nanoTime() - startTime) < sleepTime) {}
	}

}
