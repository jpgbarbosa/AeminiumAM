package examples.gameOfLife_v2;

import java.util.LinkedList;
import java.util.Queue;

import examples.gameOfLife_v2.messages.AskBoundary;
import examples.gameOfLife_v2.messages.Boundary;
import examples.gameOfLife_v2.messages.Process;
import examples.gameOfLife_v2.messages.StartMessage;
import examples.gameOfLife_v2.messages.Terminate;
import examples.gameOfLife_v2.messages.TryNewRound;
import actor.Actor;
import actor.Dispatcher;
import annotations.writable;

public class Quadrant extends Actor{
	public boolean useDisp;
	
	public int round;
	@writable
	public boolean isProcessed;
	@writable
	public boolean [][] tempMatrix;
	@writable
	public boolean [][] matrix;
	@writable
	public int [][] bufferMatrix;
	@writable
	public Quadrant [] neighbourds;
	@writable
	public int [][] boundaries;
	
	public int rows,cols;
	
	int maxRounds;
	
	String ind;
	
	public Queue<AskBoundary> queue;
	
	public Quadrant(boolean [][] matrix, Actor [] actors,int rows, int cols, boolean useDisp, int maxRounds, String ind){
		int i;
		
		this.ind = ind;
		
		this.useDisp = useDisp;
		
		this.rows = rows;
		this.cols = cols;
		
		this.maxRounds = maxRounds;
		
		round = 0;
		tempMatrix = new boolean[matrix.length+2][matrix[0].length+2];
		bufferMatrix = new int[matrix.length+2][matrix[0].length+2];
		
		
		this.matrix = matrix;
		
		for(i=1;i<tempMatrix.length-1;i++){
			for(int j=1;j<tempMatrix[0].length-1;j++){
				tempMatrix[i][j]=matrix[i-1][j-1];
			}
		}
		queue = new LinkedList<AskBoundary>();
	}

