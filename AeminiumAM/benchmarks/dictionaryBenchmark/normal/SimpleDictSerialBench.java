package benchmarks.dictionaryBenchmark.normal;

import examples.dictionaryExample.DictionaryExampleAtomic;

public class SimpleDictSerialBench {
	
	public static void main(String[] args) {
		long workTime = 15000000;
		int reps = 30;
		int inc = 100;
		int nMsg = 500;
		int dictSize = 500; //Can be <= 500. No more than that!
		boolean useSpin = true;

		for(int j=inc; j<=nMsg; j+=inc){
			long subtotal=0;
			
			System.out.println();
			System.out.println(j);
			for(int i=0; i<reps;i++){
				DictionaryExampleAtomic.rt = aeminium.runtime.implementations.Factory.getRuntime();
				DictionaryExampleAtomic.rt.init();
				
				new DictionaryExampleAtomic(j,dictSize, useSpin, workTime);
				
				long start = System.nanoTime();
				DictionaryExampleAtomic.reader.startAsking(null);			
				DictionaryExampleAtomic.rt.shutdown();

				subtotal = System.nanoTime()-start;
				System.out.println(subtotal);
			}
		}
	}
}
