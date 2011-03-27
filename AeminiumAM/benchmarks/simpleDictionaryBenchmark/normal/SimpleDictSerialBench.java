package benchmarks.simpleDictionaryBenchmark.normal;

import examples.dictionaryExample.DictionaryExampleAtomic;

public class SimpleDictSerialBench {
	
	public static void main(String[] args) {
		long workTime = 0;

		for(int j=500; j<=500; j+=100){
			long subtotal=0;
			
			System.out.println();
			System.out.println(j);
			for(int i=0; i<1;i++){
				DictionaryExampleAtomic.rt = aeminium.runtime.implementations.Factory.getRuntime();
				DictionaryExampleAtomic.rt.init();
				new DictionaryExampleAtomic(j,500, true, workTime);
				
				long start = System.nanoTime();
				DictionaryExampleAtomic.reader.startAsking(null);			
				DictionaryExampleAtomic.rt.shutdown();

				subtotal = System.nanoTime()-start;
				System.out.println(subtotal);
			}
		}
	}
}
