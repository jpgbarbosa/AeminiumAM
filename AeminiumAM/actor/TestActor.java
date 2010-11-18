package actor;

public class TestActor extends Actor{
		@writable
		public int val=3;

		public int result;

		public TestActor() {
			super();
		}
		
		@Override
		public void react(Object obj){
			
			result = 42 + ((Integer)obj) + val;
			
			
			ByteCodeOpASM.getWritableFields("react1");
			//Dispatcher.handle(this,"react1",obj);
			
			//Dispatcher.handle(this,"react2",obj);
					
		}
		
		//@VarUsed(varNames = "result")
		@SuppressWarnings("unused")
		@VarUsed(varNames = "result")
		public void react1(Object m){
			result++;
			System.out.println("react1 em execução!");
		}
		
		@SuppressWarnings("unused")
		public void react2(Object m){
			System.out.println("react2 em execução!");
		}
}
