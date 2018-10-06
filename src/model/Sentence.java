package model;

import edu.stanford.nlp.trees.*;

/*
 * @author Cedomir Spalevic
 * A model class that represents a sentence and its Parse Tree
 */
public class Sentence 
{
	//variable that holds the sentence
	private String sentence;
	//variable that holds an array of Word objects for the sentence
	private Word[] words;
	//variable that holds the sentence Parse Tree
	private Tree tree;
	
	/**
	 * @param sentence
	 * @param tree
	 */
	public Sentence(String sentence, Word[] words, Tree tree)
	{
		this.sentence = sentence;
		this.words = words;
		this.tree = tree;
	}
	
	/**
	 * @return sentence string
	 */
	public String getSentence()
	{
		return sentence;
	}
	
	/**
	 * Setting a new word
	 * @param sentence
	 */
	public void setSentence(String sentence)
	{
		this.sentence = sentence;
	}
	
	/**
	 * @return word array
	 */
	public Word[] getWords()
	{
		return words;
	}
	
	/**
	 * Setting a new Word array
	 * @param word
	 */
	public void setWords(Word[] words)
	{
		this.words = words;
	}
	
	/**
	 * @return parse tree
	 */
	public Tree getTree()
	{
		return tree;
	}
	
	/**
	 * Setting a new Parse Tree
	 * @param tree
	 */
	public void setTree(Tree tree)
	{
		this.tree = tree;
	}
	
	/**
	 * Print Parse Tree
	 */
	public void printTree()
	{
		tree.pennPrint();
	}
}
