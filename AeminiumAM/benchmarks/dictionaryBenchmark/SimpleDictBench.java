package benchmarks.dictionaryBenchmark;

import examples.dictionaryExample.*;

public class SimpleDictBench {
	
	public static void main(String[] args) {
		for(int j=100; j<=500; j+=100){
			long subtotal=0;

			System.out.println();
			System.out.println(j);
			for(int i=0; i<30;i++){
				//new DictionaryExampleAtomic(100,j);
				new DictionaryExample(100,j);
				
				long start = System.nanoTime();
				DictionaryExample.reader.startAsking(null);			
				
				subtotal = System.nanoTime()-start;
				System.out.println(subtotal);
			}
		}
	}
}
