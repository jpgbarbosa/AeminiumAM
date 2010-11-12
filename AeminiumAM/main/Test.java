package main;
import actor.*;

public class Test {
	
	static AeminiumRuntime art = new AeminiumRuntime();

	public static class TestActor extends Actor {
		@writable
		public int val=3;


		public int result;

		public TestActor() {
			super();
		}
		
		@Override
		public void react(Object obj){
			
			result = 42 + ((Integer)obj) + val;
			
			Dispatcher.handle(this,"react1",obj);
			
			//Dispatcher.handle(this,"react2",obj);
						
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
		
		@SuppressWarnings("unused")
		@VarUsed(varNames = "result")
		private void react1(Object m){
			System.out.println("react1 em execução!");
		}
		
		@SuppressWarnings("unused")
		private void react2(Object m){
			System.out.println("react2 em execução!");
		}

	}

	public static void main(String[] args) {

		
		TestActor a = new TestActor();
		
		a.sendMessage(3);

		art.endAeminiumRuntime();
	
		System.out.println("Testing result: "+a.result);

	}

}
