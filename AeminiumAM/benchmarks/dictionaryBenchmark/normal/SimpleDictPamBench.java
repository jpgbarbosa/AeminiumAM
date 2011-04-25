package benchmarks.dictionaryBenchmark.normal;

import examples.dictionaryExample.DictionaryExample;

public class SimpleDictPamBench {
	
	public static void main(String[] args) {
		long workTime = 15000000;
		int reps = 1;
		int inc = 3;
		int nMsg = 3;
		int dictSize = 500; //Can be <= 500. No more than that!
		boolean useSpin = true;

		for(int j=inc; j<=nMsg; j+=inc){
			long subtotal=0;
			
			System.out.println();
			System.out.println(j);
			for(int i=0; i<reps;i++){
				DictionaryExample.rt = aeminium.runtime.implementations.Factory.getRuntime();
				DictionaryExample.rt.init();
				
				new DictionaryExample(j,dictSize,useSpin,workTime);
				
				long start = System.nanoTime();
				DictionaryExample.reader.startAsking(null);			
				DictionaryExample.rt.shutdown();

				subtotal = System.nanoTime()-start;
				System.out.println(subtotal);
			}
		}
	}
}
