package examples.dictionaryExample;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

import actor.Actor;
import actor.annotations.*;
import aeminium.runtime.Body;
import aeminium.runtime.Hints;
import aeminium.runtime.Runtime;
import aeminium.runtime.Task;

public class DictionaryExample {
	public static boolean useSpin = false;
	public static long workTime;
	public static int noMsgs;
	public static int noMsgsRead;
	
	public static boolean useAeTasks = true; 
	
	public static Dictionary dictionary;
	public static Reader reader;
	public static Receiver receiver;
	public static Runtime rt;
	
	public DictionaryExample(int num, int num2,boolean useSpin, long workTime){		
		DictionaryExample.useSpin = useSpin;
		DictionaryExample.workTime = workTime;
		noMsgsRead = num;
		noMsgs = num2;
		reader = new Reader(rt);
		receiver = new Receiver(rt);
		dictionary = new Dictionary(rt);
	}
	
	public static class Reader extends Actor{
		private String [] words;
		
		public Reader(Runtime rt){
			super();
			this.rt = rt;
			
			if(useAeTasks){
				Task t = rt.createNonBlockingTask(new Body() {
					@Override
					public void execute(Runtime rt, Task current) throws Exception {
						consumer.run();
					}
	
					@Override
					public String toString() {
						return "Consumer";
					}
				}, Hints.NO_HINTS);
				
				this.rt.schedule(t, Runtime.NO_PARENT, Runtime.NO_DEPS);
			}
			
			words = new String[noMsgs];
			
			try {
				FileInputStream fstream = new FileInputStream("WordsSorted.txt");
			    DataInputStream in = new DataInputStream(fstream);
			    BufferedReader br = new BufferedReader(new InputStreamReader(in));
				
				int index=0;
				String word;
				while (index<noMsgs && (word = br.readLine()) != null){
					words[index] = word;
					index++;
				}
				
				in.close();
				
				Random random = new Random(System.currentTimeMillis());
				int rand;
				String temp;
				
		        for (int i = 0; i < words.length; i++) {
		            rand = (random.nextInt() & 0x7FFFFFFF) % words.length;
		            temp = words[i];
		            words[i] = words[rand];
		            words[rand] = temp;
		        }
				
			
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		@Read
		public void startAsking(Object obj) {			
			if(useSpin){
				long sleepTime = workTime; // convert to nanoseconds
			    long startTime = System.nanoTime();
			    while ((System.nanoTime() - startTime) < sleepTime) {}
			}
			for(int i=0; i<noMsgsRead; i++ ){
				dictionary.getVal(words[i]);
			}
		}

		@Override
		@End
		public void end() {
			//dictionary.end();
		}
		
	}
	
	public static class Dictionary extends Actor{
		private String [] keyWords;
		private String [] valueWords;
		
		public Dictionary(Runtime rt){
			super();
			this.rt = rt;

			if(useAeTasks){
				Task t = rt.createNonBlockingTask(new Body() {
					@Override
					public void execute(Runtime rt, Task current) throws Exception {
						consumer.run();
					}
	
					@Override
					public String toString() {
						return "Consumer";
					}
				}, Hints.NO_HINTS);
				
				this.rt.schedule(t, Runtime.NO_PARENT, Runtime.NO_DEPS);
			}
			
			keyWords = new String[noMsgs];
			valueWords = new String[noMsgs];
			
			try {
				FileInputStream fstream = new FileInputStream("WordsSorted.txt");
			    DataInputStream in = new DataInputStream(fstream);
			    BufferedReader br = new BufferedReader(new InputStreamReader(in));
				
				int index=0;
				String word;
				
				while (index<noMsgs && (word = br.readLine()) != null){
					keyWords[index] = word;
					valueWords[index] = word+"-V";
					index++;
				}
				
				in.close();
			
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}

		@Read
		public void getVal(String word) {
			if(useSpin){
				long sleepTime = workTime; // convert to nanoseconds
			    long startTime = System.nanoTime();
			    while ((System.nanoTime() - startTime) < sleepTime) {}
			}
			//for(int i=0; i<keyWords.length; i++){
				//if(keyWords[i].equals(word)){
					receiver.sendMessage(valueWords[1]);
				//}
			//}			
		}
		
		@Override
		@End
		public void end() {
			//receiver.end();
		}
		
	}
	
	public static class Receiver extends Actor{
		int ctr=0;
		
		public Receiver(Runtime rt) {
			super();
			this.rt = rt;

			if(useAeTasks){
				Task t = rt.createNonBlockingTask(new Body() {
					@Override
					public void execute(Runtime rt, Task current) throws Exception {
						consumer.run();
					}
	
					@Override
					public String toString() {
						return "Consumer";
					}
				}, Hints.NO_HINTS);
				
				this.rt.schedule(t, Runtime.NO_PARENT, Runtime.NO_DEPS);
			}
			
		}

		@Read
		public void sendMessage(String value) {
			ctr++;
			if(useSpin){
				long sleepTime = workTime; // convert to nanoseconds
			    long startTime = System.nanoTime();
			    while ((System.nanoTime() - startTime) < sleepTime) {}
			}
			if(ctr==500)
				System.out.println(value +" "+ctr);	
		}
		
		@Override
		@End
		public void end() {
		}
		
	}

}
