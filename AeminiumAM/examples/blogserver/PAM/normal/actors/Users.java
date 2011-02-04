package examples.blogserver.PAM.normal.actors;

import java.util.ArrayList;

import examples.blogserver.PAM.dist.AskPermission;
import examples.blogserver.PAM.dist.PermissionResponse;

import actor.Actor;
public class Users extends Actor{
	
	int x;
	int numNames = 100;
	long workTime = 0;
	
	ArrayList<String> users;
	
	public Add addActor;
	
	public Users(Add addActor, long workTime){
		this.addActor = addActor;
		this.workTime = workTime;
		
		users = new ArrayList<String>();
		
		for(int i=0; i<500; i++){
			users.add("auto-gen user");
		}
		/*
		try {
			
			FileInputStream fstream = new FileInputStream("Names.txt");
		    DataInputStream in = new DataInputStream(fstream);
		    BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			int index=0;
			String word;
			
			while (index<numNames && (word = br.readLine()) != null){
				users.add(word);
			}
			
			in.close();
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	*/
		users.add("Ace");
		
	}
	
	@Override
	protected void react(Object obj) {
		if( obj instanceof AskPermission){
			work();
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
