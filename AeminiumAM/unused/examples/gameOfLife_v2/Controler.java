package unused.examples.gameOfLife_v2;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map.Entry;

import unused.AeminiumRuntime;

import examples.gameOfLife_v2.messages.StartMessage;

import actor.Actor;

/*
 * TODO: this class is responsible for dividing the entire matrix for the actors.
 * Main sends a StartMessage for each actor
 */
public class Controler {
	
	int noRounds;
	
	int x;
	int blockDim;
	
	int colSize;
	int rowSize;
	
	boolean [][] table;
	
	HashMap<String, Quadrant> actorsDic;
	
	public Controler (int noRounds,int blockDim){
		this.noRounds = noRounds;
		this.blockDim = blockDim;
		
		try {
			FileInputStream f_in = new 
			FileInputStream("gameOfLifeTable.bin");
	
			ObjectInputStream obj_in;
		
			obj_in = new ObjectInputStream (f_in);
			
			table = (boolean[][]) obj_in.readObject();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		x = table.length;
		
		if (x<=blockDim){
			System.out.println("Invalid dimensions for sub-blocks");
			return;
		}
		
		double noActors = Math.pow(x/blockDim,2);
		
		
		int line = (int) Math.sqrt(noActors);
		
		actorsDic = new HashMap<String,Quadrant>();
		HashMap<Index, boolean[][]> tablesDic = new HashMap<Index,boolean[][]>();
		
		int xI=0, yI=0;
		
		boolean [][] tempTable;
		
		
		for(int xBlock=0;xBlock<table.length;xBlock+=blockDim){
			xI++;
			yI=0;
			for(int yBlock=0; yBlock<table[0].length;yBlock+=blockDim){
				yI=((++yI)%(line+1));
				tempTable=new boolean[blockDim][blockDim];
				
				for(int i=xBlock; i<xBlock+blockDim;i++){
					for(int j=yBlock; j<yBlock+blockDim;j++){
						tempTable[i%blockDim][j%blockDim]=table[i][j];
					}
				}
				Index ind = new Index(xI,yI);
				
				tablesDic.put(ind, tempTable);
				actorsDic.put(""+xI+yI, new Quadrant(tempTable, new Actor[0], blockDim, blockDim, false, noRounds,""+xI+yI));
			}
		}

		Actor [] actorsTemp;
		int x,y;
		
		int i=0;
		
		for(Entry<String, Quadrant> entry : actorsDic.entrySet()){
			actorsTemp = new Quadrant[8];
			
			
			
			String temp = entry.getKey();
			
			x= Integer.parseInt(new String(temp.charAt(0)+""));
			y= Integer.parseInt(new String(temp.charAt(1)+""));
						
			actorsTemp[0] = actorsDic.get((x-1)+""+(y));
			actorsTemp[1] = actorsDic.get((x-1)+""+(y-1));
			actorsTemp[2] = actorsDic.get((x)+""+(y-1));
			actorsTemp[3] = actorsDic.get((x+1)+""+(y-1));
			actorsTemp[4] = actorsDic.get((x+1)+""+(y));
			actorsTemp[5] = actorsDic.get((x+1)+""+(y+1));
			actorsTemp[6] = actorsDic.get((x)+""+(y+1));
			actorsTemp[7] = actorsDic.get((x-1)+""+(y+1));
						
			entry.getValue().setActors((Quadrant[]) actorsTemp);
			
			if(i==4){
				System.out.println(x+""+y);
				for(int l=0; l<8; l++){
					System.out.println(actorsTemp[l]);
				}
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			i++;
		}
	}
	
	public void wakeUpActors(){
		for(Entry<String, Quadrant> entry : actorsDic.entrySet()){
			entry.getValue().sendMessage(new StartMessage());
		}
	}
	
	static AeminiumRuntime art = new AeminiumRuntime();
	
	public static void main(String[] args) {
		
		Controler c = new Controler(5, 4);
		
		c.wakeUpActors();
		
		art.endAeminiumRuntime();
		
		
	}
}
