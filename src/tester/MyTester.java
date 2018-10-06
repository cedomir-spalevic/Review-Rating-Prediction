package tester;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Scanner;
import model.Review;
import nlp.MyNLP;

/*
 * @author Cedomir Spalevic
 * A Tester class that can go through sample review data and compare the original rating to our sentimental analysis rating
 */
public class MyTester 
{
	//variable that holds total reviews
	int total;
	//variable that holds amount of reviews we got correct
	int correct;
	//variable that holds amount of reviews we got within one
	int within_one;
	
	/**
	 * A method that test our NLP 3 star rating on the Google Review data that we gave a score on
	 */
	public void test3Star()
	{
		System.out.println("Testing 3 star rating...");
		
		//set variables
		total = 0;
		correct = 0;
		within_one = 0;
		
		//read 3 star rating folder
		read3StarFolder("testing/3star");
		
		//print results
		printResults();
	}
	
	/**
	 * A method that tests our NLP 5 star rating on the Google Review data 
	 */
	public void test5Star()
	{
		System.out.println("Testing 5 star rating...");
		
		//set variables
		total = 0;
		correct = 0;
		within_one = 0;
		
		//read 5 star rating folder
		read5StarFolder("testing/5star/hotels");
		read5StarFolder("testing/5star/restaurants");
		
		//print results
		printResults();
	}
	
	/**
	 * This method prints out our results
	 */
	private void printResults()
	{
		//at the end of the testing, display the data
		//get percentages
		double correct_percentage = ((double) correct/total)*100;
		double within_one_percentage = ((double) within_one/total)*100;
		//round to two decimal places
		DecimalFormat df = new DecimalFormat("#.##");
		
		//print output
		System.out.println("Done!");
		System.out.println("Total amount of reviews = " + total);
		System.out.println("Amount we got correct = " + correct + " (" + df.format(correct_percentage) + "%)");
		System.out.println("Amount we got within one = " + within_one + " (" + df.format(within_one_percentage) + "%)");
	}
	
	/**
	 * This method reads the txt files and runs our NLP 5 star rating
	 * @param foldername
	 */
	private void read5StarFolder(String foldername)
	{
		//creating our nlp library
		MyNLP mynlp = new MyNLP();
		
		//create file object
		File folder = new File(foldername);
		
		//get txt files from folder
		File[] files = folder.listFiles();
		
		//go through these txt files
		for(File file : files)
		{
			//create scanner object
			Scanner scanner = null;
			try
			{
				scanner = new Scanner(file);
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			
			System.out.println("Reviewing " + file.getName());
			
			//each review has company name, author, rating, time and text in order
			//read each line individually and create review object
			String name = "";
			String author = "";
			int rating = 0;
			long time = 0;
			String text = "";
			
			//first line in the txt file has the company name
			if(scanner.hasNextLine()) 
				name = scanner.nextLine();
			
			//go through txt file
			String line;
			while(scanner.hasNextLine())
			{	
				//get author
				line = scanner.nextLine();
				author = line.substring("Author: ".length());
				
				//get rating
				line = scanner.nextLine();
				rating = Integer.parseInt(line.charAt(line.length()-1)+"");
				
				//get time
				line = scanner.nextLine();
				time = Long.parseLong(line.substring("Time: ".length()));
				
				//get text
				scanner.nextLine(); //skip a line
				line = scanner.nextLine();
				while(!line.equals("-"))
				{
					text += line;
					line = scanner.nextLine();
				}
				
				//create review object
				Review review = new Review(name, author, rating, time, text);
				
				//run our library on the text and get the rating
				int our_rating = mynlp.process(review.getText(), 5);
				
				//if we got the correct rating, increment correct
				if(our_rating == rating)
					correct++;
				//if we got within one, increment within_one
				else if(our_rating == (rating+1) || our_rating == (rating-1))
					within_one++;
				
				//increment total
				total++;
			}
			
			//close scanner
			scanner.close();
		}
	}
	
	/**
	 * This method reads the txt files and runs our NLP 5 star rating
	 * @param foldername
	 */
	private void read3StarFolder(String foldername)
	{
		//creating our nlp library
		MyNLP mynlp = new MyNLP();
		
		//create file object
		File folder = new File(foldername);
		
		//get txt files from folder
		File[] files = folder.listFiles();
		
		//go through these txt files
		for(File file : files)
		{
			//create scanner object
			Scanner scanner = null;
			try
			{
				scanner = new Scanner(file);
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			
			System.out.println("Reviewing " + file.getName());
			
			//each line has a rating and then the text
			//+1 for a positive sentence, -1 for a negative sentence, and zero for a neutral sentence
			//go through txt file
			String line;
			//first line is the name of the file
			if(scanner.hasNextLine())
				line = scanner.nextLine();
			while(scanner.hasNextLine())
			{
				//get line
				line = scanner.nextLine();
				
				//set variables
				String text;
				int rating;
				
				//if first character is 0, we know rating is neutral
				if(line.charAt(0) == '0') 
				{
					//set rating to 0
					rating = 0;
					//get text
					text = line.substring(2);
				}
				else if(line.charAt(0) == '-')
				{
					//set rating to -1
					rating = -1;
					//get text
					text = line.substring(3);
				}
				else
				{
					//set rating to 1
					rating = 1;
					//get text
					text = line.substring(3);
				}
				//run our library on the text and get the rating
				int our_rating = mynlp.process(text,3);
				
				//if we got the correct rating, increment correct
				if(our_rating == rating)
					correct++;
				//if we got within one, increment within_one
				else if(our_rating == (rating+1) || our_rating == (rating-1))
					within_one++;
				
				//increment total
				total++;
			}
			
			//close scanner
			scanner.close();
		}
	}
}
