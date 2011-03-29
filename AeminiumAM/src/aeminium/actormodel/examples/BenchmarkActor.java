package aeminium.actormodel.examples;

import java.util.HashMap;

import aeminium.actormodel.actor.Actor;
import aeminium.actormodel.annotations.Read;
import aeminium.actormodel.annotations.Write;

public class BenchmarkActor extends Actor {
	protected final static int fibn = 30;
	protected final static int expectedMessages = 1000;
	
	private HashMap<Integer, Integer> dictionary = new HashMap<Integer, Integer>();
	
	public BenchmarkActor() {
		for (int i = 0; i< expectedMessages; i++) {
			dictionary.put(i, i);
		}
	}
	
	@Write
	public void setValue(int key, int value) {
		fib(fibn);
		dictionary.put(key, value);
		// System.out.println(key + " -> set!");
	}
	
	@Read
	public void getValue(int key) {
		if (key % 3 == 0) {
			fib(fibn);
		}
		dictionary.get(key);
		// System.out.println(key + " -> get.");
	}
	
	@Read
	private int fib(int n) {
		if (n <= 2) return n;
		else return fib(n-1) + fib(n-2);
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
