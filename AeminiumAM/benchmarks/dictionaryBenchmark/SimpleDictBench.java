package benchmarks.dictionaryBenchmark;

import examples.dictionaryExample.DictionaryExample;
import examples.dictionaryExample.DictionaryExampleAtomic;

@SuppressWarnings("unused")
public class SimpleDictBench {

	
	public static void main(String[] args) {
		for(int j=100; j<=500; j+=100){
			int subtotal=0;
			for(int i=0; i<30;i++){
				//DictionaryExampleAtomic de = new DictionaryExampleAtomic(100,j);
				new DictionaryExample(100,j);
				
				long start = System.nanoTime();
				DictionaryExample.reader.sendMessage(null);
				DictionaryExample.art.endAeminiumRuntime();				
				
				subtotal += System.nanoTime()-start;
			}
			subtotal/=20;
			System.out.println(subtotal);
		}
	}
}
