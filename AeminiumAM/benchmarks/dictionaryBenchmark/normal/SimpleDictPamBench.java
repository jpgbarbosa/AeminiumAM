package benchmarks.dictionaryBenchmark.normal;

import examples.dictionaryExample.*;

public class SimpleDictPamBench {
	
	public static void main(String[] args) {
		long workTime = 1000;
		
		for(int j=500; j<=500; j+=100){
			long subtotal=0;
		
			System.out.println();
			System.out.println(j);
			for(int i=0; i<1;i++){
				DictionaryExample.rt = aeminium.runtime.implementations.Factory.getRuntime();
				DictionaryExample.rt.init();
				
				new DictionaryExample(1,500,true,workTime);
				
				long start = System.nanoTime();
				DictionaryExample.reader.startAsking(null);
				
				DictionaryExample.rt.shutdown();

				subtotal = System.nanoTime()-start;
				System.out.println(subtotal);
			}
		}
		System.out.println();
		System.out.println("schedule:");
		//System.out.println(DictionaryExample.reader.avg/DictionaryExample.reader.ctr);
		System.out.println(DictionaryExample.dictionary.avg/DictionaryExample.dictionary.ctr);
		System.out.println(DictionaryExample.receiver.avg/DictionaryExample.receiver.ctr);
	}
}
