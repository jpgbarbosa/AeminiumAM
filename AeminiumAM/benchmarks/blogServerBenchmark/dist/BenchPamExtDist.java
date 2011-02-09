package benchmarks.blogServerBenchmark.dist;

import java.util.Random;

import examples.blogserver.dist.PAM.real.WebPam;

public class BenchPamExtDist {
	
	public static void main(String[] args) {
		
		for(int num=100000; num<=100000; num+=10000){
			long total=0;
			
			System.out.println();
			System.out.println(num);
			for(int x=0; x<15; x++){
				WebPam.rt = aeminium.runtime.implementations.Factory.getRuntime();
				WebPam.rt.init();
				
				WebPam web = new WebPam(3,200000,1000,true);
				
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
				WebPam.rt.shutdown();
				total=(System.nanoTime()-start);
				System.out.println(total);
			}
		}
	}
}
