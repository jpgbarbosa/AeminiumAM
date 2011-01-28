package examples.blogserver.Serial;

import unused.AeminiumRuntime;
import examples.blogserver.PAM.*;
import examples.blogserver.PAM.actors.Add;
import examples.blogserver.PAM.actors.Posts;
import examples.blogserver.PAM.actors.Reader;
import examples.blogserver.PAM.actors.Receiver;
import examples.blogserver.PAM.actors.Users;

public class WebSerial {

	public static AeminiumRuntime art;
	
	long workTime = 250000;
	
	Receiver receiver;
	public Add adder;
	Posts posts;
	Users users;
	public Reader reader;
	
	public WebSerial(int postsNum){
		receiver = new Receiver();
		users = new Users(adder,workTime);
		posts = new Posts(receiver,postsNum,workTime);
		adder = new Add(users, posts, receiver,workTime);
		reader = new Reader(posts,workTime);
		
		users.setAddActor(adder);
	}
	
	public static void main(String[] args) {
		WebSerial web = new WebSerial(100);
		
		web.adder.addMessage("Ace","[Vamos lá por isto a funcionar]");
		
		web.reader.reqReadPost(6,"USER1");
		
	}
	
}
