package benchmarks.blogServerBenchmark;

import java.util.Random;

import actor.AeminiumRuntime;

import examples.blogserver.PutRequest;
import examples.blogserver.ReadPost;
import examples.blogserver.Web;

public class PAM {
	
	public static void main(String[] args) {
		
		for(int num=500; num<=5000; num+=500){
			long total=0;
			
			for(int x=0; x<15; x++){
				Web.art = new AeminiumRuntime();
				
				Web web = new Web(1000);
				
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
				Web.art.endAeminiumRuntime();
				total+=(System.nanoTime()-start);
			}
			
			System.out.println(total/20);
		}
	}
}
