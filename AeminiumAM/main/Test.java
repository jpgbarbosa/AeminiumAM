package main;
import actor.*;

public class Test {
	
	static AeminiumRuntime art = new AeminiumRuntime();

	public static class TestActor extends Actor {

		@readOnly
		int val=3;
		
		int result;

		public TestActor() {
			super();
		}
		
		@Override
		public void react(Object obj){
			
			result = 42 + ((Integer)obj) + val;
			
			/* With this sleep, we are giving time to subActor performs his react*/
			/*
			try {
				Thread.currentThread();
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
		}		

	}

	public static void main(String[] args) {

		
		TestActor a = new TestActor();
		
		a.sendMessage(3);

		art.endAeminiumRuntime();
	
		System.out.println("Testing result: "+a.result);

	}

}
