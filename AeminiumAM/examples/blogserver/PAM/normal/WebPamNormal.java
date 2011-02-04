package examples.blogserver.PAM.normal;

import actor.AeminiumRuntime;

import examples.blogserver.PAM.normal.actors.*;

public class WebPamNormal {

	public static AeminiumRuntime art;
	boolean useSpin = false;
	long workTime = 250000;
	
	Receiver receiver;
	public Add adder;
	Posts posts;
	Users users;
	public Reader reader;
	
	public WebPamNormal(int postsNum, boolean useSpin){
		this.useSpin = useSpin;
		receiver = new Receiver();
		users = new Users(adder,workTime,useSpin);
		posts = new Posts(receiver,postsNum,workTime,useSpin);
		adder = new Add(users, posts, receiver,workTime,useSpin);
		reader = new Reader(posts,workTime,useSpin);
		
		users.addActor = adder;
	}
	
	public static void main(String[] args) {
		art = new AeminiumRuntime();
		
		WebPamNormal web = new WebPamNormal(100,false);
		
		web.adder.sendMessage(new PutRequest("Ace","[Vamos lá por isto a funcionar]"));
		
		web.reader.sendMessage(new ReadPost(6,"USER1"));
		art.endAeminiumRuntime();
		
	}
	
}
