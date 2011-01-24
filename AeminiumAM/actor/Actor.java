package actor;

import java.util.Collection;
import java.util.LinkedList;
import aeminium.runtime.Task;

public abstract class Actor{
	
	/* AspectJ vars*/
	protected Collection<Task> previousTasks;
	protected Collection<Task> latestWriters;
	protected boolean previousTasksAreWriters;
	
	public Actor() {
		previousTasks           = new LinkedList<Task>();
		latestWriters           = previousTasks;
		previousTasksAreWriters = true;
	}
}
