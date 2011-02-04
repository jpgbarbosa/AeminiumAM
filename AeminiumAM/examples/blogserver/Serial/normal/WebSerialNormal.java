package examples.blogserver.Serial.normal;

import actor.AeminiumRuntime;
import examples.blogserver.Serial.normal.actors.*;

public class WebSerialNormal {
	boolean useSpin = false;
	public static AeminiumRuntime art;
	
	long workTime = 250000;
	
	Receiver receiver;
	public Add adder;
	Posts posts;
	Users users;
	public Reader reader;
	
	public WebSerialNormal(int postsNum, boolean useSpin){
		this.useSpin = useSpin;
		receiver = new Receiver();
		users = new Users(adder, workTime, useSpin);
		posts = new Posts(receiver, postsNum, workTime, useSpin);
		adder = new Add(users, posts, receiver, workTime, useSpin);
		reader = new Reader(posts, workTime, useSpin);
		
		users.addActor = adder;
	}
	
	public static void main(String[] args) {
		art = new AeminiumRuntime();
		
		WebSerialNormal web = new WebSerialNormal(100,false);
		
		web.adder.sendMessage(new PutRequest("Ace","[Vamos lá por isto a funcionar]"));
		
		web.reader.sendMessage(new ReadPost(6,"USER1"));
		art.endAeminiumRuntime();
		
	}
	
}
