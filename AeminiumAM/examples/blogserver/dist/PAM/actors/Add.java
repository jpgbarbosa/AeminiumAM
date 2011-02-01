package examples.blogserver.dist.PAM.actors;

import java.util.Random;

import actor.Actor;
import actor.annotations.*;
import aeminium.runtime.Runtime;

public class Add extends Actor{
	Posts post;
	Users [] usersArray;
	Receiver [] receiverArray;

	long workTime;
	int numCopies;

	Random ran;

	public Add(Users[] usersArray, Posts post, Receiver[] receiverArray, long workTime, int numCopies, Runtime rt2){
		super();

		this.usersArray = usersArray;
		this.post = post;
		this.receiverArray = receiverArray;

		this.numCopies = numCopies;
		this.workTime = workTime;

		ran = new Random(5);
		
		this.rt =  rt2; 

	}
	
	@Write
	public void work(){
		long sleepTime = workTime; // convert to nanoseconds
	    long startTime = System.nanoTime();
	    while ((System.nanoTime() - startTime) < sleepTime) {}
	}

	@Read
	public void addMessage(String user, String msg) {
		int x = ran.nextInt(numCopies);
		if(usersArray[x]==null)
			System.out.println("here");
		usersArray[x].requestPermission(user, msg);		
	}

	@Read
	public void confirmReq(String user, String msg, boolean b) {
		if(b){
			/*TODO: declare of error was changed to warning. 
			 * Does not make sense set this method as a Write
			 */
			post.addPost(user, msg);
		} else {
			receiverArray[ran.nextInt(numCopies)].sendMessage("Invalid User");
		}
	}

}
