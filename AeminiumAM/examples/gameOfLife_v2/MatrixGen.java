package examples.gameOfLife_v2;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Random;

public class MatrixGen {

	public int x;
	
	public int colSize;
	public int rowSize;
	
	public boolean [][] table;
	
	public MatrixGen(int x){
		
		rowSize = (int) Math.pow(2, x);
		colSize = (int) Math.pow(2, x);
		
		table = new boolean[rowSize][colSize];
		
		Random rand = new Random();
		
		for(int i=0;i<rowSize;i++){
			for(int j=0;j<colSize;j++){
				if(rand.nextInt(100)<25){
					table[i][j] = false;
				} else {
					table[i][j] = true;
				}
			}
		}
		
		FileOutputStream f_out;
		try {
			f_out = new FileOutputStream("gameOfLifeTable.bin");
			
			ObjectOutputStream obj_out = new ObjectOutputStream (f_out);
			
			obj_out.writeObject ( table );
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		MatrixGen mg = new MatrixGen(4);
		
	}
	
}
