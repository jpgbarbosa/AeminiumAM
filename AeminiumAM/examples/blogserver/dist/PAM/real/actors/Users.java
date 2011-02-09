package examples.blogserver.dist.PAM.real.actors;

import java.util.ArrayList;
import java.util.Random;

import examples.blogserver.dist.PAM.real.actors.Add;

import actor.Actor;
import actor.annotations.Read;
import actor.annotations.Write;
import aeminium.runtime.Runtime;

public class Users extends Actor{
	
	int numNames = 100;
	long workTime = 0;
	ArrayList<String> users;
	public Add[] addActorArray;
	boolean useSpin = false;

	int numCopies;

	Random ran;

	public Users(Add[] addActorArray, long workTime, int numCopies, Runtime rt2, boolean useSpin){
		this.useSpin = useSpin;
		
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
	
	@Read
	public void work(){
		long sleepTime = workTime; // convert to nanoseconds
	    long startTime = System.nanoTime();
	    while ((System.nanoTime() - startTime) < sleepTime) {}
	}

	@Read
	public void requestPermission(String user, String msg) {
		if(useSpin){
			long sleepTime = workTime; // convert to nanoseconds
		    long startTime = System.nanoTime();
		    while ((System.nanoTime() - startTime) < sleepTime) {}
		}
		if(users.contains(user)){
			if(getAddActor()==null){
				System.out.println("addActor is null");
			}
			getAddActor()[ran.nextInt(numCopies)].confirmReq(user,msg, true);
		} else {
			getAddActor()[ran.nextInt(numCopies)].confirmReq(user,msg, false);
		}			
	}
	
	@Read
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
