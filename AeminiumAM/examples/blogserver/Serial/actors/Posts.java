package examples.blogserver.Serial.actors;

import java.util.Hashtable;

import examples.blogserver.PAM.Gen;

import actor.Actor;
import actor.annotations.*;

public class Posts extends Actor{
	private Receiver receiver;
	
	private int lastIndex;
	
	private Hashtable<Integer,String> hashPosts;
	private Hashtable<Integer,String> hashUsersID;
	
	private long workTime;
	
	public Posts(Receiver receiver, int postsNum, long workTime){
		super();
		
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

	@Write
	public void work(){
		long sleepTime = workTime; // convert to nanoseconds
	    long startTime = System.nanoTime();
	    while ((System.nanoTime() - startTime) < sleepTime) {}
	}

	@Write
	public void addPost(String user, String msg) {
		hashPosts.put(++lastIndex, msg);
		hashUsersID.put(lastIndex, user);		
	}

	@Write
	public void readPost(int id, String user) {
		if(id>=lastIndex){
			receiver.sendMessage("User "+user+" requested post "+(lastIndex-1)+" by "+
					hashUsersID.get(lastIndex-1) + ": "+hashPosts.get(lastIndex-1));
		} else {
			receiver.sendMessage("User "+user+" requested post "+id+" by "+
					hashUsersID.get(id) + ": "+hashPosts.get(id));
		}
		
	}
	
}
