package ner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * @author Cedomir Spalevic
 * A Positve/Negative word Recognizer from a text file of Positive and Negative words
 */
public class SentimentRecognizer 
{
	final private String POSITIVE = "POSITIVE";
	final private String NEGATIVE = "NEGATIVE";
	final private String NEGATION = "NEGATION";
	final private String POSITIVE_TXT = "entities/positive_words.txt";
	final private String NEGATIVE_TXT = "entities/negative_words.txt";
	final private String NEGATION_TXT = "entities/negation_words.txt";
	private ArrayList<String> positives;
	private ArrayList<String> negatives;
	private ArrayList<String> negations;
	
	/**
	 * This method builds an array list of positive and negative names from a txt file
	 */
	public void build() throws IOException
	{
		//initialize array list
		positives = new ArrayList<String>();
		negatives = new ArrayList<String>();
		negations = new ArrayList<String>();
		
		//create file objects
		File positive_file = new File(POSITIVE_TXT);
		Scanner positive_scanner = new Scanner(positive_file);
		File negative_file = new File(NEGATIVE_TXT);
		Scanner negative_scanner = new Scanner(negative_file);
		File negation_file = new File(NEGATION_TXT);
		Scanner negation_scanner = new Scanner(negation_file);
		
		//read files
		while(positive_scanner.hasNextLine())
			positives.add(positive_scanner.nextLine()); //add positive word to array list
		while(negative_scanner.hasNextLine())
			negatives.add(negative_scanner.nextLine()); //add negative word to array list
		while(negation_scanner.hasNextLine())
			negations.add(negation_scanner.nextLine()); //add negation word to array list
		
		//close scanner
		positive_scanner.close();
		negative_scanner.close();  
		negation_scanner.close();
	}
	
	/**
	 * This method determines if a given token is a positive, negative or negation word
	 * @param token
	 * @return 'POSITIVE' if the token is a positive word, 
	 * 'NEGATIVE' if the token is a negative word, 
	 * 'NEGATION' if the token is a negation word
	 * or 'O' if it is not any
	 */ 
	public String determineSentiment(String token)
	{
		//convert token to lower case (everything in file was lower case)
		token = token.toLowerCase();
		
		//determine if a token is a positive word
		if(positives.contains(token)) return POSITIVE;
		//determine if a token is a negative word
		if(negatives.contains(token)) return NEGATIVE;
		//determine if a token is a negation word
		if(negations.contains(token)) return NEGATION;
		
		//if not return 'O'
		return "O";
	}
	

}
