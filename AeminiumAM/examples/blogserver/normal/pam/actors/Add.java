package examples.blogserver.normal.pam.actors;

import actor.Actor;
import actor.annotations.*;

public class Add extends Actor{
	private Users users;
	private Posts post;
	private Receiver receiver;
	private long workTime;
	
	public Add(Users users, Posts post, Receiver receiver, long workTime){
		super();
		
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

	@Read
	public void addMessage(String user, String msg) {
		users.requestPermission(user, msg);		
	}

	@Read
	public void confirmReq(String user, String msg, boolean b) {
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
