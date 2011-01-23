package main;

import java.util.ArrayList;

import actor.Actor;
import actor.annotations.Read;
import actor.annotations.Write;

public class TestActor extends Actor{
		
	public ArrayList<Integer> cenas;
	
	public int val;
	
	static public int result;

	public TestActor() {
		super();
		cenas = new ArrayList<Integer>();
		cenas.add(0);
		cenas.set(0, 3);
	}
	
	@Override
	@Write
	public void react(Object obj){
		
		int x;
		
		react2(obj);
		
		//Dispatcher.handle(this,"react2",obj);
		
	//	Dispatcher.handle(this,"react1",obj);
		
	//	Dispatcher.handle(this,"react2",obj);
				
	}
	
	@Write
	public void react1(Object m){
		//cenas.set(0,2);
		
		react2(null);
		
		//cenas.set(0,3);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//cenas.set(0,4);
		System.out.println("react1 em execução!");
	}
	
	@Read
	public void react2(Object m){
		System.out.println("react2 em execução! cenas[0]=");
	}
	

	public static void main(String[] args) {
		
		TestActor a = new TestActor();
			
		a.sendMessage(3);
	}
}
