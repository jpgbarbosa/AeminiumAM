package unused.examples.dinningPhilosophers;
import unused.annotationsVar.writable;
import actor.Actor;
public class PhilosopherA extends Actor{

	public String name;
	@writable
	static public TableA table;
	
	public PhilosopherA(String name, TableA table2) {
		super();
		this.name = name;
		PhilosopherA.table = table2;
	}
	
	@Override
	protected void react(Object obj) {
		
		Reply msg = (Reply) obj;
		
		if(msg!=null && msg.accepted){
			eat();
			table.sendMessage(new MessageActionA("finished",this));
		} else if((msg!=null && !msg.accepted)){
			think();
			table.sendMessage(new MessageActionA("take", this));
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