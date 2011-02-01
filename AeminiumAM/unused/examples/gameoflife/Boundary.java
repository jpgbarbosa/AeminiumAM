package unused.examples.gameoflife;

import java.util.Vector;

public class Boundary {
	Vector<Boolean> vector;
	int type;
	
	public Boundary(int type, boolean[][] cells, int i, int cellCols, int cellRows) {
		 vector = new Vector<Boolean>(); 
		 
		 this.type=type;
		
		switch (i){
			case 0: getVector(cells,0,1,0,cellCols); break;
			case 1: vector.add(cells[0][0]); break;
			case 2: getVector(cells,0,1,0,cellCols); break;
			case 3: vector.add(cells[cellRows-1][0]); break;
			case 4: getVector(cells,0,1,0,cellCols); break;
			case 5: vector.add(cells[cellRows-1][cellCols-1]); break;
			case 6: getVector(cells,0,1,0,cellCols); break;
			case 7: vector.add(cells[0][cellCols-1]); break;
		}
	}

	private void getVector(boolean[][] cells,int xb, int xe, int yb, int ye) {
		for(int x=xb; x<xe; x++){
			for(int y=yb; y<ye; y++){
				vector.add(cells[x][y]);
			}
		}
	}

}
