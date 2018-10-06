package ner;

import model.Sentence;
import model.Word;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * @author Cedomir Spalevic
 * A Restaurant/Hotel Entity Recognizer from a text file of hotel and restaurant names
 */
public class RHEntityRecognizer 
{
	final private String HOTEL = "HOTEL";
	final private String RESTAURANT = "RESTAURANT";
	final private String HOTELS_TXT = "entities/hotel_names.txt";
	final private String RESTAURANTS_TXT = "entities/restaurant_names.txt";
	private ArrayList<String> hotels;
	private ArrayList<String> restaurants;
	
	/**
	 * This method builds an array list of hotel and restaurant names from a txt file
	 */
	public void build() throws IOException
	{
		//initialize array list
		hotels = new ArrayList<String>();
		restaurants = new ArrayList<String>();
		
		//create file objects
		File hotel_file = new File(HOTELS_TXT);
		Scanner hotel_scanner = new Scanner(hotel_file);
		File restaurant_file = new File(RESTAURANTS_TXT);
		Scanner restaurant_scanner = new Scanner(restaurant_file);
		
		//read files
		while(hotel_scanner.hasNextLine())
			hotels.add(hotel_scanner.nextLine()); //add hotel to array list
		while(restaurant_scanner.hasNextLine())
			restaurants.add(restaurant_scanner.nextLine()); //add restaurant to array list
		
		//close scanner
		hotel_scanner.close();
		restaurant_scanner.close();
	}
	
	/**
	 * This method processes a Sentence array given from the Stanford NLP, and determine (and modify) if there are any RH entities
	 * @param sentences
	 * @return sentences
	 */
	public Sentence[] process(Sentence[] sentences)
	{
		//for loop to go through Sentence array
		for(Sentence sentence : sentences)
		{
			//variable to represent the beginning of a sliding window (if exists)
			int start = -1;
			//variable to represent the ending of a sliding window (if exists)
			int end = -1;
			
			//get array of words
			Word[] words = sentence.getWords();
			
			//for loop to go through Word array
			for(int i = 0; i < words.length; i++)
			{
				//get current Word object
				Word current_word = words[i];
				
				//if current word is a proper noun and is not a named entity
				if(current_word.getPos().equals("NNP"))
				{
					//if start is less than 0, then it is the beginning of the sliding window
					if(start < 0) start = i;
					//if start is greater than 0, then it could be ending of the sliding window
					else end = i;
				}
				//if current word is not a proper noun
				else
				{
					//if start is greater than 0, then process the text
					if(start > 0)
					{
						//if end is less than 0, then it is a one word Named Entity
						if(end < 0) end = start;
						
						//variable to hold the possible named entity
						String token = "";
						//for loop to go through sliding window and combine the tokens
						for(int s = start; s < end+1; s++)
							token += words[s].getToken() + (s==end?"":" ");
						
						//check to see if the token is a RH named entity
						String token_ne = determineRHEntity(token);
						//if it does not return 'O', then it is a RH named entity
						if(!token_ne.equals("O"))
						{
							//for loop to go through sliding window again and set to correct Named Entities
							for(int s = start; s < end+1; s++)
								words[s].setNamedEntity(token_ne);
						}
						
						//set start and end back to -1
						start = -1;
						end = -1;
					}
				}
			}
		}
		return sentences;
	}
	
	/**
	 * This method determines if a given token is a restaurant or hotel
	 * @param token
	 * @return 'RESTAURANT' if the token is a restaurant, 'HOTEL' if the token is a hotel, or 'O' if it is neither
	 */ 
	public String determineRHEntity(String token)
	{
		//convert token to lower case (everything in file was lower case)
		token = token.toLowerCase();
		
		//determine if token is a restaurant
		if(restaurants.contains(token)) return RESTAURANT;
		//determine if token is a hotel
		if(hotels.contains(token)) return HOTEL;
		
		//if not return 'O'
		return "O";
	}
	
}
