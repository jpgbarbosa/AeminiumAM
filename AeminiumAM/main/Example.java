package main;

import actor.Actor;
import actor.AeminiumRuntime;
import actor.annotations.Read;
import actor.annotations.Write;
public class Example {
	
	private static class MessageAction{
		String msg;
		Philosopher owner;
		
		MessageAction(String msg, Philosopher owner){
			this.msg = msg;
			this.owner = owner;
		}
	}
	
	private static class Reply{
		boolean accepted;
		
		Reply(boolean accepted){
			this.accepted = accepted;
		}
	}
	
	private static class Table extends Actor{
		Fork [] forks;
		Philosopher [] phils;
		
		Table(Philosopher [] phils,Fork [] forks){
			super();
			this.phils = phils;
			this.forks = forks;
		}
		
		@Override
		@Write
		public void react(Object obj) {
			try{
			MessageAction msgA = (MessageAction)obj;
			if(msgA.msg.equals("take")){
				for(int i=1; i< phils.length+1; i++){
					if(msgA.owner==phils[i%forks.length]){
						if(forks[i%forks.length].checkAvailability() && forks[(i-1)].checkAvailability()){
							forks[i%forks.length].setAvailability(false);
							forks[(i-1)%forks.length].setAvailability(false);
							msgA.owner.sendMessage(new Reply(true));
						} else {
							msgA.owner.sendMessage(new Reply(false));
						}
						break;
					}
				}
			} else if(msgA.equals("finished")){
				for(int i=1; i< phils.length+1; i++){
					if(msgA.owner==phils[i]){
						forks[i%forks.length].setAvailability(true);
						forks[(i-1)].setAvailability(true);
					}
				}
			}
			}catch (Exception e){
				System.out.println(e);
			}
		}
		
	}
	
	private static class Fork{
		
	    public String name;
	    
	    public boolean available = true;	    
	    
	    Fork(String name){
	    	this.name = name;
	    }
	    
	    public String getName(){
	    	return name;
	    }
	    
	    synchronized public boolean checkAvailability(){
	    	return available;
	    }
	    synchronized public void setAvailability(boolean available){
	    	this.available = available;
	    }
	}
	
	private static class Philosopher extends Actor{

		static public String name;
		static public Table table;
		
		public Philosopher(String name, Table table) {
			super();
			this.name = name;
			this.table = table;
		}
		
		@Override
		@Read
		public void react(Object obj) {
			
			Reply msg = (Reply) obj;
			
			if(msg!=null && msg.accepted){
				eat();
				table.sendMessage(new MessageAction("finished",this));
			} else if(msg==null || (msg!=null && !msg.accepted)){
				think();
				table.sendMessage(new MessageAction("take", this));
			}
		}
		
		@Read
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

		@Read
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

	
	static AeminiumRuntime art = new AeminiumRuntime();

	public static void main(String[] args) {
		
		Fork [] arrayFork = new Fork[5];
		Philosopher [] arrayPhilosopher = new Philosopher[5];
		
		Table table = new Table(arrayPhilosopher,arrayFork);
		
		arrayFork[0] = new Fork("Fork 1");
		arrayFork[1] = new Fork("Fork 2");
		arrayFork[2] = new Fork("Fork 3");
		arrayFork[3] = new Fork("Fork 4");
		arrayFork[4] = new Fork("Fork 5");
		
		arrayPhilosopher[0] = new Philosopher("Phil",table);
		arrayPhilosopher[1] = new Philosopher("Dave", table);
		arrayPhilosopher[2] = new Philosopher("Alice", table);
		arrayPhilosopher[3] = new Philosopher("James", table);
		arrayPhilosopher[4] = new Philosopher("Joe", table);
		
		for(int i=0; i<5; i++){
			arrayPhilosopher[i].sendMessage(null);
		}
		
		art.endAeminiumRuntime();
	}
}