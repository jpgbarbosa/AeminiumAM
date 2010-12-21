package examples.dinningPhilosophers;

public class Fork{
	
    public String name;
    
    public boolean available = true;	    
    
    public Fork(String name){
    	this.name = name;
    }
    
    public String getName(){
    	return name;
    }
    
    synchronized public boolean checkAvailability(){
    	return available;
    }
    synchronized public void setAvailability(boolean available){
    	this.available = available;
    }
}