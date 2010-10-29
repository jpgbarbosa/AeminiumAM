package actor;

import aeminium.runtime.Runtime;
import aeminium.runtime.Task;
import aeminium.runtime.*;

import actor.AeminiumRuntime;


public abstract class Actor{
	public Actor() {}

	public abstract void react(Object obj);

	public void sendMessage(Object obj) {

		Task t1 = AeminiumRuntime.rt.createNonBlockingTask(new Body(){		
				@Override
				public void execute(Runtime rt, Task current)
						throws Exception {
					
					react(null);
					
					System.out.println("Ended");
				}}, Runtime.NO_HINTS);

		AeminiumRuntime.rt.schedule(t1, Runtime.NO_PARENT, Runtime.NO_DEPS);
	}

}
