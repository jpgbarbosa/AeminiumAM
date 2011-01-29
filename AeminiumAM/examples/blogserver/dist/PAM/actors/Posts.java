package examples.blogserver.dist.PAM.actors;

import java.util.Hashtable;
import java.util.Random;

import examples.blogserver.dist.PAM.Gen;

import actor.Actor;
import actor.annotations.*;
import aeminium.runtime.Runtime;

public class Posts extends Actor{
	Receiver[] receivers;

	int lastIndex;

	Hashtable<Integer,String> hashPosts;
	Hashtable<Integer,String> hashUsersID;

	long workTime;
	int numCopies;

	Random ran;

	public Posts(Receiver[] receiverArray, int postsNum, long workTime, int numCopies, Runtime rt2){
		super();

		this.workTime = workTime;
		this.numCopies = numCopies;
		ran = new Random(10);

		hashPosts = new Hashtable<Integer, String>();
		hashUsersID = new Hashtable<Integer, String>();

		int i;
		Gen g = new Gen(postsNum);
		for( i=0; i<postsNum; i++){
			hashPosts.put(i, g.getSentences()[i]);
			hashUsersID.put(i, "system");
		}

		lastIndex = i--;

		this.receivers = receiverArray;
		
		this. rt = (Runtime) rt2;
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

	@Read
	public void readPost(int id, String user) {
		if(id>=lastIndex){
			receivers[ran.nextInt(numCopies)].sendMessage("User "+user+" requested post "+(lastIndex-1)+" by "+
					hashUsersID.get(lastIndex-1) + ": "+hashPosts.get(lastIndex-1));
		} else {
			receivers[ran.nextInt(numCopies)].sendMessage("User "+user+" requested post "+id+" by "+
					hashUsersID.get(id) + ": "+hashPosts.get(id));
		}		
	}
	
}
