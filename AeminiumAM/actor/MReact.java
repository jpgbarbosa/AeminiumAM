package actor;

import aeminium.runtime.Body;
import aeminium.runtime.DataGroup;
import aeminium.runtime.Runtime;
import aeminium.runtime.Task;

public abstract class MReact {
	
	public MReact(){};

	public abstract void react(Object obj);
	
	public void sendToAR() {

		if(canBeParallelized()){
			System.out.println("going to be par");
			
			Task t1 = AeminiumRuntime.rt.createNonBlockingTask(new Body(){	
				@Override
				public void execute(Runtime rt, Task current)
						throws Exception {
					
					react(null);
					
				}}, Runtime.NO_HINTS);

			AeminiumRuntime.rt.schedule(t1, Runtime.NO_PARENT, Runtime.NO_DEPS);
			
		} else{
			System.out.println("is going to be an Atomic task");
			
			/* Useless Datagroup created to pass as arg in createAtomicTask */
			DataGroup dg = AeminiumRuntime.rt.createDataGroup();
			
			Task t1 = AeminiumRuntime.rt.createAtomicTask(new Body(){		
				@Override
				public void execute(Runtime rt, Task current)
						throws Exception {
					
					react(null);
					
				}},dg, Runtime.NO_HINTS);
			
			AeminiumRuntime.rt.schedule(t1, Runtime.NO_PARENT, Runtime.NO_DEPS);
		}
	}

	private boolean canBeParallelized() {
		// TODO Auto-generated method stub
		return false;
	}
}
