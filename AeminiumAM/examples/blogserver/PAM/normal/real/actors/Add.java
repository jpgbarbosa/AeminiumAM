package examples.blogserver.PAM.normal.real.actors;

import examples.blogserver.PAM.normal.real.AddPost;
import examples.blogserver.PAM.normal.real.AskPermission;
import examples.blogserver.PAM.normal.real.PermissionResponse;
import examples.blogserver.PAM.normal.real.PutRequest;
import actor.Actor;

public class Add extends Actor{
	Users users;
	Posts post;
	Receiver receiver;
	long workTime;
	
	boolean useSpin;
	
	public Add(Users users, Posts post, Receiver receiver, long workTime, boolean useSpin){
		super();
		
		this.useSpin = useSpin;
		
		this.users = users;
		this.post = post;
		this.receiver = receiver;
		
		this.workTime = workTime;
				
	}
	
	@Override
	protected void react(Object obj) {
		if(useSpin){
			work();
		}
		if( obj instanceof PutRequest){
			users.sendMessage(new AskPermission(new PutRequest(((PutRequest)obj).user,((PutRequest)obj).post)));
		}
		else if( obj instanceof PermissionResponse){
			if(((PermissionResponse)obj).accepted){
				post.sendMessage(new AddPost(((PermissionResponse)obj).req.post,((PermissionResponse)obj).req.user));
			} else {
				receiver.sendMessage("Invalid User");
			}
		}
	}
	
	/* nao estao a ser usadas visto que serao igualmente NB tasks */
	public void sendNotification(Object obj){
		if(useSpin){
			work();
		}
		receiver.sendMessage("Invalid User: "+((PermissionResponse)obj).req.user);
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
