package examples.gameOfLife_v2;

public class Index {
	public int x;
	public int y;
	
	public Index(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(x==((Index)obj).x && y==((Index)obj).y){
			return true;
		}
		return false;
	}
	
	@Override
	public String toString(){
		return "Index: " +x+"."+y; 
		}
}
