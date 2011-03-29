package aeminium.actormodel.examples.dictionary;

import aeminium.actormodel.actor.Actor;
import aeminium.actormodel.annotations.Read;
import aeminium.actormodel.annotations.Write;

public class BenchmarkActor extends Actor {
	
	protected final static int expectedMessages = 1000;
	private final static BenchmarkBackend backend = new BenchmarkBackend(23);
	
	@Write
	public void setValue(int key, int value) {
		backend.setValue(key, value);
	}
	
	@Read
	public void getValue(int key) {
		backend.getValue(key);
	}
	
	public static void main(String[] args) {
		BenchmarkActor ba = new BenchmarkActor();
		
		for (int i = 0; i < expectedMessages; i++) {
			if (i % 5 == 0) {
				ba.setValue(i, 10);
			} else {
				ba.getValue(i);
			}
			
		}
		
	}
	
}
