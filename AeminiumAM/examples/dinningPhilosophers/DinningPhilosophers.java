/*
package dinningPhilosophers;

import actor.AeminiumRuntime;

public class DinningPhilosophers {
	
	static AeminiumRuntime art = new AeminiumRuntime();

	public static void main(String[] args) {
		
		Fork [] arrayFork = new Fork[5];
		Philosopher [] arrayPhilosopher = new Philosopher[5];
		
		Table table = new Table(arrayPhilosopher,arrayFork);
		
		arrayFork[0] = new Fork("Fork 1");
		arrayFork[1] = new Fork("Fork 2");
		arrayFork[2] = new Fork("Fork 3");
		arrayFork[3] = new Fork("Fork 4");
		arrayFork[4] = new Fork("Fork 5");
		
		arrayPhilosopher[0] = new Philosopher("Phil",table);
		arrayPhilosopher[1] = new Philosopher("Dave", table);
		arrayPhilosopher[2] = new Philosopher("Alice", table);
		arrayPhilosopher[3] = new Philosopher("James", table);
		arrayPhilosopher[4] = new Philosopher("Joe", table);
		
		for(int i=0; i<arrayPhilosopher.length; i++){
			arrayPhilosopher[i].sendMessage(new Reply(false));
		}
		
		art.endAeminiumRuntime();
	}
}
*/

package examples.dinningPhilosophers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import actor.AeminiumRuntime;

public class DinningPhilosophers {
	
	static AeminiumRuntime art;

	public static void main(String[] args) throws IOException {
		

		FileWriter fstream1;
		BufferedWriter out = null;
		
		for(int i=5;i<=45;i+=5){
			fstream1 = new FileWriter(i+" PhilosophersNB.txt");
			out = new BufferedWriter(fstream1);
			
			Fork [] arrayFork = new Fork[i];
			Philosopher [] arrayPhilosopher = new Philosopher[i];
			Table table = new Table(arrayPhilosopher,arrayFork);
			
			try {
				
				FileInputStream fstream = new FileInputStream("Names.txt");
			    DataInputStream in = new DataInputStream(fstream);
			    BufferedReader br = new BufferedReader(new InputStreamReader(in));
				
				int index=0;
				String word;
				while (index<i && (word = br.readLine()) != null){
					arrayPhilosopher[index] = new Philosopher(word,table);
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
				
				var = (System.nanoTime()-start);
				
				out.write(""+var+"\n");
			}
			System.out.println(i+" done");
			out.close();
		}
	}
}
