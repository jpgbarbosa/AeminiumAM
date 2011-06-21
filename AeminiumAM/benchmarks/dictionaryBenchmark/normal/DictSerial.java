package benchmarks.dictionaryBenchmark.normal;

import examples.dictionaryExample.DictionaryExampleAtomic;

public class DictSerial {

	
	public static void main(String[] args) {
		for(int j=100; j<=500; j+=100){
			long subtotal=0;

			System.out.println();
			System.out.println(j);
			for(int i=0; i<30;i++){
				new DictionaryExampleAtomic(j,500,false,15000000);
				
				long start = System.nanoTime();
				DictionaryExampleAtomic.reader.sendMessage(null);
				DictionaryExampleAtomic.art.endAeminiumRuntime();				
				
				subtotal = System.nanoTime()-start;
				System.out.println(subtotal);
			}
		}
	}
}
