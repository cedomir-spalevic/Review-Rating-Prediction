package model;

/*
 * @author Cedomir Spalevic
 * A model class that represents a word and all of its NLP components
 */
public class Word 
{
	//variable that holds the word
	private String token;
	//variable that holds the word's Part of Speech tag
	private String pos;
	//variable that holds the word's Sentiment tag
	private String sentiment;
	//variable that holds true if the word has a Sentiment tag and false if not
	private boolean is_sentiment;
	//variable that holds the word's Named Entity tag (if there is one)
	private String named_entity;
	//variable that holds true if the word has a Named Entity tag and false if not
	private boolean is_named_entity;

	/**
	 * @param token
	 * @param pos
	 * @param namedEntity
	 */
	public Word(String token, String pos, String sentiment, String named_entity)
	{
		setToken(token);
		setPos(pos);
		setSentiment(sentiment);
		setNamedEntity(named_entity);
	}

	/**
	 * @return token string
	 */
	public String getToken()
	{
		return token;
	}

	/**
	 * Setting a new word
	 * @param token
	 */
	public void setToken(String token)
	{
		this.token = token;
	}
	
	/**
	 * @return part of speech tag
	 */
	public String getPos()
	{
		return pos;
	}
	
	/**
	 * Setting the word's Part of Speech tag
	 * @param pos
	 */
	public void setPos(String pos)
	{
		this.pos = pos;
	}
	
	/**
	 * @return sentiment tag
	 */
	public String getSentiment()
	{
		return sentiment;
	}
	
	/**
	 * Setting the word's Sentiment tag
	 * @param sentiment
	 */
	public void setSentiment(String sentiment)
	{
		this.sentiment = sentiment;
		//set whether or not this word is a sentiment
		if(sentiment.equals("O"))
			this.is_sentiment = false;
		else 
			this.is_sentiment = true;
	}
	
	/**
	 * @return true if the word has a sentiment tag, false if not
	 */
	public boolean isSentiment()
	{
		return is_sentiment;
	}
	
	/**
	 * @return Named Entity tag
	 */
	public String getNamedEntity()
	{
		return named_entity;
	}
	
	/**
	 * Setting new Named Entity tag
	 * @param namedEntity
	 */
	public void setNamedEntity(String named_entity)
	{
		this.named_entity = named_entity;
		//set whether or not this word is a sentiment
		if(named_entity.equals("O"))
			this.is_named_entity = false;
		else 
			this.is_named_entity = true;
	}
	
	/**
	 * @return true if the word has a Named Entity tag, false if not
	 */
	public boolean isNamedEntity()
	{
		return is_named_entity;
	}
}
