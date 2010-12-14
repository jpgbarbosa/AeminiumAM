package dinningPhilosophers;
import actor.Actor;
import annotations.writable;
class PhilosopherA extends Actor{

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
//        System.out.println(name+" I'm thinking");
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(name+" I'm done thinking");
    }

    public void eat() {
  //  	System.out.println(name+" I'm EATING");
		try {
			Thread.sleep(2);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	//	System.out.println(name+" I'm done EATING");
    }
}