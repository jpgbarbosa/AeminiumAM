package actor;

public class TestActor extends Actor{
		@writable
		public int val=3;
		
		String [] array = new String[5];

		public int result=0;

		public TestActor() {
			super();
		}
		
		@Override
		public void react(Object obj){
			
			result = 42 + ((Integer)obj) + val;			
			
			Dispatcher.handle(this,"react1",obj);
			
			Dispatcher.handle(this,"react2",obj);
					
		}
		
		//@VarUsed(varNames = "result")
		@SuppressWarnings("unused")
		public void react1(Object m){
			result=result+2;
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("react1 em execução!");
		}
		
		@SuppressWarnings("unused")
		public void react2(Object m){
			System.out.println("react2 em execução!: result="+result);
		}
}
