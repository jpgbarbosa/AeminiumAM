package actor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import aeminium.runtime.Body;
import aeminium.runtime.Hints;
import aeminium.runtime.Runtime;
import aeminium.runtime.Task;

public abstract class Actor {

	protected class ConsumerOut implements Runnable {
		private final BlockingQueue<Task> queue;

		ConsumerOut(BlockingQueue<Task> q) {
			queue = q;
		}

		@Override
		public void run() {
			try {
				while (true) {
					consume(queue.take());
				}
			} catch (InterruptedException ex) {
				System.out.println("exception consumer actor, exiting...");
			}

		}

		void consume(Task task) throws InterruptedException {

			String m = task.toString();
			String name = m.substring(m.indexOf('<') + 1, m.indexOf('>'));
			// System.out.println("task name: " + m);
			if (!name.equals("Reader")) {
				System.out
						.println("is in consume OUTOUTOUT inside !Reader, name: <"
								+ name + "<" + m + ">" + ">");
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

				if (name.equals("End")) {
					throw new InterruptedException();
				}

			} else {
				try {
					Collection<Task> deps = Runtime.NO_DEPS;

					if (previousTaskWasWriter) {
						lastReaders.clear();
						previousTaskWasWriter = false;
					}
					lastReaders.add(task);
					deps = new ArrayList<Task>(1);
					deps.addAll(lastWriter);

					rt.schedule(task, Runtime.NO_PARENT, deps);
					// System.out.println("scheduled "+task.toString());

				} catch (Exception e) {
					System.out.println("here " + e);
				}
			}
		}

	}

	public int privateCounter = 0;
	public int msgs = 500;

	protected class Consumer {
		private final BlockingQueue<Task> queue;

		Consumer(BlockingQueue<Task> q) {
			queue = q;
		}

		public void run() {
			try {
				boolean flag;
				while (true) {
					Task task = queue.poll(100, TimeUnit.MICROSECONDS);

					if (privateCounter == msgs)
						break;

					if (task != null) {
						privateCounter++;
						
						flag=consume(task);

						if (!flag || privateCounter == msgs) {
							System.out.println("if exiting...");
							break;
						}
					} else {
						Task t = rt.createNonBlockingTask(new Body() {
							@Override
							public void execute(Runtime rt, Task current)
									throws Exception {
								consumer.run();
							}

							@Override
							public String toString() {
								return "ConsumerRec";
							}
						}, Hints.NO_HINTS);

						rt.schedule(t, Runtime.NO_PARENT, Runtime.NO_DEPS);
						break;
					}
				}
			} catch (Exception e) {
				System.out.println("exception consumer actor, exiting..."+e);
			}
		}

		public boolean consume(Task task) {

			String m = task.toString();
			String name = m.substring(m.indexOf('<') + 1, m.indexOf('>'));
			if (name.equals("Reader")) {
				//try {
					Collection<Task> deps = Runtime.NO_DEPS;

					if (previousTaskWasWriter) {
						lastReaders.clear();
						previousTaskWasWriter = false;
					}
					lastReaders.add(task);
					deps = new ArrayList<Task>(1);
					deps.addAll(lastWriter);
					
					rt.schedule(task, Runtime.NO_PARENT,deps);

			} else {

				if (name.equals("End")) {
					System.out.println("End exiting... " + name);
					return false;
				}
/*
				try {
					System.out.println("is in consume inside !Reader, name: <"
							+ name + "> <" + m + ">");
				} catch (Exception e) {
					System.out.println("here: " + e);
				}
	*/			
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

			}
			return true;
		}
	}

	/* AspectJ vars */
	protected Collection<Task> lastWriter = new LinkedList<Task>();
	protected Collection<Task> lastReaders = new LinkedList<Task>();
	protected boolean previousTaskWasWriter = false;

	protected BlockingQueue<Task> queue = new LinkedBlockingQueue<Task>();

	protected aeminium.runtime.Runtime rt;

	// public ConsumerOut consumer;
	public Consumer consumer;

	public Actor() {
		consumer = new Consumer(queue);
		// consumer = new ConsumerOut(queue);
		// new Thread(consumer).start();
	}

	abstract public void end();
}
