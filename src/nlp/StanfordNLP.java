package nlp;

import model.Sentence;
import model.Word;
import java.util.*;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.ling.CoreAnnotations.*;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.*;

/*
 * @author Cedomir Spalevic
 * A NLP class that uses the Stanford NLP API to analyze text 
 */
public class StanfordNLP 
{
	//variable that holds the StanfordCoreNLP object
	private StanfordCoreNLP pipeline;
	//variable that holds an array of Sentence objects
	private Sentence[] sentences;

	/**
	 * Constructor that initializes StanfordCoreNLP
	 */
	public StanfordNLP()
	{
		//building StanfordCoreNLP
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref, sentiment");
		pipeline = new StanfordCoreNLP(props);
	}
	
	/**
	 * This method processes the given text and generates an array of Sentence objects
	 * @param text
	 */
	public void process(String text)
	{
		//creating Annotation object with the given text
		Annotation document = new Annotation(text);
		//running all Annotators on the text
		pipeline.annotate(document);
		
		//getting all the sentences in the text
		List<CoreMap> sentencesAnnotation = document.get(SentencesAnnotation.class);
		
		//initializing array of Sentence objects
		sentences = new Sentence[sentencesAnnotation.size()];
		//counter for sentences
		int sentenceCount = 0;
		
		//for loop to go through each sentence
		for(CoreMap currentSentence : sentencesAnnotation)
		{
			//getting sentence text
			String sentenceText = currentSentence.get(TextAnnotation.class);
			
			//getting all the words in the sentence
			List<CoreLabel> tokensAnnotation = currentSentence.get(TokensAnnotation.class);
			
			//getting sentence Parse Tree
			Tree tree = currentSentence.get(TreeAnnotation.class);
			
			//initializing array of Word objects
			Word[] words = new Word[tokensAnnotation.size()];
			//counter for words
			int wordCount = 0;
			
			//for loop to go through each word in a sentence
			for(CoreLabel currentToken : tokensAnnotation)
			{
				//getting the word
				String token = currentToken.get(TextAnnotation.class);
				//getting the word's Named Entity tag
				String namedEntity = currentToken.getString(NamedEntityTagAnnotation.class);
				//getting the word's Part of Speech tag
				String pos = currentToken.get(PartOfSpeechAnnotation.class);
				
				//creating a Word object
				Word word = new Word(token, pos, "O", namedEntity);
				
				//adding the Word object to the list of words
				words[wordCount] = word;
				//increment counter for words
				wordCount++;
			}
			
			//creating Sentence object
			Sentence sentence = new Sentence(sentenceText, words, tree);
			//adding Sentence object to array
			sentences[sentenceCount] = sentence;
			//increment counter for sentences
			sentenceCount++;
		}
	}
	
	/**
	 * @return sentence array
	 */
	public Sentence[] getSentences()
	{
		return sentences;
	}

	/**
	 * Setting a new Sentence array
	 * @param sentences
	 */
	public void setSentences(Sentence[] sentences)
	{
		this.sentences = sentences;
	}
}
