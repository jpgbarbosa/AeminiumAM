package benchmarks.dinningPhilosophersSimpleBenchmark;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import examples.dinningPhilosophers.Fork;
import examples.dinningPhilosophers.PhilosopherA;
import examples.dinningPhilosophers.Reply;
import examples.dinningPhilosophers.TableA;

import actor.AeminiumRuntime;

public class DinningPhilosophersA {
	
	static AeminiumRuntime art;

	public static void main(String[] args) throws IOException {
		
		for(int i=5;i<=50;i+=5){
			
			Fork [] arrayFork = new Fork[i];
			PhilosopherA [] arrayPhilosopher = new PhilosopherA[i];
			TableA table = new TableA(arrayPhilosopher,arrayFork);
			
			try {
				
				FileInputStream fstream = new FileInputStream("Names.txt");
			    DataInputStream in = new DataInputStream(fstream);
			    BufferedReader br = new BufferedReader(new InputStreamReader(in));
				
				int index=0;
				String word;
				while (index<i && (word = br.readLine()) != null){
					arrayPhilosopher[index] = new PhilosopherA(word,table);
					arrayFork[index] = new Fork("Fork "+index);
					index++;
				}
				
				in.close();
			
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}		
						
			long var=0;
			
			for(int b=0;b<20;b++){
				art = new AeminiumRuntime();
				
				long start = System.nanoTime();
				
				for(int x=0; x<arrayPhilosopher.length; x++){
					arrayPhilosopher[x].sendMessage(new Reply(false));
				}
								
				art.endAeminiumRuntime();
				
				var += (System.nanoTime()-start);
			}
			System.out.println(var/20);
		}
	}
}
