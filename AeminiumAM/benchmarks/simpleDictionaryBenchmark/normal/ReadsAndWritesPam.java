package benchmarks.simpleDictionaryBenchmark.normal;

import java.util.Random;

import examples.simpleDictionaryExample.DictionaryExampleInsertsReads;

public class ReadsAndWritesPam {
	
	public static void main(String[] args) {
		long maxWorkTime = 20000000;
		long inc = 1000000;
		int nMsgs = 5000;
		long init=0;
		
		int repetitions = 1;
		
		for(long workTime=init; workTime<=maxWorkTime; workTime+=inc){
			long subtotal=0;
			
			System.out.println();
			System.out.println(workTime);
			for(int i=0; i<repetitions;i++){
				DictionaryExampleInsertsReads.rt = aeminium.runtime.implementations.Factory.getRuntime();
				DictionaryExampleInsertsReads.rt.init();
				
				new DictionaryExampleInsertsReads(false,true,workTime);
				
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

				subtotal = System.nanoTime()-start;
				System.out.println(subtotal);
			}
		}
	}
}
