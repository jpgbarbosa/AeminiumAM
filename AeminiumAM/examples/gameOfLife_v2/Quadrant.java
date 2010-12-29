package examples.gameOfLife_v2;

import examples.gameOfLife_v2.messages.AskBoundary;
import examples.gameOfLife_v2.messages.Boundary;
import examples.gameOfLife_v2.messages.Process;
import examples.gameOfLife_v2.messages.StartMessage;
import examples.gameOfLife_v2.messages.TryNewRound;
import actor.Actor;
import actor.Dispatcher;

public class Quadrant extends Actor{
	boolean useDisp;
	
	int round;
	boolean isProcessed;
	
	boolean [][] tempMatrix;
	boolean [][] matrix;
	int [][] bufferMatrix;
	
	Actor [] neighbourds;
	int [][] boundaries;
	
	int myRowNo, maxRowNo,rows;
	int myColNo, maxColNo,cols;
	
	public Quadrant(int [][] matrix, Actor [] actors,int myRowNo, boolean useDisp){
		/*
		 * TODO: build a nice constructor that call a function Initialize;
		 */
		this.useDisp = useDisp;
		round = 1;
		tempMatrix = new boolean[matrix.length+2][matrix[0].length+2];
		bufferMatrix = new int[matrix.length+2][matrix[0].length+2];
	}

	@Override
	protected void react(Object obj) {
		if( obj instanceof Boundary){
			if(useDisp){
				Dispatcher.handle(this, "borderIncorporator", (Boundary)obj);
			} else {
				borderIncorporator((Boundary)obj);
			}
		} else if ( obj instanceof AskBoundary){
			if(useDisp){
				Dispatcher.handle(this, "sendBorder", (AskBoundary)obj);
			} else {
				sendBorder((AskBoundary)obj);
			}
		} else if (obj instanceof Process){
			processQuadrant();
			isProcessed=true;
			this.sendMessage(new TryNewRound());
		} else if (obj instanceof TryNewRound){
			/*
			 * TODO: check if we sent and receive all the borders and if we processed all info
			 * if yes:
			 * 		copy tempMatrix to matrix, reset to boundaries[][], clean buffers and temps, round++, and StartMEssage
			 * -> function Initialize
			 * if no: 
			 * 		do nothing
			 */
			
		} else if(obj instanceof StartMessage){
			/*
			 * TODO: ask for boundaries again!
			 */
		}
		
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
		
		if(isProcessed){
			this.sendMessage(new TryNewRound());
		}
		
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
			if(boundaries[i][0]==1 && boundaries[i][0]==0){
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
