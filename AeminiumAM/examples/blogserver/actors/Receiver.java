package examples.blogserver.actors;

import actor.Actor;

public class Receiver extends Actor{

	@Override
	protected void react(Object obj) {
		System.out.println(obj);
	}

}
