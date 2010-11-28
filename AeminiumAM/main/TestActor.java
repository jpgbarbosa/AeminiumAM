package main;

import actor.Actor;
import actor.Dispatcher;
import annotations.VarType;
import annotations.writable;

public class TestActor extends Actor{
		@writable
		static public int val=3;
		
		@writable
		static public int result;

		public TestActor() {
			super();
		}
		
		@Override
		protected void react(Object obj){
			
			result = 42 + ((Integer)obj) + val;			
			
			Dispatcher.handle(this,"react1",obj);
			
			Dispatcher.handle(this,"react2",obj);
					
		}
		
		@SuppressWarnings("unused")
		@VarType(isReadOnly="val", isWritable="result val")
		private void react1(Object m){
			result=result+2;

			react2(null);
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			System.out.println("react1 em execução!");
		}
		
		@SuppressWarnings("unused")
		@VarType(isReadOnly="val")
		private void react2(Object m){
			System.out.println("react2 em execução! val="+val);
		}
}
