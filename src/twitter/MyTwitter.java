package twitter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import org.json.*;
import nlp.MyNLP;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

/*
 * @author Cedomir Spalevic
 * A class that will retrieve tweets from the Twitter API and determine a star rating
 */
public class MyTwitter 
{
	//variable that will count number of tweets
	private int count;
	//variables used to see when program should end
	private int minute_now;
	private int minute_end;
	//variables used for the coordinates of a location
	private double southwest_latitude;
    private double southwest_longitude;
    private double northeast_latitude;
    private double northeast_longitude;
	//variables hold the credentials for Twitter and Google Maps API
	private String OAuthConsumerKey;
	private String OAuthConsumerSecret;
	private String OAuthAccessToken;
	private String OAuthAccessTokenSecret;
	private String GoogleMapsApiKey;
	
	/**
	 * This method sets up the credentials and coordinates for the Twitter status listener
	 * @param location
	 * @param length
	 * @param recognition
	 * @param rating_input
	 */
	public void process(String location, int length, boolean recognition, int rating_input)
	{
		//first get the credentials of from credentials.txt
		getCredentials();
		
		//if the parameter is not -1, then the user wants to filter by a location
		if(!location.equals("-1"))
			//method to use the Google Maps API and get the coordinates of the location
			getCoordinates(location);
		
		//start listening for tweets
		startListening(location, length, recognition, rating_input);
	}
	
