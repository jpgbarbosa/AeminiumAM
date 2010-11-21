package main;

import actor.Actor;
import actor.AeminiumRuntime;
import actor.Dispatcher;

public class Example {
	private static class State{
		boolean accepted;
		
		State(boolean accepted){
			this.accepted = accepted;
		}
	}
	
	private static class MessageAction{
		String msg;
		Philosopher owner;
		
		MessageAction(String msg, Philosopher owner){
			this.msg = msg;
			this.owner = owner;
		}
		
	}
	
	private static class Fork extends Actor{
	    static public String name;
	    static public boolean available = true;	    
	    
	    Fork(String name){
	    	this.name = name;
	    }
	    
		@Override
		protected void react(Object obj) {
			MessageAction msg = (MessageAction) obj;
			if(msg.msg.equals("take")){
				if (available) {
				    available = false;
				    msg.owner.sendMessage(new State(true));
				} else{ 
					msg.owner.sendMessage(new State(false));
				}
			} else if(msg.equals("finished")){
				available = true;
			} else {
				throw new IllegalStateException("Cannot process the message: "+msg);
			}
		}
	}
	
	private static class Philosopher extends Actor{

		static public String name;
		static public Fork [] array;
		
		public Philosopher(String name, Fork fork1, Fork fork2) {
			super();
			this.name = name;
			array = new Fork[2];
			array[0] = fork1;
			array[1] = fork2;
		}
		
		@Override
		protected void react(Object obj) {
			MessageAction msg = (MessageAction) obj;
			
			Dispatcher.handle(this, "think", obj);
			array[0].sendMessage(new MessageAction("take", this));
			array[1].sendMessage(new MessageAction("take", this));
			
			if(array[0].available == false || array[1].available == false){
				System.out.println(name+"Oops, can't get my forks! Giving up.");
				if(array[0].available == false){
					array[0].sendMessage(new MessageAction("finished", this));
				}
				if(array[1].available == false){
					array[1].sendMessage(new MessageAction("finished", this));
				}
			} else {
				Dispatcher.handle(this, "eat", obj);
				msg.owner.sendMessage(new MessageAction("finished", this));
			}
			
		}
		
	    public void think() {
	        System.out.println(name+"I'm thinking");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(name+"I'm done thinking");
	    }

	    public void eat() {
	    	System.out.println(name+"I'm EATING");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(name+"I'm done EATING");
	    }
	}
	
	static AeminiumRuntime art = new AeminiumRuntime();

	public static void main(String[] args) {
		
		Fork [] arrayFork = new Fork[5];
		Philosopher [] arrayPhilosopher = new Philosopher[5];
		
		arrayFork[0] = new Fork("Fork 1");
		arrayFork[1] = new Fork("Fork 2");
		arrayFork[2] = new Fork("Fork 3");
		arrayFork[3] = new Fork("Fork 4");
		arrayFork[4] = new Fork("Fork 5");
		
		arrayPhilosopher[0] = new Philosopher("Joe", arrayFork[0], arrayFork[1]);
		arrayPhilosopher[1] = new Philosopher("Dave", arrayFork[1], arrayFork[2]);
		arrayPhilosopher[2] = new Philosopher("Alice", arrayFork[2], arrayFork[3]);
		arrayPhilosopher[3] = new Philosopher("James", arrayFork[3], arrayFork[4]);
		arrayPhilosopher[4] = new Philosopher("Phil", arrayFork[4], arrayFork[0]);
		
		art.endAeminiumRuntime();

	}
}