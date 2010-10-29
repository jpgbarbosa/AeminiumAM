package Actor;

import aeminium.runtime.Body;
import aeminium.runtime.Task;
import aeminium.runtime.Runtime;

public abstract class Actor implements IActor{
	Runtime rt = null;

	public Actor(Runtime rt) {
		this.rt = rt;
	}

	public abstract void react();

	public void sendMessage() {
<<<<<<< HEAD
		
		Task t1 = rt.createNonBlockingTask(new Body(){		
				@Override
				public void execute(Runtime rt, Task current)
						throws Exception {
					
					react();
					System.out.println("Ended");
				}}, Runtime.NO_HINTS);
		
=======

		Task t1 = rt.createNonBlockingTask(new Body() {
			@Override
			public void execute(Runtime rt, Task current) throws Exception {

				react();

			}
		}, Runtime.NO_HINTS);

>>>>>>> cf7967a4c7a632e263e6c17a6d453b690e4640b4
		rt.schedule(t1, Runtime.NO_PARENT, Runtime.NO_DEPS);
	}

}
