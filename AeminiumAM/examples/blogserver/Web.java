package examples.blogserver;

import actor.AeminiumRuntime;
import examples.blogserver.actors.*;

public class Web {

	public static AeminiumRuntime art;
	
	long workTime = 250000;
	
	Receiver receiver;
	public Add adder;
	Posts posts;
	Users users;
	public Reader reader;
	
	public Web(int postsNum){
		receiver = new Receiver();
		users = new Users(adder,workTime);
		posts = new Posts(receiver,postsNum,workTime);
		adder = new Add(users, posts, receiver,workTime);
		reader = new Reader(posts,workTime);
		
		users.addActor = adder;
	}
	
	public static void main(String[] args) {
		art = new AeminiumRuntime();
		
		Web web = new Web(100);
		
		web.adder.sendMessage(new PutRequest("Ace","[Vamos lá por isto a funcionar]"));
		
		web.reader.sendMessage(new ReadPost(6,"USER1"));
		art.endAeminiumRuntime();
		
	}
	
}
