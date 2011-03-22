
public class Spin {

	
	public static void main(String[] args) {
		double workTime = 1000000000;
	
		System.out.println("Start");
		 // convert to nanoseconds
		System.out.println(System.nanoTime());
	    long startTime = System.nanoTime();
	    while ((System.nanoTime() - startTime) < workTime) {}
	    
	    System.out.println(System.nanoTime()-startTime);
	    
	    System.out.println("End");
		
	}
	
}
