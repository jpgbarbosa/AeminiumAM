package examples.dinningPhilosophers;


public class MessageAction{
	String msg;
	Philosopher owner;
	
	MessageAction(String msg, Philosopher owner){
		this.msg = msg;
		this.owner = owner;
	}
}
