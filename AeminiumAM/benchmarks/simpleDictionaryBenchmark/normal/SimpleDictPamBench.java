package benchmarks.simpleDictionaryBenchmark.normal;

import java.util.Random;

import examples.simpleDictionaryExample.*;

public class SimpleDictPamBench {
	
	public static void main(String[] args) {
		long maxWorkTime = 15000000;
		int nMsgs = 50000;
		
		for(long workTime=100; workTime<=maxWorkTime; workTime+=100){
			long subtotal=0;
			
			System.out.println();
			System.out.println(workTime);
			for(int i=0; i<30;i++){
				DictionaryExampleInsertsReads.rt = aeminium.runtime.implementations.Factory.getRuntime();
				DictionaryExampleInsertsReads.rt.init();
				
				new DictionaryExampleInsertsReads(true,workTime);
				
				long start = System.nanoTime();
				
				Random randP = new Random(10);
			
				for(int k = 0; k<nMsgs; k++){
					if(randP.nextInt(10)<2){
						DictionaryExampleInsertsReads.reader.makeWriteRequest(null);
					} else {
						DictionaryExampleInsertsReads.reader.makeReadRequest(null);
					}
				}
				
				DictionaryExampleInsertsReads.reader.makeReadRequest(null);			
				DictionaryExampleInsertsReads.rt.shutdown();

				subtotal = System.nanoTime()-start;
				System.out.println(subtotal);
			}
		}
	}
}
