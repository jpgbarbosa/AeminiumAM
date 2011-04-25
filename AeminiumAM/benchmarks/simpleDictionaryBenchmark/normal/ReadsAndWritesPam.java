package benchmarks.simpleDictionaryBenchmark.normal;

import java.util.Random;

import examples.simpleDictionaryExample.DictionaryExampleInsertsReads;

public class ReadsAndWritesPam {
	
	public static void main(String[] args) {
		long maxWorkTime = 1000000;
		long inc = 1000000;
		int nMsgs = 5000;
		long init=0;
		int i=0;
		long first = 0;
		long temp;
		
		int repetitions = 3;
		
		for(long workTime=init; workTime<=maxWorkTime; workTime+=inc){
			long subtotal=0;
			
			System.out.println();
			System.out.println(workTime);
			for(i=1; i<=repetitions;i++){
				DictionaryExampleInsertsReads.rt = aeminium.runtime.implementations.Factory.getRuntime();
				DictionaryExampleInsertsReads.rt.init();
				
				new DictionaryExampleInsertsReads(true,false,maxWorkTime);
				
				long start = System.nanoTime();
				
				Random randP = new Random(10);
			
				for(int k = 0; k<nMsgs; k++){
					if(randP.nextInt(10)<2){
						DictionaryExampleInsertsReads.reader.makeWriteRequest(null);
					} else {
						DictionaryExampleInsertsReads.reader.makeReadRequest(null);
					}
				}
						
				DictionaryExampleInsertsReads.rt.shutdown();
				
				temp = System.nanoTime()-start;
				
				if(i==1){
					first = temp;
				}
				
				subtotal += temp;
			}
			
			i--;
			System.out.println("Reps: "+i);
			System.out.println("no warm up: "+subtotal/i);
			System.out.println("with warm up: "+(subtotal-first)/(i-1));
		}
	}
}
