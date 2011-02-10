package benchmarks.dictionaryBenchmark.normal;

import examples.dictionaryExample.*;

public class SimpleDictPamBench {
	
	public static void main(String[] args) {
		long workTime = 15000000;
		
		for(int j=100; j<=500; j+=100){
			long subtotal=0;
			
			System.out.println();
			System.out.println(j);
			for(int i=0; i<30;i++){
				DictionaryExample.rt = aeminium.runtime.implementations.Factory.getRuntime();
				DictionaryExample.rt.init();
				
				new DictionaryExample(j,100,true,workTime);
				
				long start = System.nanoTime();
				DictionaryExample.reader.startAsking(null);			
				DictionaryExample.rt.shutdown();

				subtotal = System.nanoTime()-start;
				System.out.println(subtotal);
			}
		}
	}
}
