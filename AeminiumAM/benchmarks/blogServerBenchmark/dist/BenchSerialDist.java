package benchmarks.blogServerBenchmark.dist;

import java.util.Random;
import examples.blogserver.dist.Serial.WebSerial;

public class BenchSerialDist {
	
	public static void main(String[] args) {
		
		for(int num=10000; num<=100000; num+=10000){
			long total=0;
			
			System.out.println();
			System.out.println(num);
			for(int x=0; x<30; x++){
				WebSerial.rt = aeminium.runtime.implementations.Factory.getRuntime();
				WebSerial.rt.init();
				
				WebSerial web = new WebSerial(1000);
				
				Random randP = new Random(10);
				Random randMID = new Random((int) (num+0.2*num));
				Random ran = new Random (2);
			
				long start = System.nanoTime();
				for(int i = 0; i<num; i++){
					if(randP.nextInt(10)<2){
						web.addActorArray[ran.nextInt(web.numCopies)].addMessage("Ace","Post gerado na "+i+"iteracao.");
					} else {
						web.readersArray[ran.nextInt(web.numCopies)].reqReadPost(randMID.nextInt(110),"BenchUser"+i);
					}
				}
				WebSerial.rt.shutdown();
				total=(System.nanoTime()-start);
				System.out.println(total);
			}
		}
	}
}
