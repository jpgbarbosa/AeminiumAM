package dinningPhilosophers;


public class MessageActionA{
	String msg;
	PhilosopherA owner;
	
	MessageActionA(String msg, PhilosopherA owner){
		this.msg = msg;
		this.owner = owner;
	}
}
