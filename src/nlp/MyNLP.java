package nlp;

import ner.MyRecognizer;
import sa.SentimentalAnalysis;

/*
 * @author Cedomir Spalevic
 * A NLP class that runs our library onto a given text
 */
public class MyNLP 
{
	//variable that holds StanfordNLP object
	private StanfordNLP snlp;
	//variable that holds Recognizer object
	private MyRecognizer rec;
	//variable that holds SentimentalAnalysis object
	private SentimentalAnalysis sa;
	
	/**
	 * Constructor that creates NLP objects
	 */
	public MyNLP()
	{
		//create StanfordNLP
		snlp = new StanfordNLP();
		//create Recognizer
		rec = new MyRecognizer();
		//build recognizers
		rec.build();
		//create SentimentalAnalysis
		sa = new SentimentalAnalysis();
	}
	
	/**
	 * A method that runs our NLP project on the given text and returns a 3 star or a 5 star rating
	 * @param text
	 * @param rating_wanted
	 * @return a 3 star or 5 star rating
	 */
	public int process(String text, int rating_wanted)
	{	
		//run Stanford NLP on the text
		snlp.process(text);
		
		//recognize entities within Sentence array
		rec.process(snlp.getSentences());
		
		//return rating determined from sentimental analysis
		return sa.process(snlp.getSentences(), rating_wanted);
	}
	
	/**
	 * This method takes in a text and determines if it has a RH named entity or not
	 * @param text
	 * @return true if the text has a RH named entity, and false if it does not
	 */
	public boolean hasNamedEntity(String text)
	{
		//run Stanford NLP on the text
		snlp.process(text);
		
		//check to see if the text has any named entities
		return rec.hasNamedEntity(snlp.getSentences());
	}
}
