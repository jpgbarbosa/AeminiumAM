package main;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import actor.Actor;
import actor.AeminiumRuntime;

public class DictionaryExample {
	
	public static Dictionary dictionary;
	public static Reader reader;
	public static Receiver receiver;
	
	private static class Lock{
		
	}
	
	private static class Message{
		
		public Reader reader;
		public String word;
		
		Message(Reader reader , String word){
			this.reader = reader;
			this.word = word;
		}
	}
	
	private static class Reader extends Actor{

		static public String [] words;
		
		Reader(){
			super();
			
			words = new String[500];
			
			try {
				FileInputStream fstream = new FileInputStream("500Word1.txt");
			    DataInputStream in = new DataInputStream(fstream);
			    BufferedReader br = new BufferedReader(new InputStreamReader(in));
				
				int index=0;
				String word;
				while ((word = br.readLine()) != null){
					words[index] = word;
					index++;
				}
				
				in.close();
			
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		@Override
		protected void react(Object obj) {
			for(int i=0; i<words.length; i++ ){
				dictionary.sendMessage(new Message(this,words[i]));
			}
		}
		
	}
	
	private static class Dictionary extends Actor{

		static public String [] keyWords;
		static public String [] valueWords;
		
		Dictionary(){
			super();
			
			keyWords = new String[500];
			valueWords = new String[500];
			
			try {
				FileInputStream fstream = new FileInputStream("500Word1.txt");
			    DataInputStream in = new DataInputStream(fstream);
			    BufferedReader br = new BufferedReader(new InputStreamReader(in));
				
				int index=0;
				String word;
				while ((word = br.readLine()) != null){
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
		
		@Override
		protected void react(Object obj) {
			
			String word = ((Message)obj).word;
			
			for(int i=0; i<keyWords.length; i++){
				if(keyWords.equals(word)){
					receiver.sendMessage(valueWords[i]);
				}
			}
						
		}
		
	}
	
	private static class Receiver extends Actor{

		@Override
		protected void react(Object obj) {
			System.out.println((String)obj);
		}
		
	}
	
	static AeminiumRuntime art = new AeminiumRuntime();

	public static void main(String[] args) {
		
		reader = new Reader();
		receiver = new Receiver();
		dictionary = new Dictionary();
		
		reader.sendMessage(null);

	}

}
