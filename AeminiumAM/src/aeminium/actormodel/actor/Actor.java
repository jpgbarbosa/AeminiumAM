package aeminium.actormodel.actor;

import java.util.Collection;
import java.util.LinkedList;

import aeminium.runtime.Task;

public abstract class Actor{
	
	/* AspectJ vars*/
	protected Task lastWriter = null;
	protected Collection<Task> lastReaders = new LinkedList<Task>();
	protected boolean previousTaskWasWriter = false;
	
	protected aeminium.runtime.Runtime rt;
	
}
