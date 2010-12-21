package examples.blogserver.actors;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import examples.blogserver.AskPermission;
import examples.blogserver.PermissionResponse;
import actor.Actor;
import annotations.writable;

public class Users extends Actor{

	int x;
	int numNames = 100;

	ArrayList<String> users;
	
	public Add addActor;
	
	public Users(Add addActor){
		this.addActor = addActor;
		
		users = new ArrayList<String>();
		
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
		}		
		
	}
	
	@Override
	protected void react(Object obj) {
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

}
