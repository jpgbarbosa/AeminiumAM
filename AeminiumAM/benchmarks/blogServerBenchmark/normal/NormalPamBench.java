package benchmarks.blogServerBenchmark.normal;

import java.util.Random;

import actor.AeminiumRuntime;

import examples.blogserver.PAM.normal.PutRequest;
import examples.blogserver.PAM.normal.ReadPost;
import examples.blogserver.PAM.normal.WebPamNormal;

public class NormalPamBench {
	
	public static void main(String[] args) {
		
		for(int num=10000; num<=100000; num+=10000){
			long total=0;
			
			System.out.println();
			System.out.println(num);
			for(int x=0; x<30; x++){
				WebPamNormal.art = new AeminiumRuntime();
				
				WebPamNormal web = new WebPamNormal(1000);
				
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
				WebPamNormal.art.endAeminiumRuntime();
				total=(System.nanoTime()-start);
				System.out.println(total);
			}
		}
	}
}
