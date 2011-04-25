package actor;

import java.util.Collection;
import java.util.LinkedList;

import aeminium.runtime.Task;

public abstract class Actor{
	
	/* AspectJ vars*/
	protected Collection<Task> lastWriter = new LinkedList<Task>();
	protected Collection<Task> lastReaders = new LinkedList<Task>();
	protected boolean previousTaskWasWriter = false;
	
	protected aeminium.runtime.Runtime rt;
	
	public Actor() {}
}
