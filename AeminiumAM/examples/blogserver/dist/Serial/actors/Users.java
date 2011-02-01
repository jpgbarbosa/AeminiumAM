package examples.blogserver.dist.Serial.actors;

import java.util.ArrayList;
import java.util.Random;

import unused.AeminiumRuntime;
import unused.annotationsVar.writable;

import actor.Actor;
import actor.annotations.Read;
import actor.annotations.Write;
import aeminium.runtime.Runtime;

public class Users extends Actor{

	int x;
	int numNames = 100;
	long workTime = 0;
	ArrayList<String> users;
	public Add[] addActorArray;

	int numCopies;

	Random ran;

	public Users(Add[] addActorArray, long workTime, int numCopies, Runtime rt2){
		this.addActorArray = addActorArray;
		this.workTime = workTime;
		this.numCopies = numCopies;
		ran = new Random(7);

		users = new ArrayList<String>();

		for(int i=0; i<500; i++){
			users.add("auto-gen user");
		}
		users.add("Ace");
		
		this.rt = (Runtime) rt2;

	}
	
	@Write
	public void work(){
		long sleepTime = workTime; // convert to nanoseconds
	    long startTime = System.nanoTime();
	    while ((System.nanoTime() - startTime) < sleepTime) {}
	}

	@Write
	public void requestPermission(String user, String msg) {
		if(users.contains(user)){
			if(getAddActor()==null){
				System.out.println("addActor is null");
			}
			getAddActor()[ran.nextInt(numCopies)].confirmReq(user,msg, true);
		} else {
			getAddActor()[ran.nextInt(numCopies)].confirmReq(user,msg, false);
		}		
	}
	
	@Write
	public Add [] getAddActor(){
		return addActorArray;
	}

	@Write
	public void setAddActor(Add[] add) {
		this.addActorArray = add;
	}

	@Write
	public void initAddActor(int numCopies2) {
		this.addActorArray = new Add[numCopies2];
	}

}