	@Override
	protected void react(Object obj) {
		if( obj instanceof Boundary){
			//System.out.println(""+((Boundary)obj).index);
			if(useDisp){
				Dispatcher.handle(this, "borderIncorporator", (Boundary)obj);
			} else {
				borderIncorporator((Boundary)obj);
			}
		} else if ( obj instanceof AskBoundary){
			System.out.println("AB round: "+((AskBoundary)obj).round);
			if(useDisp){
				if(((AskBoundary)obj).round == round){
					Dispatcher.handle(this, "sendBorder", (AskBoundary)obj);
				} else {
					queue.add((AskBoundary)obj);
				}
			} else {
				if(((AskBoundary)obj).round == round){
					sendBorder((AskBoundary)obj);
				} else {
					queue.add((AskBoundary)obj);
				}
			}
		} else if (obj instanceof Process){
			processQuadrant();
			isProcessed=true;
			this.sendMessage(new TryNewRound());
		} else if (obj instanceof TryNewRound){
			System.out.println(ind+"  "+ round+"  "+maxRounds);
			if(canGoToNextRound()){
				initialize(false);
				if(round==maxRounds){
					this.sendMessage(new Terminate());
				}else {
					this.sendMessage(new StartMessage());
				}
			}
		} else if(obj instanceof StartMessage){
			
			if(!queue.isEmpty()){
				while(!queue.isEmpty() && queue.peek().round==round){
					if(useDisp){
						Dispatcher.handle(this, "sendBorder", queue.peek());
					} else {
						sendBorder(queue.peek());
					}
				}
			} else {
				int counter=0;
				for(int i = 0;i<neighbourds.length;i++){
					if(neighbourds[i]!=null){
						System.out.println(ind+" : "+i);
						neighbourds[i].sendMessage(new AskBoundary(this,i,round));
						counter++;
					}
				}
				/*System.out.println("ctr: "+counter+"..."+ind);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
			}			
		} else if (obj instanceof Terminate){
			String temp="";
			
			for(int i=0;i<matrix.length; i++){
				for( int j=0;j<matrix[0].length; j++){
					temp+=matrix[i][j]+" ";
				}
				temp+="/n";
			}
			
			System.out.println("-----------");
			System.out.println(ind);
			System.out.println(temp);
			System.out.println("-----------");
			
			return;
		}
	}
	
	public void setActors(Quadrant [] actors){
		neighbourds = actors.clone();
				
		boundaries = new int[actors.length][3];
		
		for(int i=0;i<actors.length;i++){
			if(actors[i]==null){
				boundaries[i][0] = 0;
				boundaries[i][1] = 0;
				boundaries[i][2] = 0;
			} else {
				//System.out.println(ind+" : "+i);
				boundaries[i][0] = 1;
				boundaries[i][1] = 0;
				boundaries[i][2] = 0;
			}
		}
		
		initialize(true);
	}
	
	private void initialize(boolean isFirst){
		int i=0,j=0;
		
		for(i=0;i<boundaries.length;i++){
			boundaries[i][1] = 0;
			boundaries[i][2] = 0;
		}
		isProcessed=false;
		
		//copy from temp to Matrix
		if(!isFirst){
			for(i=1;i<tempMatrix.length-1;i++){
				for(j=1;j<tempMatrix[0].length-1;j++){
					matrix[i-1][j-1]=tempMatrix[i][j];
				}
			}
		}
		
		round++;
	}
	
	private boolean canGoToNextRound(){
		
		if(!isProcessed)
			return false;
		
		for(int i=0; i<boundaries.length;i++){
			if(boundaries[i][0]==1){
				if(boundaries[i][1]!=1 || boundaries[i][2]!=1 ){
					System.out.println("cant go bc: "+ind+":"+i+" || "+boundaries[i][1]+" || "+boundaries[i][2]);
					return false;
				}
			}
		}
		
		return true;
	}
	
	private void sendBorder(AskBoundary obj) {
		
		boolean [] boundary;
		
		int index=-1;
		
		switch (obj.borderIndex){
			case 0:
				boundary = new boolean[cols-2];
				for(int i=1; i<cols-1; i++){
					boundary[i-1] = matrix[rows-1][i];
				}				
				obj.actor.sendMessage(new Boundary(obj.borderIndex,boundary));
				index=4;
				break;
			case 1: 
				boundary = new boolean[1];
				boundary[0] = matrix[rows-1][cols-1];
				obj.actor.sendMessage(new Boundary(obj.borderIndex,boundary));
				index=5;
				break;
			case 2: 
				boundary = new boolean[rows-2];
				for(int i=1; i<rows-1; i++){
					boundary[i-1] = matrix[i][cols-1];
				}				
				obj.actor.sendMessage(new Boundary(obj.borderIndex,boundary));
				index=6;
				break;
			case 3:
				boundary = new boolean[1];
				boundary[0] = matrix[rows-1][0];
				obj.actor.sendMessage(new Boundary(obj.borderIndex,boundary));
				index=7;
				break;
			case 4:
				boundary = new boolean[cols-2];
				for(int i=1; i<cols-1; i++){
					boundary[i-1] = matrix[0][i];
				}				
				obj.actor.sendMessage(new Boundary(obj.borderIndex,boundary));
				index=0;
				break;
			case 5:
				boundary = new boolean[1];
				boundary[0] = matrix[0][0];
				obj.actor.sendMessage(new Boundary(obj.borderIndex,boundary));
				index=1;
				break;
			case 6: 
				boundary = new boolean[rows-2];
				for(int i=1; i<rows-1; i++){
					boundary[i-1] = matrix[i][0];
				}				
				obj.actor.sendMessage(new Boundary(obj.borderIndex,boundary));
				index=2;
				break;
			case 7:
				boundary = new boolean[1];
				boundary[0] = matrix[0][cols-1];
				obj.actor.sendMessage(new Boundary(obj.borderIndex,boundary));
				index=3;
				break;
		}

		boundaries[index][2]=1;
		
		
		if(boundaries[index][0]==0){
			System.out.println("border a zero-> "+ind+" : "+index);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
			this.sendMessage(new TryNewRound());
		
	}

	private void borderIncorporator(Boundary b){
		switch (b.index){
			case 0:
				for(int i=1; i<b.boundary.length; i++){
					tempMatrix[0][i] = b.boundary[i-1];
				}
				break;
			case 1:
				tempMatrix[0][0] = b.boundary[0];
				break;
			case 2:
				for(int i=1; i<b.boundary.length; i++){
					tempMatrix[i][0] = b.boundary[i-1];
				}
				break;
			case 3:
				tempMatrix[rows][0] = b.boundary[0];
				break;
			case 4:
				for(int i=1; i<b.boundary.length; i++){
					tempMatrix[rows][i] = b.boundary[i-1];
				}
				break;
				
			case 5:
				tempMatrix[rows][cols] = b.boundary[0];
				break;
			case 6:
				for(int i=1; i<b.boundary.length; i++){
					tempMatrix[i][cols] = b.boundary[i-1];
				}
				break;
			case 7:
				tempMatrix[0][cols] = b.boundary[0];
				break;
		}
		
		boundaries[b.index][1] = 1;
		
		for(int i=0; i<8;i++){
			if(boundaries[i][0]==1 && boundaries[i][1]==0){
				return;
			}
		}
		
		this.sendMessage(new Process());
		
	}
	
	private void processQuadrant(){
		int x,y;
		
		for( x=0; x<=cols; x++ ) {
			for( y=0; y<=rows; y++ ) {
				bufferMatrix[x][y] = 0;
			}
		}

		// count neighbors of off-edge cells
		for( x=1; x<=cols; x++ ) {
			for( y=1; y<=rows; y++ ) {
				if ( tempMatrix[x][y] ) {
					bufferMatrix[x-1][y-1]++;
					bufferMatrix[x][y-1]++;
					bufferMatrix[x+1][y-1]++;
					bufferMatrix[x-1][y]++;
					bufferMatrix[x+1][y]++;
					bufferMatrix[x-1][y+1]++;
					bufferMatrix[x][y+1]++;
					bufferMatrix[x+1][y+1]++;
				}
			}
		}
		
		for( x=1; x<=cols; x++ ) {
			for( y=1; y<=rows; y++ ) {
				switch( bufferMatrix[x][y] ) {
					case 2:
						// no change
						break;
					case 3:
						tempMatrix[x][y] = true;
						break;
					default:
						tempMatrix[x][y] = false;
						break;
				}
			}
		}
	}
	
}
