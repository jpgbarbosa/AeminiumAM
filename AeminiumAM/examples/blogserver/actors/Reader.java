package examples.blogserver.actors;

import actor.Actor;

public class Reader extends Actor{

	Posts posts;
	
	public Reader(Posts posts){
		this.posts = posts;
	}
	
	@Override
	protected void react(Object obj) {
		posts.sendMessage(obj);
	}

}
