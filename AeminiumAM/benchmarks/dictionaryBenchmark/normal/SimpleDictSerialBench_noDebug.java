package benchmarks.dictionaryBenchmark.normal;

import examples.dictionaryExample.DictionaryExampleAtomic_noDebug;

public class SimpleDictSerialBench_noDebug {

	public static void main(String[] args) {
		long workTime = 0;

		new DictionaryExampleAtomic_noDebug(1, 500, false, workTime);

		DictionaryExampleAtomic_noDebug.reader.startAsking(null);
		System.out.println("done");
	}
}
