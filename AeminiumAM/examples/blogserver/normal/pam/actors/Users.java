package examples.blogserver.normal.pam.actors;

import java.util.ArrayList;

import actor.Actor;
import actor.annotations.Read;
import actor.annotations.Write;

public class Users extends Actor{
	
	int x;
	int numNames = 100;
	long workTime = 0;
	ArrayList<String> users;
	
	private Add addActor;
	
	public Users(Add addActor, long workTime){
		this.setAddActor(addActor);
		this.workTime = workTime;
		
		users = new ArrayList<String>();
		
		for(int i=0; i<500; i++){
			users.add("auto-gen user");
		}
		users.add("Ace");
		
	}
	
	@Write
	public void work(){
		long sleepTime = workTime; // convert to nanoseconds
	    long startTime = System.nanoTime();
	    while ((System.nanoTime() - startTime) < sleepTime) {}
	}

	@Read
	public void requestPermission(String user, String msg) {
		if(users.contains(user)){
			if(getAddActor()==null){
				System.out.println("addActor is null");
			}
			getAddActor().confirmReq(user,msg, true);
		} else {
			getAddActor().confirmReq(user,msg, false);
		}		
	}
	
	@Read
	private Add getAddActor(){
		return addActor;
	}

	@Write
	public void setAddActor(Add addActor) {
		this.addActor = addActor;
	}

}
