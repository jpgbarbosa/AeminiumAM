package examples.blogserver.dist.Serial;

import java.util.Random;

import aeminium.runtime.Runtime;

import examples.blogserver.dist.Serial.actors.*;
import unused.AeminiumRuntime;

public class WebSerial {
	public int numCopies=20;

	public static aeminium.runtime.Runtime rt;

	long workTime = 250000;

	Receiver [] receiverArray = new Receiver[numCopies];

	public Add [] addActorArray = new Add[numCopies];

	Posts posts;

	Users [] usersArray = new Users[numCopies];

	public Reader [] readersArray = new Reader[numCopies];

	public WebSerial(int postsNum){
		int i;

		for(i=0;i<numCopies;i++){
			receiverArray[i] = new Receiver();
		}

		for(i=0;i<numCopies;i++){
			usersArray[i] = new Users(addActorArray,workTime, numCopies, rt);
		}

		posts = new Posts(receiverArray,postsNum,workTime, numCopies, rt);

		for(i=0;i<numCopies;i++){
			addActorArray[i] = new Add(usersArray, posts, receiverArray,workTime, numCopies, rt);
		}

		for(i=0;i<numCopies;i++){
			readersArray[i] = new Reader(posts ,workTime, numCopies, rt);
		}

		for(i=0;i<numCopies;i++){
			usersArray[i].addActorArray = new Add[numCopies];
			usersArray[i].setAddActor(addActorArray);
		}
	}

	public static void main(String[] args) {
		WebSerial.rt = aeminium.runtime.implementations.Factory.getRuntime();
		WebSerial.rt.init();

		WebSerial web = new WebSerial(100);
		
		Random ran = new Random(20);

		web.addActorArray[ran.nextInt(web.numCopies)].addMessage("Ace","[Vamos lá por isto a funcionar]");

		web.readersArray[ran.nextInt(web.numCopies)].reqReadPost(6,"USER1");
		
		WebSerial.rt.shutdown();

	}	
}
