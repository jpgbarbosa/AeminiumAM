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
import aeminium.runtime.Runtime;

public class DictionaryExample {
	public static boolean useSpin = false;
	public static long workTime;
	public static int noMsgs;
	public static int noMsgsRead;
	
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
			words = new String[noMsgs];
			
			try {
				FileInputStream fstream = new FileInputStream("500Word1.txt");
			    DataInputStream in = new DataInputStream(fstream);
			    BufferedReader br = new BufferedReader(new InputStreamReader(in));
				
				int index=0;
				String word;
				while (index<noMsgs && (word = br.readLine()) != null){
					words[index] = word;
					index++;
				}
				
				in.close();
				
				Random random = new Random(System.nanoTime());
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
				long sleepTime = workTime;
			    long startTime = System.nanoTime();
			    while ((System.nanoTime() - startTime) < sleepTime) {}
			}
			for(int i=0; i<noMsgsRead; i++ ){
				dictionary.getVal(words[i]);
			}
		}
		
	}
	
	public static class Dictionary extends Actor{
		private String [] keyWords;
		private String [] valueWords;
		
		public Dictionary(Runtime rt){
			super();
			this.rt = rt;
			keyWords = new String[noMsgs];
			valueWords = new String[noMsgs];
			
			try {
				FileInputStream fstream = new FileInputStream("500Word1.txt");
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
				long sleepTime = workTime;
			    long startTime = System.nanoTime();
			    while ((System.nanoTime() - startTime) < sleepTime) {}
			}
			for(int i=0; i<keyWords.length; i++){
				if(keyWords[i].equals(word)){
					receiver.sendMessage(valueWords[i]);
				}
			}			
		}
		
	}
	
	public static class Receiver extends Actor{
		
		public Receiver(Runtime rt) {
			this.rt = rt;
		}

		@Read
		public void sendMessage(String value) {
			if(useSpin){
				long sleepTime = workTime;
			    long startTime = System.nanoTime();
			    while ((System.nanoTime() - startTime) < sleepTime) {}
			}
		}

	}

}
