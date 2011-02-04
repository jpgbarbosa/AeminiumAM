package examples.blogserver.normal.Serial;

import examples.blogserver.normal.Serial.actors.*;

public class WebSerial {

	public int numCopies=20;
	
	boolean useSpin = false;


	public static aeminium.runtime.Runtime rt;
	
	long workTime = 250000;
	
	Receiver receiver;
	public Add adder;
	Posts posts;
	Users users;
	public Reader reader;
	
	public WebSerial(int postsNum){
		receiver = new Receiver(useSpin);
		users = new Users(adder,workTime,rt,useSpin);
		posts = new Posts(receiver,postsNum,workTime,rt,useSpin);
		adder = new Add(users, posts, receiver,workTime,rt,useSpin);
		reader = new Reader(posts,workTime,rt,useSpin);
		
		users.setAddActor(adder);
	}
	
	public static void main(String[] args) {
		WebSerial.rt = aeminium.runtime.implementations.Factory.getRuntime();
		WebSerial.rt.init();
		
		WebSerial web = new WebSerial(100);
		
		web.adder.addMessage("Ace","[Vamos lá por isto a funcionar]");
		
		web.reader.reqReadPost(6,"USER1");
		
		WebSerial.rt.shutdown();
		
	}
	
}
