package main;

import java.util.ArrayList;

import actor.Actor;
import actor.Dispatcher;
import annotations.writable;

public class TestActor extends Actor{
		
	@writable
	public ArrayList<Integer> cenas;
	
	public int val=3;
	
	@writable
	static public int result;

	public TestActor() {
		super();
		cenas = new ArrayList<Integer>();
		cenas.add(0);
		cenas.set(0, 3);
	}
	
	@Override
	protected void react(Object obj){
		
		result = 42 + ((Integer)obj) + val;			
		
		Dispatcher.handle(this,"react1",obj);
		
		Dispatcher.handle(this,"react2",obj);
				
	}
	
	@SuppressWarnings("unused")
	private void react1(Object m){
		cenas.set(0,2);
		
		react2(null);
		
		cenas.set(0,3);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		cenas.set(0,4);
		System.out.println("react1 em execução!");
	}
	
	private void react2(Object m){
		System.out.println("react2 em execução! cenas[0]="+cenas.get(0));
	}
}
