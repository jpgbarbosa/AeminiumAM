package benchmarks.dictionaryBenchmark.normal;

import java.util.Hashtable;
import java.util.Map.Entry;

import examples.dictionaryExample.*;

public class SimpleDictSerialBench {
	static int REPETITIONS = 30;

	static int REQUESTS = 1;

	public static long getAvg(Hashtable<Integer, Long> ht) {

		long avg = 0;
		int ctr = 0;

		for (Entry<Integer, Long> e : ht.entrySet()) {
			ctr++;

			avg += e.getValue();
		}

		return (avg / ctr);
	}

	public static void main(String[] args) {
		long workTime = 0;
		int i;

		for (int j = 500; j <= 500; j += 100) {
			long subtotal = 0;
			long rqI = 0, dI = 0, rcI = 0, rqO = 0, dO = 0, rcO = 0, rqW = 0, dW = 0, rcW = 0;

			System.out.println();
			System.out.println(j);

			for (i = 0; i < REPETITIONS; i++) {
				DictionaryExampleAtomic.rt = aeminium.runtime.implementations.Factory
						.getRuntime();
				DictionaryExampleAtomic.rt.init();

				new DictionaryExampleAtomic(REQUESTS, 500, true, workTime);

				long start = System.nanoTime();
				DictionaryExampleAtomic.reader.startAsking(start);
				DictionaryExampleAtomic.rt.shutdown();

				subtotal += System.nanoTime() - start;

				rqI += DictionaryExampleAtomic.reader.insideSideLock;
				dI += DictionaryExampleAtomic.dictionary.insideSideLock;
				rcI += DictionaryExampleAtomic.receiver.insideSideLock;

				rqO += DictionaryExampleAtomic.reader.outSideLock;
				dO += DictionaryExampleAtomic.dictionary.outSideLock;
				rcO += DictionaryExampleAtomic.receiver.outSideLock;

				rqW += DictionaryExampleAtomic.reader.total;
				dW += DictionaryExampleAtomic.dictionary.total;
				rcW += DictionaryExampleAtomic.receiver.total;

			}
			System.out.println("Repetitions " + i);

			System.out.println("Global time (avg): " + subtotal / i);

			System.out.println();
			System.out.println("__inLock__");
			System.out.println("Actor\ttotal\tavg\tn");
			System.out.println("Reader: " + (rqI / i) + " "
					+ ((rqI / i) / DictionaryExampleAtomic.reader.ctr) + " "
					+ DictionaryExampleAtomic.reader.ctr);
			System.out.println("Dict: \t" + (dI / i) + " "
					+ ((dI / i) / DictionaryExampleAtomic.dictionary.ctr) + " "
					+ DictionaryExampleAtomic.dictionary.ctr);
			System.out.println("Receiver: " + (rcI / i) + " "
					+ ((rcI / i) / DictionaryExampleAtomic.receiver.ctr) + " "
					+ DictionaryExampleAtomic.receiver.ctr);

			System.out.println();
			System.out.println("__outLock__");
			System.out.println("Actor\ttotal\tavg\tn");
			System.out.println("Reader: " + (rqO / i) + " "
					+ ((rqO / i) / DictionaryExampleAtomic.reader.ctr) + " "
					+ DictionaryExampleAtomic.reader.ctr);
			System.out.println("Dict: \t" + (dO / i) + " "
					+ ((dO / i) / DictionaryExampleAtomic.dictionary.ctr) + " "
					+ DictionaryExampleAtomic.dictionary.ctr);
			System.out.println("Receiver: " + (rcO / i) + " "
					+ ((rcO / i) / DictionaryExampleAtomic.receiver.ctr) + " "
					+ DictionaryExampleAtomic.receiver.ctr);

			System.out.println();
			System.out.println("__work__");
			System.out.println("Actor\ttotal\tavg\tn");
			System.out.println("Reader: " + (rqW / i) + " "
					+ ((rqW / i) / DictionaryExampleAtomic.reader.ctr) + " "
					+ DictionaryExampleAtomic.reader.ctr);
			System.out.println("Dict: \t" + (dW / i) + " "
					+ ((dW / i) / DictionaryExampleAtomic.dictionary.ctr) + " "
					+ DictionaryExampleAtomic.dictionary.ctr);
			System.out.println("Receiver: " + (rcW / i) + " "
					+ ((rcW / i) / DictionaryExampleAtomic.receiver.ctr) + " "
					+ DictionaryExampleAtomic.receiver.ctr);

			if(REQUESTS>1){
				System.out.println();
				System.out.println("__Hash__");
				System.out.println("Actor\tavg\t1st\t2nd");
				System.out.println("Reader: " + " "
						+ getAvg(DictionaryExampleAtomic.reader.mapActor) + " "
						+ DictionaryExampleAtomic.reader.mapActor.get(1));
				System.out.println("Dict: " + " "
						+ getAvg(DictionaryExampleAtomic.dictionary.mapActor) + " "
						+ DictionaryExampleAtomic.dictionary.mapActor.get(1) + " "
						+ DictionaryExampleAtomic.dictionary.mapActor.get(2));
				System.out.println("Receiver: " + " "
						+ getAvg(DictionaryExampleAtomic.receiver.mapActor) + " "
						+ DictionaryExampleAtomic.receiver.mapActor.get(1) + " "
						+ DictionaryExampleAtomic.receiver.mapActor.get(2));
			} else {
				System.out.println();
				System.out.println("__Hash__");
				System.out.println("Actor\tavg\t1st");
				System.out.println("Reader: " + " "
						+ getAvg(DictionaryExampleAtomic.reader.mapActor) + " "
						+ DictionaryExampleAtomic.reader.mapActor.get(1));
				System.out.println("Dict: " + " "
						+ getAvg(DictionaryExampleAtomic.dictionary.mapActor) + " "
						+ DictionaryExampleAtomic.dictionary.mapActor.get(1));
				System.out.println("Receiver: " + " "
						+ getAvg(DictionaryExampleAtomic.receiver.mapActor) + " "
						+ DictionaryExampleAtomic.receiver.mapActor.get(1));
			}
		}
	}
}
