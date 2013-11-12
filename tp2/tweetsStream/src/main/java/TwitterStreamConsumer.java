import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;



public class TwitterStreamConsumer {
	public static void main(String[] args) throws InterruptedException, IOException{
		
		InputStream is = TwitterStreamConsumer.class.getResourceAsStream("/oauthcredentials");
		BufferedReader bf = new BufferedReader(new InputStreamReader(is));
		
		String line = bf.readLine();
		String consumerKey = "";
		String consumerSecret = "";
		String accessToken = "";
		String accessTokenSecret = "";
		
		String[] lineElements;
		
		while (line != null)
		{
			lineElements = line.split("=");
			
			if (lineElements.length == 2)
			{
				switch (lineElements[0].trim()) {
				case "consumerKey": 
					consumerKey = lineElements[1].trim();
					break;
				case "consumerSecret": 
					consumerSecret = lineElements[1].trim();
					break;
				case "accessToken":
					accessToken = lineElements[1].trim();
					break;
				case "accessTokenSecret": 
					accessTokenSecret = lineElements[1].trim();
					break;
				default:
					break;
				}
			}
			
			line = bf.readLine();
		}
		
		if (accessToken.equals("") || accessTokenSecret.equals("") || consumerSecret.equals("") || consumerKey.equals(""))
		{
			System.out.println("Authentication isn't complete");
			System.exit(1);
		}
		
		ArrayList<String> search = Lists.newArrayList("hashtag");
		
		if (args.length > 0)
		{
			String[] keywords = args[0].split(",");
			
			search = new ArrayList<String>(Arrays.asList(keywords));
		}
		
		String searchString = search.toString();
		
		searchString = searchString.substring(1, searchString.length()-1);
				
		// Create an appropriately sized blocking queue
		//TODO Decide size...
		BlockingQueue<String> queue = new LinkedBlockingQueue<String>(10000);
		
		StatusesFilterEndpoint endpoint = new StatusesFilterEndpoint();
	    // add some track terms
		endpoint.trackTerms(search);
		
		// Authenticate via OAuth
		Authentication auth = new OAuth1(consumerKey, consumerSecret, accessToken, accessTokenSecret);
		// Build a hosebird client
		ClientBuilder builder = new ClientBuilder()
		   .hosts(Constants.STREAM_HOST)
		   .authentication(auth)
		   .endpoint(endpoint)
		   .processor(new StringDelimitedProcessor(queue));
		
		Client hosebirdClient = builder.build();
		
		List<String> tweets = Lists.newArrayList();
		
		int i = 0;
		
		File folderTweets = new File("tweets");  
		  
		if (!folderTweets.isDirectory())
		{
			folderTweets.mkdir();
		}
		
		File subFolderTweets = new File("tweets/" + searchString);
		
		if (!subFolderTweets.isDirectory())
		{
			subFolderTweets.mkdir();
		}
		
		hosebirdClient.connect();
		while (!hosebirdClient.isDone()) 
		{			
			queue.drainTo(tweets, 500);

			if(!tweets.isEmpty()) 
			{
				PrintWriter printWriter = new PrintWriter("tweets/" + searchString + "/tweets" + i);
				
				for (String tweet : tweets) 
				{
					printWriter.println(tweet);
				}
				
				i++;
				
				printWriter.close();
			}
			
			tweets.clear();
			
			Thread.sleep(10000);
		}
	}
}
