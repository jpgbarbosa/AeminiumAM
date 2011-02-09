package benchmarks.dictionaryBenchmark.normal;

import examples.dictionaryExample.DictionaryExample;

public class DictPam {

	
	public static void main(String[] args) {
		for(int j=100; j<=500; j+=100){
			long subtotal=0;

			System.out.println();
			System.out.println(j);
			for(int i=0; i<30;i++){
				new DictionaryExample(j,500,true,15000000);
				
				long start = System.nanoTime();
				DictionaryExample.reader.sendMessage(null);
				DictionaryExample.art.endAeminiumRuntime();				
				
				subtotal = System.nanoTime()-start;
				System.out.println(subtotal);
			}
		}
	}
}