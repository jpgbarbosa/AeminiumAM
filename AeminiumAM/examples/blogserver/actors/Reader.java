package examples.blogserver.actors;

import actor.Actor;
import annotations.writable;

public class Reader extends Actor{

	Posts posts;
	
	@writable
	int x;
	
	public Reader(Posts posts){
		this.posts = posts;
	}
	
	@Override
	protected void react(Object obj) {
		posts.sendMessage(obj);
	}

}
