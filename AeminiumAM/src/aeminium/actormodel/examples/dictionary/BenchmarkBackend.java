package aeminium.actormodel.examples.dictionary;

import java.util.HashMap;

public class BenchmarkBackend {
	protected final static int fibn = 30;
	protected boolean debug = false;
	
	private HashMap<Integer, Integer> dictionary = new HashMap<Integer, Integer>();
	
	public BenchmarkBackend(int n) {
		for (int i = 0; i < n; i++) {
			dictionary.put(i, i);
		}
	}
	
	public void setValue(int key, int value) {
		fib(fibn);
		dictionary.put(key, value);
		if (debug) System.out.println(key + " -> set!");
	}
	
	public void getValue(int key) {
		if (key % 3 == 0) {
			fib(fibn);
		}
		dictionary.get(key);
		if (debug) System.out.println(key + " -> get.");
	}
	
	private int fib(int n) {
		if (n <= 2) return n;
		else return fib(n-1) + fib(n-2);
	}
}
