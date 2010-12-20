package examples.blogserver.actors;

import actor.Actor;
import annotations.writable;

public class Receiver extends Actor{

	@writable
	int x;
	
	@Override
	protected void react(Object obj) {
		//System.out.println(obj);
	}

}
