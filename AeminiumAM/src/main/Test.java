package main;

import aeminium.runtime.Runtime;
import aeminium.runtime.examples.fjtests.AeminiumFibonacci;
import aeminium.runtime.examples.fjtests.AeminiumFibonacci.FibBody;
import aeminium.runtime.implementations.Factory;

import Actor.*;

public class Test {
	public static FibBody body;

	public static class TestActor extends Actor {
		int result;

		public TestActor(Runtime rt) {
			super(rt);
		}
		
		@Override
		public void react(){
			System.out.println("Cenas");
			result = 42;
		}		

	}

	public static void main(String[] args) {

		Runtime rt = Factory.getRuntime();
		rt.init();
		TestActor a = new TestActor(rt);
		a.sendMessage();

		rt.shutdown();
	
		System.out.println("ola: "+a.result);

	}

}
