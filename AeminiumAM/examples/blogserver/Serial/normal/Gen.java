package examples.blogserver.Serial.normal;

import java.util.*;

public class Gen {
	int num;
	
	String [] sentences;
	
	public Gen(int num){
		sentences = new String[num];
		
		Random randomNumber = new Random();// random number generator
		// variables hold words to be chosen by program
		String article1 = "A";
		String article2 = "The";
		String article3 = "a";
		String article4 = "the";
		String noun1 = "man";
		String noun2 = "doctor";
		String noun3 = "nurse";
		String noun4 = "firefighter";
		String noun5 = "cat";
		String noun6 = "mouse";
		String noun7 = "goat";
		String nounA = "rock";
		String nounB = "rules";
		String nounC = "wall";
		String nounD = "stick";
		String nounE = "ditch";
		String nounF = "bicycle";
		String verb1 = "tested";
		String verb2 = "recited";
		String verb3 = "disregarded";
		String verb4 = "leaped";
		String verb5 = "cracked";
		String verb6 = "followed";
		String verb7 = "broke";
		// sets up variable for final sentence
		String finalSentence = " ";
		
		for (int i = 0; i <num; i++) {
			switch (1 + randomNumber.nextInt(2))// generates numbers 1-2
			{
			case 1:
				finalSentence = article1 + " ";
				break;
			case 2:
				finalSentence = article2 + " ";
			}
			switch (1 + randomNumber.nextInt(7))// generates numbers 1-7
			{
			case 1:
				finalSentence += noun1 + " ";
				break;
			case 2:
				finalSentence += noun2 + " ";
				break;
			case 3:
				finalSentence += noun3 + " ";
				break;
			case 4:
				finalSentence += noun4 + " ";
				break;
			case 5:
				finalSentence += noun5 + " ";
				break;
			case 6:
				finalSentence += noun6 + " ";
				break;
			case 7:
				finalSentence += noun7 + " ";
			}
			switch (1 + randomNumber.nextInt(7))// generates numbers 1-7
			{
			case 1:
				finalSentence += verb1 + " ";
				break;
			case 2:
				finalSentence += verb2 + " ";
				break;
			case 3:
				finalSentence += verb3 + " ";
				break;
			case 4:
				finalSentence += verb4 + " ";
				break;
			case 5:
				finalSentence += verb5 + " ";
				break;
			case 6:
				finalSentence += verb6 + " ";
				break;
			case 7:
				finalSentence += verb7 + " ";
			}
			switch (1 + randomNumber.nextInt(2))// generates numbers 1-3
			{
			case 1:
				finalSentence += article3 + " ";
				break;
			case 2:
				finalSentence += article4 + " ";
			}
			switch (1 + randomNumber.nextInt(6))// generates numbers 1-6
			{
			case 1:
				finalSentence += nounA + ".";
				break;
			case 2:
				finalSentence += nounB + ".";
				break;
			case 3:
				finalSentence += nounC + ".";
				break;
			case 4:
				finalSentence += nounD + ".";
				break;
			case 5:
				finalSentence += nounE + ".";
				break;
			case 6:
				finalSentence += nounF + ".";
			}
			sentences[i]=finalSentence;// prints sentence
															// number, and
															// formatted
															// sentence output
		}// end for
	}
	
	public String[] getSentences(){
		return sentences;
	}
	
	public static void main(String args[]) {
		Gen g = new Gen(10);
		
		System.out.println(g.sentences[4]+" || "+ g.sentences[9]);
	}// end main
}// end clas