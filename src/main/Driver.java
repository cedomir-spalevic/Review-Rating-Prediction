package main;

import java.util.Scanner;
import tester.MyTester;
import twitter.MyTwitter;

/*
 * @author Cedomir Spalevic
 * A main class that will provide an interface 
 * You can either test the NLP algorithm on Google Reviews or run it on new Twitter data
 */
public class Driver 
{
	//scanner
	static Scanner sc;
	
	public static void main(String[] args) 
	{
		runMyNLP();
	}
	
	public static void runMyNLP()
	{
		//scanner object to read input
		sc = new Scanner(System.in);
		//print interface
		for(int i = 0; i < 51; i++)
			System.out.print("-"+(i==50?"\n":""));
		System.out.println("\tWelcome to MyNLP project!");
		for(int i = 0; i < 51; i++)
			System.out.print("-"+(i==50?"\n\n":""));
		
		//get input and make sure its correct
		String input = "";
		while(!input.equals("a") && !input.equals("b"))
		{
			System.out.println("What would you like to do? (pick a or b)\n"
					+ "a) Conduct sentimental analysis on sample Google Reviews\n"
					+ "b) Retrieve new tweets from Twitter and conduct sentimental analysis \n");
			input = sc.nextLine().replaceAll("\n","");
		}
		
		//switch statement for input
		switch(input)
		{
			case "a":
				mytester();
				break;
			case "b":
				mytwitter();
				break;
		}
	}
	
	/**
	 * Method that will run MyTester
	 */
	public static void mytester()
	{
		//ask the user what star rating they would like to use
		int rating_input = getRatingInput();
		
		//run tester
		MyTester tester = new MyTester();
		//if user input a, then run 3 star rating
		if(rating_input == 3)
			tester.test3Star();
		//if user input b, then run 5 star rating
		else
			tester.test5Star();
	}
	
	/**
	 * Method that will run MyTwitter
	 */
	public static void mytwitter()
	{
		//get additional information
		//ask the user if they want to filter by a location
		String location = getLocationFilter();
		//ask the user how long they want the program to run for
		int length = getLength();
		//ask the user if they want to filter incoming tweets by if the text contains a named entity
		boolean recognition = getRecognitionFilter();
		//ask the user what star rating they would like to use
		int rating_input = getRatingInput();
		
		
		//run my twitter
		MyTwitter twitter = new MyTwitter();
		twitter.process(location, length, recognition, rating_input);
	}
	
	/**
	 * This method asks the user if they would test a 3 or 5 star rating
	 * @return rating input
	 */
	private static int getRatingInput()
	{
		String input = "";
		//make sure they input a or b
		while(!input.equals("a") && !input.equals("b"))
		{
			//ask user if they would like to test our 3 star rating or our 5 star rating
			System.out.println("Would you like to test our 3 star rating or our 5 star rating (pick a or b)\n"
					+ "a) 3 Star\n"
					+ "b) 5 Star\n");
			//get user input
			input = sc.nextLine().replaceAll("\n","");
		}
		//if user input a, return 3
		if(input.equals("a"))
			return 3;
		//otherwise, return 5
		else
			return 5;
	}
	
	/**
	 * This method asks the user if they would like to filter incoming tweets by location
	 * @return location filter
	 */
	private static String getLocationFilter()
	{
		String input = "";
		//ask the user if they want to filter by a location and make sure its correct
		while(!input.equals("a") && !input.equals("b"))
		{
			System.out.println("Would you like to filter the statuses by a specific location? (pick a or b)\n"
					+ "a) Yes\n"
					+ "b) No\n");
			//get user input
			input = sc.nextLine().replaceAll("\n","");
		}
		
		//if user entered a, ask additional information
		if(input.equals("a"))
		{
			System.out.println("Where would you like to filter by?\n"
					+ "ex: if you would like to filter by Nashville, TN then please enter Nashville, TN\n");
			input = sc.nextLine().replaceAll("\n","");
		}
		else
			input = "-1";
		
		return input;
	}
	
	/**
	 * Ask the user how long they would like the program to run for
	 * @return length
	 */
	private static int getLength()
	{
		String input = "";
		//ask the user how long they would like the program to run for and make sure they entered correctly
		while(true)
		{
			System.out.println("How long would you to run the program for? (pick a number that is 0 < x <= 30)\n");
			input = sc.nextLine().replaceAll("\n","");
			
			//attempt to parse to integer
			try
			{
				int length = Integer.parseInt(input);
				//if the length is correct, break from while loop
				if(length > 0 && length <= 30)
					return length;
			}
			catch(Exception e){}
		}
	}
	
	/**
	 * Ask the user if they would like to filter incoming tweets by name recognition
	 * @return recognition filter
	 */
	private static boolean getRecognitionFilter()
	{
		String input = "";
		//ask user if they would like to filter tweets by name recognition and make sure they enter it correctly
		while(!input.equals("a") && !input.equals("b"))
		{
			System.out.println("Would you like to filter incoming tweets by whether or not the tweet is talking about a hotel or restaurant? (pick a or b)\n"
					+ "a) Yes\n"
					+ "b) No\n");
			//get user input
			input = sc.nextLine().replaceAll("\n","");
		}
		
		//set recognition filter
		if(input.equals("a"))
			return true;
		else
			return false;
	}
}
