package Actor;

import aeminium.runtime.Body;
import aeminium.runtime.Task;
import aeminium.runtime.Runtime;

public class Actor{
	Runtime rt = null;

	public Actor(Runtime rt) {
		this.rt = rt;
	}

	protected void react() {
		// TODO Auto-generated method stub
	}
	
	public void sendMessage() {
		
		Task t1 = rt.createNonBlockingTask(new Body(){		
				@Override
				public void execute(Runtime rt, Task current)
						throws Exception {
					
					react();
					
				}}, Runtime.NO_HINTS);
		
		rt.schedule(t1, Runtime.NO_PARENT, Runtime.NO_DEPS);
	}

}
