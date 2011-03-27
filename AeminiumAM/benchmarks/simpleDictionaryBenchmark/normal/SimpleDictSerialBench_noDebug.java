package benchmarks.simpleDictionaryBenchmark.normal;

import examples.dictionaryExample.DictionaryExampleAtomic_noDebug;

public class SimpleDictSerialBench_noDebug {

	public static void main(String[] args) {
		long workTime = 15000000;

		new DictionaryExampleAtomic_noDebug(500, 500, true, workTime);

		DictionaryExampleAtomic_noDebug.reader.startAsking(null);
		System.out.println("done");
	}
}
