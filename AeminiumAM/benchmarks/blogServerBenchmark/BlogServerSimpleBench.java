package benchmarks.blogServerBenchmark;

import java.util.Random;

import actor.AeminiumRuntime;

import examples.blogserver.PAM.dist.PutRequest;
import examples.blogserver.PAM.dist.ReadPost;
import examples.blogserver.PAM.dist.WebPamDist;
import examples.blogserver.Serial.normal.WebSerialNormal;

public class BlogServerSimpleBench {
	
	public static void main(String[] args) {
		
		for(int num=10000; num<=100000; num+=10000){
			long total=0;
			
			System.out.println();
			System.out.println(num);
			for(int x=0; x<10; x++){
				WebPamDist.art = new AeminiumRuntime();
				
				WebPamDist web = new WebPamDist(1000);
				//WebSerial web = new WebSerial(1000);
				
				Random randP = new Random(10);
				Random randMID = new Random((int) (num+0.2*num));
				Random ran = new Random(15);
				
				long start = System.nanoTime();
				for(int i = 0; i<num; i++){
					if(randP.nextInt(10)<2){
						web.addActorArray[ran.nextInt(web.numCopies)].sendMessage(new PutRequest("Ace","Post gerado na "+i+"iteracao."));
					} else {
						web.readersArray[ran.nextInt(web.numCopies)].sendMessage(new ReadPost(randMID.nextInt(110),"BenchUser"+i));
					}
				}
				WebPamDist.art.endAeminiumRuntime();
				total=(System.nanoTime()-start);
				System.out.println(total);
			}
		}
	}
}
