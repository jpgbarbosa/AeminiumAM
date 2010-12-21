package examples.blogserver.actors;

import examples.blogserver.AddPost;
import examples.blogserver.AskPermission;
import examples.blogserver.PermissionResponse;
import examples.blogserver.PutRequest;
import actor.Actor;
import annotations.writable;

public class Add extends Actor{

	Users users;
	Posts post;
	Receiver receiver;

	
	public Add(Users users, Posts post, Receiver receiver){
		super();
		
		this.users = users;
		this.post = post;
		this.receiver = receiver;
				
	}
	
	@Override
	protected void react(Object obj) {
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
		receiver.sendMessage("Invalid User: "+((PermissionResponse)obj).req.user);
	}
	
	public void putPost(Object obj){
		post.sendMessage(new AddPost(((PermissionResponse)obj).req.post,((PermissionResponse)obj).req.user));
	}

}
