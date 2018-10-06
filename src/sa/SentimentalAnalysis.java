package sa;

import model.Sentence;
import model.Word;

/*
 * @author Cedomir Spalevic
 * A Sentimental Analysis class that determines a rating of a text
 */
public class SentimentalAnalysis 
{
	//variable that holds amount of positive words
	private int positives;
	//variable that holds the amount of negative words
	private int negatives;
	//variable that holds whether or not a word is a negation
	boolean negation;
	
	/**
	 * A method that processes a Sentence array and returns a 3 star or 5 star rating
	 * @param sentences
	 * @param rating_within
	 * @return
	 */
	public int process(Sentence[] sentences, int rating_within)
	{
		//set variables
		positives = 0;
		negatives = 0;
		negation = false;
		
		//count the number of positives and negatives
		countPosNeg(sentences);
		
		//if rating_within is 3, run 3 star rating algorithm
		if(rating_within == 3)
			return get3StarRating();
		//if rating_within is 5, run 5 star rating algorithm
		else
			return get5StarRating();
	}
	
	/**
	 * A method that will count the amount of positive and negative words within a Sentence array
	 * @param sentences
	 */
	private void countPosNeg(Sentence[] sentences)
	{
		//for loop to go through Sentence array
		for(Sentence sentence : sentences)
		{
			//for loop to go through Word array
			for(Word word : sentence.getWords())
			{
				//switch statement for different cases of a word sentiment
				switch(word.getSentiment())
				{
					//if word is a negation
					case "NEGATION":
						//if negation is true, set it to false
						if(negation)
							negation = false;
						//if negation is false, set it to true
						else
							negation = true;
						break;
					//if word is negative
					case "NEGATIVE":
						//if there was a negation before this word, then this word is positive
						if(negation) positives++;
						//if not, then this word is negative
						else negatives++;
						negation = false;
						break;
					//if word is positive
					case "POSITIVE":
						//if there was a negation before this word, then this word is negative
						if(negation) negatives++;
						//if not, then this word is positive
						else positives++;
						negation = false;
						break;
				}
			}
		}
	}
	
	/**
	 * A method that takes in the amount of positive and negative words within a sentence and returns a rating between 1 and 5
	 * @param positives
	 * @param negatives
	 * @return 5 star rating
	 */
	private int get5StarRating()
	{
		//if there are no positives, we determine that will produce a 1 star rating
		if(positives==0)
			return 1;
		//if there are no negatives, we determine that will produce a 5 star rating
		if(negatives==0)
			return 5;
		
		//if the amount of positives and negatives are about the same, we determine that will produce a 3 star rating
		int m = Math.abs(positives-negatives);
		if(m <= 2)
			return 3;
		//if there are less than 5 more positives or negatives, we determine that will produce a 4 or 2 star rating
		else if(m < 5)
			return (positives>negatives?4:2);
		//if there are 5 or more positive or negatives, we determine that will produce a 5 or 1 star rating
		else
			return (positives>negatives?5:1);
	}
	
	/**
	 * A method that takes in the amount of positive and negative words within a sentence and returns a rating between -1 and 1
	 * @param positives
	 * @param negatives
	 * @return 3 star rating
	 */
	private int get3StarRating()
	{
		//if the amount of positives and negatives are the same, return 0
		if(positives == negatives)
			return 0;
		//if there are more positives than negatives, return 1
		else if(positives > negatives)
			return 1;
		//if there are more negatives than positives, return -1
		else
			return -1;
	}
}
