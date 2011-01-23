package examples;

import aeminium.actor.Actor;
import aeminium.actor.annotations.Read;
import aeminium.actor.annotations.Write;

public class Foo extends Actor {
	private int value = 0;

	public static void main(String[] args) {
		Foo act = new Foo();
		act.bar(1);
		act.bar(1);
		act.bar(1);
		act.foo(2, "Writer 1");
		act.bar(3);		
		act.bar(3);
		act.foo(4, "Writer 2");
	}
	
	@Write
	public void foo(int x, String s) {
		System.out.println("foo: " + x + " " + s );
	}

	@Read
	public void bar(int x) {
		System.out.println("bar: " + x);
		value = 0; // this should be an error
		setValue(value+1); // this should be an error
	}
	
	@Write
	private void setValue(int x) {
		value = 0;
		missingAnnotation(this); // This should be a warning
	}
	
	public void missingAnnotation(Foo foo) {   // this should be an error 
		foo.value++; // this should be an error
	}
}

class Bar extends Actor {
	
	public void set(Foo foo) {
		foo.bar(10);
	}
}
