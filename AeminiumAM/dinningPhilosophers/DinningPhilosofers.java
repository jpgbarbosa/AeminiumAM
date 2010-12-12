package dinningPhilosophers;

import actor.AeminiumRuntime;

public class DinningPhilosofers {
	
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
			arrayPhilosopher[i].sendMessage(new Reply(false));
		}
		
		art.endAeminiumRuntime();
	}
}
