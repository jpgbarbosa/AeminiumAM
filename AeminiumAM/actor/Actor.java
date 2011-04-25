package actor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import aeminium.runtime.Body;
import aeminium.runtime.Hints;
import aeminium.runtime.Runtime;
import aeminium.runtime.Task;

public abstract class Actor {

	protected class Consumer {
		private final BlockingQueue<Task> queue;

		Consumer(BlockingQueue<Task> q) {
			queue = q;
		}

		public void run() {
			try {
					System.out.println("waiting");
					
					consume(queue.take());
					
					Task t = rt.createNonBlockingTask(new Body() {
						@Override
						public void execute(Runtime rt, Task current) throws Exception {
							consumer.run();
						}

						@Override
						public String toString() {
							return "ConsumerRec";
						}
					}, Hints.NO_HINTS);
					
					rt.schedule(t, Runtime.NO_PARENT, Runtime.NO_DEPS);
					
					System.out.println("took task");
					
			} catch (InterruptedException ex) {
				System.out.println("exception consumer actor");
			}
		}

		void consume(Object obj) {
			Task task = (Task) obj;
			// write
			String m = task.toString();
			String name = m.substring(m.indexOf('<')+1, m.indexOf('>'));
			System.out.println("task name: "+name);
			if (name.equals("Writer")) {
				Collection<Task> deps = null;
				if (previousTaskWasWriter) {
					deps = new ArrayList<Task>(1);
					deps.addAll(lastWriter);
				} else {
					deps = new ArrayList<Task>();
					deps.addAll(lastReaders);
				}
				previousTaskWasWriter = true;
				lastWriter = new ArrayList<Task>(1);
				lastWriter.add(task);
				rt.schedule(task, Runtime.NO_PARENT, deps);
			} else {
				Collection<Task> deps = Runtime.NO_DEPS;

				if (previousTaskWasWriter) {
					lastReaders.clear();
					previousTaskWasWriter = false;
				}
				lastReaders.add(task);
				deps = new ArrayList<Task>(1);
				deps.addAll(lastWriter);
				
				rt.schedule(task, Runtime.NO_PARENT, deps);
			}
			
			
			
		}
	}

	/* AspectJ vars */
	protected Collection<Task> lastWriter = new LinkedList<Task>();
	protected Collection<Task> lastReaders = new LinkedList<Task>();
	protected boolean previousTaskWasWriter = false;

	protected BlockingQueue<Task> queue = new LinkedBlockingQueue<Task>();

	protected aeminium.runtime.Runtime rt;
	
	public Task t;
	
	public Consumer consumer = new Consumer(queue);

	public Actor() {
		//Consumer consumer = new Consumer(queue);
		//new Thread(consumer).start();		
	}
}
