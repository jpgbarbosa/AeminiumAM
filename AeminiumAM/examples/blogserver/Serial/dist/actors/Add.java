package examples.blogserver.Serial.dist.actors;

import java.util.Random;

import examples.blogserver.Serial.dist.AddPost;
import examples.blogserver.Serial.dist.AskPermission;
import examples.blogserver.Serial.dist.PermissionResponse;
import examples.blogserver.Serial.dist.PutRequest;
import actor.Actor;
import annotations.writable;

public class Add extends Actor{
	@writable
	Posts post;
	@writable
	Users [] usersArray;
	@writable
	Receiver [] receiverArray;
	
	long workTime;
	int numCopies;
	
	boolean useSpin;
	
	Random ran;
	
	public Add(Users[] usersArray, Posts post, Receiver[] receiverArray, long workTime, int numCopies, boolean useSpin){
		super();
		this.useSpin = useSpin;
		this.usersArray = usersArray;
		this.post = post;
		this.receiverArray = receiverArray;
		
		this.numCopies = numCopies;
		this.workTime = workTime;
		
		ran = new Random(5);
				
	}
	
	@Override
	protected void react(Object obj) {
		if(useSpin){
			work();
		}
		if( obj instanceof PutRequest){
			usersArray[ran.nextInt(numCopies)].sendMessage(new AskPermission(new PutRequest(((PutRequest)obj).user,((PutRequest)obj).post)));
		}
		else if( obj instanceof PermissionResponse){
			if(((PermissionResponse)obj).accepted){
				post.sendMessage(new AddPost(((PermissionResponse)obj).req.post,((PermissionResponse)obj).req.user));
			} else {
				receiverArray[ran.nextInt(numCopies)].sendMessage("Invalid User");
			}
		}
	}
	
	/* nao estao a ser usadas visto que serao igualmente NB tasks */
	public void sendNotification(Object obj){
		if(useSpin){
			work();
		}
		receiverArray[ran.nextInt(numCopies)].sendMessage("Invalid User: "+((PermissionResponse)obj).req.user);
	}
	
	public void putPost(Object obj){
		if(useSpin){
			work();
		}
		post.sendMessage(new AddPost(((PermissionResponse)obj).req.post,((PermissionResponse)obj).req.user));
	}
	
	private void work(){
		long sleepTime = workTime; // convert to nanoseconds
	    long startTime = System.nanoTime();
	    while ((System.nanoTime() - startTime) < sleepTime) {}
	}

}
