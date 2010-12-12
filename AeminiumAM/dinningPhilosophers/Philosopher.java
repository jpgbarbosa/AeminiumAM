package dinningPhilosophers;
import actor.Actor;

class Philosopher extends Actor{

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
        System.out.println(name+" I'm thinking");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(name+" I'm done thinking");
    }

    public void eat() {
    	System.out.println(name+" I'm EATING");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(name+" I'm done EATING");
    }
}