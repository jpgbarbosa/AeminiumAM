package main;
import actor.*;

public class Test {
	
	static AeminiumRuntime art = new AeminiumRuntime();
	
	public static class SubR extends MReact {
		@readOnly
		public int x = 3;
		
		public SubR() {
			super();
		}
		
		@Override
		public void react(Object obj){
			x*=3;
		}
		
	}

	public static class TestActor extends Actor {
	
		public int result;

		public TestActor() {
			super();
		}
		
		@Override
		public void react(Object obj){
			
			SubR subActor = new SubR();
			subActor.sendToAR();
			
			result = 42;
			
			/* With this sleep, we are giving time to subActor performs his react*/
			try {
				Thread.currentThread();
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Testing subActor: "+subActor.x);
		}		

	}

	public static void main(String[] args) {

		
		TestActor a = new TestActor();
		
		a.sendMessage(null);

		art.endAeminiumRuntime();
	
		System.out.println("Testing result: "+a.result);

	}

}
