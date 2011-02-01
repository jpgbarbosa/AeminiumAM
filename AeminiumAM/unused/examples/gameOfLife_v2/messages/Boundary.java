package unused.examples.gameOfLife_v2.messages;

public class Boundary {
	
	public int index;
	
	public boolean [] boundary;
	
	public Boundary(int index, boolean [] boundary){
		this.index = index;
		this.boundary = boundary;
	}
}
