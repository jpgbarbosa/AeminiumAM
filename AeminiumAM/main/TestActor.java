package main;

import actor.Actor;
import actor.annotations.Read;
import actor.annotations.Write;

public class TestActor extends Actor{
	
	static private int val;
	
	static public int result;

	public TestActor() {
		super();
		val=1;
	}
	
	@Write
	public void doSomethingX(Object m){
		setVal(2);
		
		doSomethingY(null);
		
		setVal(3);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		setVal(4);
		System.out.println("foo1 is going to finish! val="+(getVal()+1));
	}
	
	@Read
	private void doSomethingY(Object m){
		System.out.println("foo2 is executing with val="+getVal());
	}
	
	@Write
	public void setVal(int x){
		val=x;
	}
	
	@Read
	private int getVal(){
		return val;
	}
	

	public static void main(String[] args) {
		
		TestActor a = new TestActor();
			
		a.doSomethingX(null);
	}
}
