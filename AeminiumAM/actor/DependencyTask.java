package actor;

import aeminium.runtime.Task;

public class DependencyTask {
	Task task;
	boolean isWritable;
	
	public DependencyTask(Task task, boolean isWritable) {
		this.task = task;
		this.isWritable = isWritable;
	}
}
