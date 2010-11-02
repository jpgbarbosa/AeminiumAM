package main;
import actor.*;

public class Test {
	
	static AeminiumRuntime art = new AeminiumRuntime();

	public static class TestActor extends Actor {
		
		@readOnly(isReadOnly = true)
		private int result;

		public TestActor() {
			super();
		}
		
		@Override
		public void react(Object obj){
			System.out.println("React");
			result = 42;
		}		

	}

	public static void main(String[] args) {

		
		TestActor a = new TestActor();
		
		a.sendMessage(null);

		art.endAeminiumRuntime();
	
		System.out.println("ola: "+a.result);

	}

}
