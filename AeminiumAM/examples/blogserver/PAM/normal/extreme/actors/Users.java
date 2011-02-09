package examples.blogserver.PAM.normal.extreme.actors;

import java.util.ArrayList;

import examples.blogserver.PAM.normal.real.AskPermission;
import examples.blogserver.PAM.normal.real.PermissionResponse;

import actor.Actor;
public class Users extends Actor{
	
	int numNames = 100;
	long workTime = 0;
	
	ArrayList<String> users;
	
	boolean useSpin;
	
	public Add addActor;
	
	public Users(Add addActor, long workTime, boolean useSpin){
		this.useSpin = useSpin;
		this.addActor = addActor;
		this.workTime = workTime;
		
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
				if(addActor==null){
					System.out.println("addActor is null");
				}
				addActor.sendMessage(new PermissionResponse(((AskPermission)obj).req, true));
				
			} else {
				addActor.sendMessage(new PermissionResponse(((AskPermission)obj).req, false));
			}
		}
	}
	
	private void work(){
		long sleepTime = workTime; // convert to nanoseconds
	    long startTime = System.nanoTime();
	    while ((System.nanoTime() - startTime) < sleepTime) {}
	}

}
