package model;

/*
 * @author Cedomir Spalevic
 * A model class that represents a review
 */
public class Review 
{
	//variable that holds company name
	private String name;
	//variable that holds author of the review
	private String author;
	//variable that holds the rating
	private int rating;
	//variable that holds the time it was submitted
	private long time;
	//variable that holds the review text
	private String text;
	
	
	/**
	 * @param name
	 * @param author
	 * @param rating
	 * @param time
	 * @param text
	 */
	public Review(String name, String author, int rating, long time, String text)
	{
		this.name = name;
		this.author = author;
		this.rating = rating;
		this.time = time;
		this.text = text;
	}
	
	/**
	 * @return company name
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Setting a new company name
	 * @param name
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	
	/**
	 * @return author of review
	 */
	public String getAuthor()
	{
		return author;
	}
	
	/**
	 * Setting a new author
	 * @param author
	 */
	public void setAuthor(String author)
	{
		this.author = author;
	}
	
	/**
	 * @return rating of review
	 */
	public int getRating()
	{
		return rating;
	}
	
	/**
	 * Setting a new rating
	 * @param rating
	 */
	public void setRating(int rating)
	{
		this.rating = rating;
	}
	
	/**
	 * @return time review was submitted
	 */
	public long getTime()
	{
		return time;
	}
	
	/**
	 * Setting a new time
	 * @param time
	 */
	public void setTime(long time)
	{
		this.time = time;
	}
	
	/**
	 * @return text of review
	 */
	public String getText()
	{
		return text;
	}
	
	/**
	 * Setting a new text
	 * @param text
	 */
	public void setText(String text)
	{
		this.text = text;
	}
}
