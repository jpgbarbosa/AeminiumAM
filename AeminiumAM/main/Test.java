package main;

import aeminium.runtime.Runtime;
import aeminium.runtime.implementations.Factory;

import actor.*;

public class Test {

	public static class TestActor extends Actor {
		int result;

		public TestActor(Runtime rt) {
			super(rt);
		}
		
		@Override
		public void react(){
			System.out.println("React");
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
