package examples.gameoflife;

import java.util.HashMap;
import java.util.Vector;

import actor.Actor;
import actor.Dispatcher;

public class Quadrante extends Actor{

	HashMap<Integer, Vector<Boolean>> hash;
	boolean own;
	
	boolean [][] cells;
	int [][] cellsBuffer;
	
	Actor [] neighbourActors;
	
	int neighboursNum;
	int cellCols;
	int cellRows;
	int done;
	
	Quadrante(){
		neighbourActors = null;
		own = false;
	}
	
	@Override
	protected void react(Object obj) {
		if( obj instanceof MeetMessage){
			cells = ((MeetMessage)obj).matrix.clone();
			cellRows = cells.length;
			cellCols = cells[0].length;
			neighboursNum = ((MeetMessage)obj).neigh.length;
			neighbourActors = ((MeetMessage)obj).neigh;
			Dispatcher.handle(this, "Refresh", null);
		}
	}
	
	public void Refresh(){
		ProcessQuad();
		//own=true;
		//em vez de ter uma escrita, enviar uma mensagem ao GC a dizer q terminou
		//GC tera de ter numQuads*2 == done para gerar a proxima iteraçao
		if(CheckCompleted()){
			//envia ao GameControler q esta terminado
		}
	}
	
	public void ProcessQuad(){
		int x;
		int y;
		
		// clear the buffer
		for( x=0; x<cellCols; x++ ) {
			for( y=0; y<cellRows; y++ ) {
				cellsBuffer[x][y] = 0;
			}
		}

		// count neighbors of off-edge cells
		for( x=2; x<cellCols-2; x++ ) {
			for( y=2; y<cellRows-2; y++ ) {
				if ( cells[x][y] ) {
					cellsBuffer[x-1][y-1]++;
					cellsBuffer[x][y-1]++;
					cellsBuffer[x+1][y-1]++;
					cellsBuffer[x-1][y]++;
					cellsBuffer[x+1][y]++;
					cellsBuffer[x-1][y+1]++;
					cellsBuffer[x][y+1]++;
					cellsBuffer[x+1][y+1]++;
				}
			}
		}

		// count neighbors of edge cells
		x=2; // start at (1,0)
		y=1;
		int dx=1;
		int dy=0;
		while( true ) {
			if ( cells[x][y] ) {
				if ( x > 1 ) {
					if ( y > 1 )
						cellsBuffer[x-1][y-1]++;
					if ( y < cellRows-2 )
						cellsBuffer[x-1][y+1]++;
					cellsBuffer[x-1][y]++;
				}
				if ( x < cellCols-2 ) {
					if ( y < cellRows-2 )
						cellsBuffer[x+1][y+1]++;
					if ( y > 1 )
						cellsBuffer[x+1][y-1]++;
					cellsBuffer[x+1][y]++;
				}
				if ( y > 1 )
					cellsBuffer[x][y-1]++;
				if ( y < cellRows-2 )
					cellsBuffer[x][y+1]++;
			}

			// turn clockwise at collision with edge
			if ( x==cellCols-2 && y==1 ) {
				dx = 0;
				dy = 1;
			} else if ( x==cellCols-2 && y==cellRows-2 ) {
				dx = -1;
				dy = 0;
			} else if ( x==1 && y==cellRows-2 ) {
				dx = 0;
				dy = -1;
			} else if ( x==1 && y==1 ) {
				// all edge cells done
				break;
			}
			x += dx;
			y += dy;
		}

		// here is the life algorithm
		// simple, isn't it?
		for( x=1; x<cellCols-1; x++ ) {
			for( y=1; y<cellRows-1; y++ ) {
				switch( cellsBuffer[x][y] ) {
					case 2:
						// no change
						break;
					case 3:
						cells[x][y] = true;
						break;
					default:
						cells[x][y] = false;
						break;
				}
			}
		}
	}
	
	public void ProcessBoudary(){
		
		
	}
	
	public void Boundary(Actor actor){
		
	}
	
	public void CallBackF(){
		ProcessBoudary();
		
		if(CheckCompleted()){
			//envia ao GameControler q esta terminado
		}
	}
	
	public boolean CheckCompleted(){
		if(done == neighboursNum+1)
			return true;
		return false;
	}

	
	
}
