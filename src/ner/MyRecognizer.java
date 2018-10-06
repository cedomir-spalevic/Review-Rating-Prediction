package ner;

import java.io.IOException;

import model.Sentence;
import model.Word;

/*
 * @author Cedomir Spalevic
 * A Recognizer class that processes a sentence recognizes a Sentiment and/or a RH entity
 */
public class MyRecognizer 
{
	//variable that holds RH Entity Recognizer
	private RHEntityRecognizer rhRecognizer;
	//variable that holds Sentiment Recognizer
	private SentimentRecognizer sentimentRecognizer;
	
	/**
	 * This method builds the RHEntityRecognizer and SentimentRecognizer lists 
	 */
	public void build()
	{
		//create Recognizer objects
		rhRecognizer = new RHEntityRecognizer();
		sentimentRecognizer = new SentimentRecognizer();
		
		//build recognizer lists
		try
		{
			rhRecognizer.build();
			sentimentRecognizer.build();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * This method processes a Sentence array and determines if a word is a RH or Sentiment entity
	 * @param sentences
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
			
			//get Word array
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
				//if word is not proper noun
				else
				{
					//determine if word is a sentiment
					String sentiment = sentimentRecognizer.determineSentiment(current_word.getToken());
					//set sentiment
					current_word.setSentiment(sentiment);
					
					//if start is greater than 0, then process sliding window
					if(start >= 0)
					{
						//if end is less than 0, then it is a one word Named Entity
						if(end < 0) end = start;
						
						//variable to hold the possible named entity
						String token = "";
						//for loop to go through sliding window and combine the tokens
						for(int s = start; s < end+1; s++)
							token += words[s].getToken() + (s==end?"":" ");
						
						//check to see if the token is a RH named entity
						String token_ne = rhRecognizer.determineRHEntity(token);
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
		//return Sentence array
		return sentences;
	}
	
	/**
	 * This method tests to see if a Sentence array has a named entity
	 * @param sentence
	 * @return recognition
	 */
	public boolean hasNamedEntity(Sentence[] sentences)
	{
		//set boolean
		boolean has_named_entity = false;
		
		//build RH entity
		RHEntityRecognizer rhRecognizer = new RHEntityRecognizer();
		try
		{
			rhRecognizer.build();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		//for loop to go through Sentence array
		for(Sentence	 sentence : sentences)
		{
			//variable to represent the beginning of a sliding window (if exists)
			int start = -1;
			//variable to represent the ending of a sliding window (if exists)
			int end = -1;
			
			//get Word array
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
				//if word is not proper noun
				else
				{	
					//if start is greater than 0, then process sliding window
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
						String token_ne = rhRecognizer.determineRHEntity(token);
						//if it does not return 'O', then it is a RH entity, so return true
						if(!token_ne.equals("O"))
							return true;
						
						//set start and end back to -1
						start = -1;
						end = -1;
					}
				}
			}
		}
		
		//return boolean
		return has_named_entity;
	}
}
