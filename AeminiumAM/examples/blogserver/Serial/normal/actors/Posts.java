package examples.blogserver.Serial.normal.actors;

import java.util.Hashtable;

import examples.blogserver.Serial.normal.Gen;
import examples.blogserver.Serial.normal.AddPost;
import examples.blogserver.Serial.normal.ReadPost;

import actor.Actor;
import annotations.writable;

public class Posts extends Actor{
	Receiver receiver;
	
	@writable
	int lastIndex;
	
	Hashtable<Integer,String> hashPosts;
	Hashtable<Integer,String> hashUsersID;
	
	long workTime;
	
	boolean useSpin;
	
	public Posts(Receiver receiver, int postsNum, long workTime, boolean useSpin){
		super();
		this.useSpin = useSpin;
		this.workTime = workTime;
		
		hashPosts = new Hashtable<Integer, String>();
		hashUsersID = new Hashtable<Integer, String>();
		
		int i;
		Gen g = new Gen(postsNum);
		for( i=0; i<postsNum; i++){
			hashPosts.put(i, g.getSentences()[i]);
			hashUsersID.put(i, "system");
		}
		
		lastIndex = i--;
		
		this.receiver = receiver;
	}

	@Override
	protected void react(Object obj) {
		if(useSpin){
			work();
		}
		if(obj instanceof AddPost){
			hashPosts.put(++lastIndex, ((AddPost)obj).post);
			hashUsersID.put(lastIndex, ((AddPost)obj).user);
			
		} else if( obj instanceof ReadPost){
			//Dispatcher.handle(this, "sendNotification", (ReadPost)obj);
			sendNotification((ReadPost)obj);
		}
	}
	
	public void sendNotification(ReadPost rp){
		if(useSpin){
			work();
		}
		if(rp.id>=lastIndex){
			receiver.sendMessage("User "+rp.user+" requested post "+(lastIndex-1)+" by "+
					hashUsersID.get(lastIndex-1) + ": "+hashPosts.get(lastIndex-1));
		} else {
			receiver.sendMessage("User "+rp.user+" requested post "+rp.id+" by "+
					hashUsersID.get(rp.id) + ": "+hashPosts.get(rp.id));
		}
		
	}
	
	private void work(){
		long sleepTime = workTime; // convert to nanoseconds
	    long startTime = System.nanoTime();
	    while ((System.nanoTime() - startTime) < sleepTime) {}
	}
	
}
