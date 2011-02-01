package benchmarks.blogServerBenchmark;

import java.util.Random;

import unused.AeminiumRuntime;

import examples.blogserver.PAM.WebPam;
import examples.blogserver.Serial.WebSerial;

public class BlogServerBench {
	
	public static void main(String[] args) {
		
		for(int num=10000; num<=100000; num+=10000){
			long total=0;
			
			System.out.println();
			System.out.println(num);
			for(int x=0; x<10; x++){
				
				WebPam web = new WebPam(1000);
				//WebSerial web = new WebSerial(1000);
				
				Random randP = new Random(10);
				Random randMID = new Random((int) (num+0.2*num));
			
				long start = System.nanoTime();
				for(int i = 0; i<num; i++){
					if(randP.nextInt(10)<2){
						web.adder.addMessage("Ace","Post generated at "+i+"iteration.");
					} else {
						web.reader.reqReadPost(randMID.nextInt(110),"BenchUser"+i);
					}
				}
				total=(System.nanoTime()-start);
				System.out.println(total);
			}
		}
	}
}
