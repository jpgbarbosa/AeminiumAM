package examples.blogserver.Serial.dist.actors;

import java.util.ArrayList;
import java.util.Random;

import examples.blogserver.Serial.dist.AskPermission;
import examples.blogserver.Serial.dist.PermissionResponse;

import actor.Actor;
import annotations.writable;

public class Users extends Actor{
	
	int numNames = 100;
	long workTime = 0;
	@writable
	ArrayList<String> users;
	@writable
	public Add[] addActorArray;
	
	boolean useSpin;
	
	int numCopies;
	
	Random ran;
	
	public Users(Add[] addActorArray, long workTime, int numCopies, boolean useSpin){
		super();
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
		
	}
	
	@Override
	protected void react(Object obj) {
		if(useSpin){
			work();
		}
		if( obj instanceof AskPermission){
			if(users.contains(((AskPermission)obj).req.user)){
				if(addActorArray==null){
					System.out.println("addActor is null");
				}
				addActorArray[ran.nextInt(numCopies)].sendMessage(new PermissionResponse(((AskPermission)obj).req, true));
				
			} else {
				addActorArray[ran.nextInt(numCopies)].sendMessage(new PermissionResponse(((AskPermission)obj).req, false));
			}
		}
	}
	
	private void work(){
		long sleepTime = workTime; // convert to nanoseconds
	    long startTime = System.nanoTime();
	    while ((System.nanoTime() - startTime) < sleepTime) {}
	}

}
