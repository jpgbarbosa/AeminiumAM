package dictionaryBenchmark;

import dictionaryExample.DictionaryExample;
import dictionaryExample.DictionaryExampleAtomic;
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
				DictionaryExample de = new DictionaryExample();
				
				long start = System.nanoTime();
				de.reader.sendMessage(null);
				de.art.endAeminiumRuntime();				
				
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
				DictionaryExampleAtomic de = new DictionaryExampleAtomic();
				
				long start = System.nanoTime();
				de.reader.sendMessage(null);
				de.art.endAeminiumRuntime();				
				
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
