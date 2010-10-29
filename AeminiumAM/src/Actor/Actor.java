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

		Task t1 = rt.createNonBlockingTask(new Body() {
			@Override
			public void execute(Runtime rt, Task current) throws Exception {

				react();

			}
		}, Runtime.NO_HINTS);

		rt.schedule(t1, Runtime.NO_PARENT, Runtime.NO_DEPS);
	}

}
