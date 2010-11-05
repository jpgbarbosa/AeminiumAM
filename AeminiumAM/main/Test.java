package main;
import actor.*;

public class Test {
	
	static AeminiumRuntime art = new AeminiumRuntime();

	public static class TestActor extends Actor {
	
		@readOnly
		public int result;

		public TestActor() {
			super();
		}
		
		@Override
		public void react(Object obj){
			result = 42;
		}		

	}

	public static void main(String[] args) {

		
		TestActor a = new TestActor();
		
		a.sendMessage(null);

		art.endAeminiumRuntime();
	
		System.out.println("Testing result: "+a.result);

	}

}