	/**
	 * This method will read credentials.txt to get the credentials needed for the Twitter and Google Maps API
	 */
	private void getCredentials()
	{
		//create file object
		File file = new File("credentials.txt");
		//create scanner object
		Scanner sc = null;
		try
		{
			sc = new Scanner(file);
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		
		//read file
		String line;
		//get OAuthConsumeKey
		line = sc.nextLine();
		OAuthConsumerKey = line.substring(18,line.length()-1);
		//get OAuthConsumerSecret
		line = sc.nextLine();
		OAuthConsumerSecret = line.substring(21,line.length()-1);
		//get OAuthAccessToken
		line = sc.nextLine();
		OAuthAccessToken = line.substring(18,line.length()-1);
		//get OAuthAccessTokenSecret
		line = sc.nextLine();
		OAuthAccessTokenSecret = line.substring(24,line.length()-1);
		//get GoogleMapsApiKey
		line = sc.nextLine();
		GoogleMapsApiKey = line.substring(18,line.length()-1);
	}
	
	/**
	 * This method will begin listening for tweets using the Twitter API
	 * @param location
	 */
	private void startListening(String location, int length, boolean recognition, int rating_input)
	{
        //setting current directory
        String directory = System.getProperty("user.dir") + "/TwitterData/";
        File dir = new File(directory);
        
        //if directory does not already exist, create a new one
        if(!dir.exists())
        		dir.mkdirs();
        
        //creating directory strings
        Date date = new Date();
        SimpleDateFormat day = new SimpleDateFormat("YYYYMMdd_hmma");
        //the folder name will be the current date and time the program is run
        String foldername = day.format(date);
        if(!location.equals("-1"))
        		foldername += "-"+location;
         
        //creating folders to store tweets
        File folder = new File(directory+foldername);
        //if folder does not already exist, create a new
        if(!folder.exists())
        		folder.mkdirs();
        
        //set variables
        count = 0;
        minute_now = 0;
        //current minute plus the length the user wants the program to run for is when the program should end
        minute_end = Calendar.getInstance().get(Calendar.MINUTE) + length;
        //minute end should be less than 60
        if(minute_end>=60) minute_end = minute_end%60;
         
         //configure Twitter API
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(OAuthConsumerKey)
                .setOAuthConsumerSecret(OAuthConsumerSecret)
                .setOAuthAccessToken(OAuthAccessToken)
                .setOAuthAccessTokenSecret(OAuthAccessTokenSecret);
 
        //create a new TwitterStream
        TwitterStream twitter_stream = new TwitterStreamFactory(cb.build()).getInstance();
        
        //creating our nlp library
        MyNLP mynlp = new MyNLP();
         
        //StatusListener - continuously listens for new tweets to come in
        StatusListener status_listener = new StatusListener() 
        {
        		/**
        		 * Method for when a new Tweet is received
        		 */
            @Override
            public void onStatus(twitter4j.Status status)
            {
            		//get author of the tweet
                User user = status.getUser();
                boolean user_null = (user!=null?true:false);
                boolean status_location = (status.getGeoLocation()!=null?true:false);
                boolean status_place = (status.getPlace()!=null?true:false);
                
                //extra layer to get rid of outliers, unless user does not want to filter by location
                boolean stay = false;
                if(!location.equals("-1"))
                {
	                	if(status_location && 
	                            status.getGeoLocation().getLatitude() >= southwest_latitude &&
	                            status.getGeoLocation().getLatitude() <= northeast_latitude &&
	                            status.getGeoLocation().getLongitude() >= southwest_longitude &&
	                            status.getGeoLocation().getLongitude() <= northeast_longitude)
	                            stay = true;
                    if(status_place &&
                        status.getPlace().getBoundingBoxCoordinates()[0][0].getLatitude() >= southwest_latitude &&
                        status.getPlace().getBoundingBoxCoordinates()[0][0].getLongitude() >= southwest_longitude &&
                        status.getPlace().getBoundingBoxCoordinates()[0][1].getLatitude() <= northeast_latitude &&
                        status.getPlace().getBoundingBoxCoordinates()[0][1].getLongitude() <= northeast_longitude)
                        stay = true;
                }
                else
                		stay = true;
                
                
                String text = status.getText();
                //if user wants to filter by name recognition within a tweet, then check to see if there is a named entity
                if(recognition)
                {
                		//check to see if there is a named entity
                		boolean has_named_entity = mynlp.hasNamedEntity(text);
                		//if it does, then run
                		if(has_named_entity)
                			stay = true;
                		//if not, then drop this tweet
                		else
                			stay = false;
                }
                
                //if the tweet is within the bounding box, then write it to the file
                if(stay)
                {
                		//increment counter
                    count++;
                     
                    //for writing the status and user object attributes to a .txt file  
                    try
                    {
                        FileWriter fw = new FileWriter(new File(folder.toString() + "/tweets.txt"), true);
                        fw.write("Tweet #" + count + ":\n");
                         
                        fw.write("User ID: " + (user_null?user.getId():"NULL_NULL_NULL") + "\n");
                        fw.write("User name: " + (user_null?user.getName():"NULL_NULL_NULL") + "\n");
                        fw.write("User screen name: " + (user_null?user.getScreenName():"NULL_NULL_NULL") + "\n");
                        fw.write("Date user created account: " + (user_null?user.getCreatedAt().toString():"NULL_NULL_NULL") + "\n");
                        fw.write("Statuses posted: " + (user_null?user.getStatusesCount():"NULL_NULL_NULL") + "\n");
                        fw.write("Amount of followers: " + (user_null?user.getFollowersCount():"NULL_NULL_NULL") + "\n");
                        fw.write("Amount user is following: " + (user_null?user.getFriendsCount():"NULL_NULL_NULL") + "\n");
                        fw.write("User location: " + (user_null&&user.isGeoEnabled()?user.getLocation():"NULL_NULL_NULL") + "\n");
                        fw.write("User timezone: " + (user_null?user.getTimeZone():"NULL_NULL_NULL") + "\n");
                        fw.write("User language: " + (user_null?user.getLang():"NULL_NULL_NULL") + "\n");
                        fw.write("Status ID: " + status.getId() + "\n");
                        fw.write("Status language: " + status.getLang() + "\n");
                        fw.write("Date created: " + status.getCreatedAt().toString() + "\n");
                        fw.write("Status location: " + (status_location?status.getGeoLocation().getLatitude() + ", " + status.getGeoLocation().getLongitude():"NULL_NULL_NULL") + "\n");
                        fw.write("Is favorited: " + status.isFavorited() + "\n");
                        fw.write("Amount of favorites: " + status.getFavoriteCount() + "\n");
                        fw.write("Is retweeted: " + status.isRetweeted() + "\n");
                        fw.write("Amount of retweets: " + status.getRetweetCount() + "\n");
                        fw.write("Is retweeted by me: " + status.isRetweetedByMe() + "\n");
                        fw.write("Is a reweet: " + status.isRetweet() + "\n");
                        Status t = status;
                        while(t.isRetweet())
                        {
                            fw.write("Retweeted status ID: " + t.getRetweetedStatus().getId() + "\n");
                            t = t.getRetweetedStatus();
                        }
                        fw.write("Is possibly sensitive: " + status.isPossiblySensitive() + "\n");
                        fw.write("Is truncated: " + status.isTruncated() + "\n");
                        fw.write("Place: " + (status_place?status.getPlace().getFullName():"NULL_NULL_NULL") + "\n");
                        fw.write("Description: " + (user_null?user.getDescription():"NULL_NULL_NULL") + "\n\n");
                        fw.write(text + "\n\n");
                        
                        int our_rating;
                        //if user wanted 3 star rating, run 3 star rating on text
                        if(rating_input == 3)
                        		our_rating = mynlp.process(status.getText(), 3);
                        else
                        		our_rating = mynlp.process(status.getText(), 5);
                        //print to file
                        fw.write("Computed rating of the tweet = " + our_rating + "\n\n-\n\n");
                        
                        //close FileWriter
                        fw.close();
                    }
                    catch(IOException e) 
                    {
                    		System.out.println("Could not print to file.");
                    }
                }
                
                //getting the current minute
                minute_now = Calendar.getInstance().get(Calendar.MINUTE);
                //if is the time at which the program should, then end
                if(minute_now == minute_end)
                {
                		System.out.println("Done!");
                		//clear twitter stream
                    twitter_stream.cleanUp();
                    //shut down twitter stream
                    twitter_stream.shutdown();
                }
            }

			@Override
			public void onException(Exception arg0) {}
			@Override
			public void onDeletionNotice(StatusDeletionNotice arg0) {}
			@Override
			public void onScrubGeo(long arg0, long arg1) {}
			@Override
			public void onStallWarning(StallWarning arg0) {}
			@Override
			public void onTrackLimitationNotice(int arg0) {}
        };
        
        //add status listener to twitter stream
        twitter_stream.addListener(status_listener);
        System.out.println("Starting...");
        //if the user wants to filter by location, add the filter
        if(!location.equals("-1"))
        {
        		//create a FilterQuery create object
        		FilterQuery filter = new FilterQuery();
            //coordinates of teh location
            double[][] locations = {{southwest_longitude, southwest_latitude}, 
                    {northeast_longitude, northeast_latitude}};
            //add location to FilterQuery
            filter.locations(locations);
            //begin listening
            twitter_stream.filter(filter);
        }
        //if not, then run a sample
        else
        		twitter_stream.sample();
	}
	
	/**
	 * This method will use the Google Maps API to get the coordinates of the given location
	 * @param location
	 */
	private void getCoordinates(String location)
	{
		//first check that the Google Maps API Key was given
		if(GoogleMapsApiKey.isEmpty())
		{
			//no Google Maps API Key was provided, so terminate the program
			System.out.println("No Google Maps API Key was provided.");
			System.exit(1);
		}
		
		//the address attribute in the google maps HTTP GET request does not accept spaces
		//so we must replace ' ' with a '+'
        location = location.replace(' ', '+');
        //Google Maps API url to retrieve bounds of region
        String url_string = "https://maps.googleapis.com/maps/api/geocode/json?address=" + location + "&key=" + GoogleMapsApiKey;
        
        try
        {
        		//create new URL object 
            URL url = new URL(url_string);
            //open the stream and get the content
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            //create StringBuffer object
            StringBuffer buffer = new StringBuffer();
            int read;
            //read the content and store it to a StringBuffer object
            while((read = reader.read()) != -1)
                buffer.append((char) read);
            //result of the HTTP GET request is in the form of a JSON
            //create JSON object
            JSONObject json = new JSONObject(buffer.toString());
            JSONObject geometry = json.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("viewport");
            //get south west coordinates of the location
            JSONObject southwest = geometry.getJSONObject("southwest");
            southwest_latitude = (double) southwest.get("lat");
            southwest_longitude = (double) southwest.get("lng");
            //get north east coordinates of the location
            JSONObject northeast = geometry.getJSONObject("northeast");
            northeast_latitude = (double) northeast.get("lat");
            northeast_longitude = (double) northeast.get("lng");
        }
        catch(Exception e)
        {
            System.out.println("Could not find coordinates.");
            System.exit(1);
        }
	}
}
