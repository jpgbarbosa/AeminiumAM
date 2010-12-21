package benchmarks.dictionaryBenchmark;

import examples.dictionaryExample.DictionaryExample;
import examples.dictionaryExample.DictionaryExampleAtomic;
import aeminium.runtime.tools.benchmark.forkjoin.Benchmark;
import aeminium.runtime.tools.benchmark.forkjoin.BenchmarkExecutor;


public class DictionaryBenchMarkSuite {

	Benchmark[] tests;
	
	DictionaryBenchMarkSuite(){
		tests = new Benchmark[2];
		
		tests[0] = new Benchmark(){

			@Override
			public String getName() {
				return "PAM";	
			}

			@Override
			public long run() {
				new DictionaryExample(100,500);
				
				long start = System.nanoTime();
				DictionaryExample.reader.sendMessage(null);
				DictionaryExample.art.endAeminiumRuntime();				
				
				return System.nanoTime()-start;
			}
			
		};
		
		tests[1] = new Benchmark(){

			@Override
			public String getName() {
				return "Atomic";
			}

			@Override
			public long run() {
				new DictionaryExampleAtomic(100,500);
				
				long start = System.nanoTime();
				DictionaryExampleAtomic.reader.sendMessage(null);
				DictionaryExampleAtomic.art.endAeminiumRuntime();				
				
				return System.nanoTime()-start;
			}
			
		};
	}
	
	public static void main(String[] args) {
		DictionaryBenchMarkSuite suite = new DictionaryBenchMarkSuite();
		new BenchmarkExecutor(suite.getTests()).run(args);
	}
	
	public Benchmark[] getTests() {
		return tests;
	}
	

}
