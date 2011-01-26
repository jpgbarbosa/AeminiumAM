package unused.examples.gameoflife;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Map.Entry;

import actor.Actor;
import actor.Dispatcher;

public class Quadrante extends Actor{

	Hashtable<Integer, Vector<Boolean>> hash;
	Hashtable<Actor, Integer> neighbourds;
	Actor gameControler;
		
	boolean [][] cells;
	boolean [][] cellsBackup;
	int [][] cellsBuffer;
	
	boolean [] boundariesDone;
		
	int cellCols;
	int cellRows;
	int done;
	
	Quadrante(){
		hash = new Hashtable<Integer, Vector<Boolean>>(5);
	}
	
	@Override
	protected void react(Object obj) {
		if( obj instanceof MeetMessage){
			cells = ((MeetMessage)obj).matrix.clone();
			cellsBackup = cells.clone();
			cellRows = cells.length;
			cellCols = cells[0].length;
			neighbourds = ((MeetMessage)obj).neighbourds;
			
			//inicializamos a informação das fronteiras existentes a null
			for(Entry<Actor,Integer> entry : neighbourds.entrySet()){
				hash.put(entry.getValue(), null);
			}
			
			this.gameControler = ((MeetMessage)obj).gameControler;
			
			//Processamos o interior da matriz
			Dispatcher.handle(this, "Refresh", null);
			
			//as fronteiras são chamadas
			Dispatcher.handle(this, "callBoundaries", null);
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
	
	public void RefreshBoundary(int boundarySide){
		switch (boundarySide){
			case 0:
				for(int i=0; i<cellCols;i++){
					cellsBuffer[0][i]=getAlives(0,i);
					switch( cellsBuffer[0][i] ) {
						case 2:
							// no change
							break;
						case 3:
							cells[0][i] = true;
							break;
						default:
							cells[0][i] = false;
							break;
					}
				}
				break;
			case 2:
				for(int i=0; i<cellRows;i++){
					cellsBuffer[i][0]=getAlives(i,0);
					
					switch( cellsBuffer[i][0] ) {
						case 2:
							// no change
							break;
						case 3:
							cells[i][0] = true;
							break;
						default:
							cells[i][0] = false;
							break;
					}
				}
				break;
			case 4:
				for(int i=0; i<cellCols;i++){
					cellsBuffer[cellRows-1][i]=getAlives(cellRows-1,i);
					
					switch( cellsBuffer[cellRows-1][i] ) {
						case 2:
							// no change
							break;
						case 3:
							cells[cellRows-1][i] = true;
							break;
						default:
							cells[cellRows-1][i] = false;
							break;
					}
				}
				break;
			case 6:
				for(int i=0; i<cellRows;i++){
					cellsBuffer[i][cellCols-1]=getAlives(i,cellCols-1);
					
					switch( cellsBuffer[i][cellCols-1] ) {
						case 2:
							// no change
							break;
						case 3:
							cells[i][cellCols-1] = true;
							break;
						default:
							cells[i][cellCols-1] = false;
							break;
					}
				}
			break;
		}
	}
	
	private int getAlives(int x, int y) {
		int ctr=0;
		
		//cantos
		if(x==0 && y==0){
			if(hash.contains(1) && hash.get(1).get(0)){
				ctr++;
			}
			for(int i=0;i<2;i++){
				if(hash.contains(2) && hash.get(2).get(i)){
					ctr++;
				}
				if(hash.contains(0) && hash.get(0).get(i)){
					ctr++;
				}
			}
			
			for(int i=0; i<2; i++){
				for( int j=0; j<2; j++){
					if(cellsBackup[i][j]){
						ctr++;
					}
				}
			}
		}
		else if(x==cellRows-1 && y==0){
			if(hash.contains(3) && hash.get(3).get(0)){
				ctr++;
			}
			for(int i=cellRows-2;i<cellRows;i++){
				if(hash.contains(2) && hash.get(2).get(i)){
					ctr++;
				}
				if(hash.contains(4) && hash.get(4).get(i-(cellRows-2))){
					ctr++;
				}
			}
			
			for(int i=cellRows-2; i<cellRows; i++){
				for( int j=0; j<2; j++){
					if(cellsBackup[i][j]){
						ctr++;
					}
				}
			}
		}
		else if(x==cellRows-1 && y==cellCols-1){
			if(hash.contains(5) && hash.get(5).get(0)){
				ctr++;
			}
			for(int i=cellCols-2;i<cellCols;i++){
				if(hash.contains(4) && hash.get(4).get(i)){
					ctr++;
				}
				if(hash.contains(6) && hash.get(6).get(i)){
					ctr++;
				}
			}
			
			for(int i=cellRows-2; i<cellRows; i++){
				for( int j=cellCols-2; j<cellCols; j++){
					if(cellsBackup[i][j]){
						ctr++;
					}
				}
			}
		}
		else if(x==0 && y==cellCols-1){
			if(hash.contains(7) && hash.get(7).get(0)){
				ctr++;
			}
			for(int i=cellCols-2;i<cellCols;i++){
				if(hash.contains(0) && hash.get(0).get(i)){
					ctr++;
				}
				if(hash.contains(6) && hash.get(6).get(i-(cellCols-2))){
					ctr++;
				}
			}
			
			for(int i=0; i<2; i++){
				for( int j=cellCols-2; j<cellCols; j++){
					if(cellsBackup[i][j]){
						ctr++;
					}
				}
			}
		} 
		//Fronteiras normais
		else{
			if(x==0){
				if(hash.contains(0) && hash.get(0)!=null){
					for(int j=y-1; j<y+2;j++){
						if(hash.get(0).get(j)){
							ctr++;
						}
					}
				}
				for(int i=0; i<2; i++){
					for( int j=y-1; j<y+2 ;j++){
						if(cellsBackup[i][j]){
							ctr++;
						}
					}
				}
			}
			else if(y==0){
				if(hash.contains(2) && hash.get(2)!=null){
					for(int j=x-1; j<x+2;j++){
						if(hash.get(2).get(j)){
							ctr++;
						}
					}
				}
				for(int i=x-1; i<x+2; i++){
					for( int j=y; j<y+2 ;j++){
						if(cellsBackup[i][j]){
							ctr++;
						}
					}
				}
			}
			else if(x==cellRows-1){
				if(hash.contains(4) && hash.get(4)!=null){
					for(int j=y-1; j<y+2;j++){
						if(hash.get(4).get(j)){
							ctr++;
						}
					}
				}
				for(int i=cellRows-2; i<cellRows; i++){
					for( int j=y-1; j<y+2 ;j++){
						if(cellsBackup[i][j]){
							ctr++;
						}
					}
				}
			}
			else if(y==cellCols-1){
				if(hash.contains(6) && hash.get(6)!=null){
					for(int j=x-1; j<x+2;j++){
						if(hash.get(6).get(j)){
							ctr++;
						}
					}
				}
				for(int i=x-1; i<x+2; i++){
					for( int j=y-1; j<cellCols ;j++){
						if(cellsBackup[i][j]){
							ctr++;
						}
					}
				}
			}
		}
		
		return ctr;
	}

	public void ProcessBoundary(Object obj){
		Boundary b = (Boundary)obj;
		
		hash.put(b.type, b.vector);
		
		if(b.type%2==1){
			b.type-=1;
			ProcessBoundary(b);
			b.type=(b.type+2)%8;
			ProcessBoundary(b);
			return;
		}
		
		if(b.type==2){
			for(int i=0;i<=4;i++){
				if(hash.contains(i) && hash.get(i)==null){
					return;
				}
			}
			RefreshBoundary(2);
		}
		else if(b.type==4){
			for(int i=2;i<=6;i++){
				if(hash.contains(i) && hash.get(i)==null){
					return;
				}
			}
			RefreshBoundary(4);
		}
		else if(b.type==6){
			for(int i=4;i<=6 || i==1;i=(i++)%8){
				if(hash.contains(i) && hash.get(i)==null){
					return;
				}
			}
			RefreshBoundary(6);
		}
		else if(b.type==0){
			for(int i=0;i<=7;i++){
				if(i==3){ i=6; }
				
				if(hash.contains(i) && hash.get(i)==null){
					return;
				}
			}
			RefreshBoundary(0);
		}
	}
	
	public void Boundary(Object obj){
		Boundary boundary=null;
		AskBoundary ab = ((AskBoundary)obj);
		
		switch (ab.value){
			case 0: boundary = new Boundary(ab.value,cells,4,cellCols,cellRows); break;
			case 1: boundary = new Boundary(ab.value,cells,5,cellCols,cellRows); break;
			case 2: boundary = new Boundary(ab.value,cells,6,cellCols,cellRows); break;
			case 3: boundary = new Boundary(ab.value,cells,7,cellCols,cellRows); break;
			case 4: boundary = new Boundary(ab.value,cells,0,cellCols,cellRows); break;
			case 5: boundary = new Boundary(ab.value,cells,1,cellCols,cellRows); break;
			case 6: boundary = new Boundary(ab.value,cells,2,cellCols,cellRows); break;
			case 7: boundary = new Boundary(ab.value,cells,3,cellCols,cellRows); break;
		}
				
		ab.actor.sendMessage(boundary);
	}
	
	public void CallBackF(Object obj){
		
		ProcessBoundary(obj);
		
		if(CheckCompleted()){
			gameControler.sendMessage(new Finished(this));
		}
	}
	
	public boolean CheckCompleted(){
		//only checks if boundaries are completed
		if(done == neighbourds.size())
			return true;
		return false;
	}
	
	public void callBoundaries(){
		for(Entry<Actor,Integer> entry : neighbourds.entrySet()){
			entry.getKey().sendMessage(new AskBoundary(entry.getKey(),entry.getValue()));
		}
	}
}
