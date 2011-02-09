package examples.blogserver.dist.PAM.real;

import java.util.Random;

import examples.blogserver.dist.PAM.real.actors.*;

public class WebPam {
	public int numCopies=3;
	public boolean useSpin = false;

	public static aeminium.runtime.Runtime rt;

	long workTime = 5000000;

	Receiver [] receiverArray = new Receiver[numCopies];

	public Add [] addActorArray = new Add[numCopies];

	Posts posts;

	Users [] usersArray = new Users[numCopies];

	public Reader [] readersArray = new Reader[numCopies];

	public WebPam(int numCopies,int workTime ,int postsNum, boolean useSpin){
		this.useSpin = useSpin;
		this.numCopies = numCopies;
		this.workTime = workTime;
		int i;

		for(i=0;i<numCopies;i++){
			receiverArray[i] = new Receiver(useSpin);
		}

		for(i=0;i<numCopies;i++){
			usersArray[i] = new Users(addActorArray,workTime, numCopies, rt, useSpin);
		}

		posts = new Posts(receiverArray,postsNum,workTime, numCopies, rt, useSpin);

		for(i=0;i<numCopies;i++){
			addActorArray[i] = new Add(usersArray, posts, receiverArray,workTime, numCopies, rt, useSpin);
		}

		for(i=0;i<numCopies;i++){
			readersArray[i] = new Reader(posts ,workTime, numCopies, rt, useSpin);
		}

		for(i=0;i<numCopies;i++){
			usersArray[i].addActorArray = new Add[numCopies];
			usersArray[i].setAddActor(addActorArray);
		}
	}
	
	public static void main(String[] args) {
		WebPam.rt = aeminium.runtime.implementations.Factory.getRuntime();
		WebPam.rt.init();

		WebPam web = new WebPam(3,100,250000, false);
		
		Random ran = new Random(20);

		web.addActorArray[ran.nextInt(web.numCopies)].addMessage("Ace","[Vamos lá por isto a funcionar]");

		web.readersArray[ran.nextInt(web.numCopies)].reqReadPost(6,"USER1");
		
		WebPam.rt.shutdown();
		
	}
	
}
