package examples.blogserver.normal.Serial.actors;

import actor.Actor;
import actor.annotations.*;
import aeminium.runtime.Runtime;

public class Add extends Actor{
	private Users users;
	private Posts post;
	private Receiver receiver;
	private long workTime;
	boolean useSpin;

	public Add(Users users, Posts post, Receiver receiver, long workTime, Runtime rt, boolean useSpin){
		super();
		
		this.rt = rt;
		this.useSpin = useSpin;

		this.users = users;
		this.post = post;
		this.receiver = receiver;
		
		this.workTime = workTime;
				
		
	}
	
	@Write
	public void work(){
		long sleepTime = workTime; // convert to nanoseconds
	    long startTime = System.nanoTime();
	    while ((System.nanoTime() - startTime) < sleepTime) {}
	}

	@Write
	public void addMessage(String user, String msg) {
		if(useSpin){
			long sleepTime = workTime; // convert to nanoseconds
		    long startTime = System.nanoTime();
		    while ((System.nanoTime() - startTime) < sleepTime) {}
		}
		users.requestPermission(user, msg);		
	}

	@Write
	public void confirmReq(String user, String msg, boolean b) {
		if(useSpin){
			long sleepTime = workTime; // convert to nanoseconds
		    long startTime = System.nanoTime();
		    while ((System.nanoTime() - startTime) < sleepTime) {}
		}
		if(b){
			/*TODO: declare of error was changed to warning. 
			 * Does not make sense set this method as a Write
			 */
			post.addPost(user, msg);
		} else {
			receiver.sendMessage("Invalid User");
		}
	}

}
