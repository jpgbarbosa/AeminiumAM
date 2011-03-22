package actor;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Hashtable;
import java.util.LinkedList;

import examples.dictionaryExample.DictionaryExampleAtomic;

import aeminium.runtime.Task;

public abstract class Actor{
	public int ctr=0;

	public long outSideLock = 0;
	public long insideSideLock = 0;
	public long avg=0;
		
	//public DictionaryExampleAtomic dea=null;
	
	public int idCounter = 0;
	public Hashtable <Integer, Long> mapActor; 
	
	/* AspectJ vars*/
	protected Collection<Task> previousTasks;
	protected Collection<Task> latestWriters;
	protected boolean previousTasksAreWriters;
	
	protected aeminium.runtime.Runtime rt;
	
	public Actor() {
		previousTasks           = new LinkedList<Task>();
		latestWriters           = previousTasks;
		previousTasksAreWriters = true;
	}
}
