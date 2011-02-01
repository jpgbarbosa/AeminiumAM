package unused.examples.dinningPhilosophers;
import actor.Actor;
public class Philosopher extends Actor{

	public String name;
	static public Table table;
	
	public Philosopher(String name, Table table) {
		super();
		this.name = name;
		Philosopher.table = table;
	}
	
	@Override
	protected void react(Object obj) {
		
		Reply msg = (Reply) obj;
		
		if(msg!=null && msg.accepted){
			eat();
			table.sendMessage(new MessageAction("finished",this));
		} else if((msg!=null && !msg.accepted)){
			think();
			table.sendMessage(new MessageAction("take", this));
		}
	}
	
    public void think() {
		long sleepTime = 250000L; // convert to nanoseconds
	    long startTime = System.nanoTime();
	    while ((System.nanoTime() - startTime) < sleepTime) {}
    }

    public void eat() {
		long sleepTime = 500*1000L; // convert to nanoseconds
	    long startTime = System.nanoTime();
	    while ((System.nanoTime() - startTime) < sleepTime) {}
    }
}