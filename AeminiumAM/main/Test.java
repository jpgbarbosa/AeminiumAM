package main;

import actor.*;

public class Test {
	
	static AeminiumRuntime art = new AeminiumRuntime();

	public static void main(String[] args) {
		
		TestActor a = new TestActor();
			
		a.sendMessage(3);
		
		art.endAeminiumRuntime();
	
		System.out.println("Testing result: "+a.result);

	}

}
