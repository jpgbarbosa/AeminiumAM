package benchmarks.blogServerBenchmark.normal;

import java.util.Random;

import actor.AeminiumRuntime;

import examples.blogserver.Serial.normal.PutRequest;
import examples.blogserver.Serial.normal.ReadPost;
import examples.blogserver.Serial.normal.WebSerialNormal;

public class DistSerialBench {
	
	public static void main(String[] args) {
		
		for(int num=10000; num<=100000; num+=10000){
			long total=0;
			
			System.out.println();
			System.out.println(num);
			for(int x=0; x<10; x++){
				WebSerialNormal.art = new AeminiumRuntime();
				
				WebSerialNormal web = new WebSerialNormal(1000);
				
				Random randP = new Random(10);
				Random randMID = new Random((int) (num+0.2*num));
				
				long start = System.nanoTime();
				for(int i = 0; i<num; i++){
					if(randP.nextInt(10)<2){
						web.adder.sendMessage(new PutRequest("Ace","Post gerado na "+i+"iteracao."));
					} else {
						web.reader.sendMessage(new ReadPost(randMID.nextInt(110),"BenchUser"+i));
					}
				}
				WebSerialNormal.art.endAeminiumRuntime();
				total=(System.nanoTime()-start);
				System.out.println(total);
			}
		}
	}
}
