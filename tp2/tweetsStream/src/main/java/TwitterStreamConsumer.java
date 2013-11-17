import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
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
		
		// Reading Oauth Credentials
		InputStream is = TwitterStreamConsumer.class.getResourceAsStream("/oauthcredentials");
		BufferedReader bf = new BufferedReader(new InputStreamReader(is));
		
		String line = bf.readLine();
		String consumerKey = "";
		String consumerSecret = "";
		String accessToken = "";
		String accessTokenSecret = "";
		
		String[] lineElements;
		String trimmedLine;
		
		while (line != null)
		{
			lineElements = line.split("=");
			
			if (lineElements.length == 2)
			{
				trimmedLine = lineElements[0].trim();
				
				if (trimmedLine.equals("consumerKey"))
				{
					consumerKey = lineElements[1].trim();
					line = bf.readLine();
					continue;
				}
				
				if (trimmedLine.equals("consumerSecret"))
				{
					consumerSecret = lineElements[1].trim();
					line = bf.readLine();
					continue;
				}
				
				if (trimmedLine.equals("accessToken"))
				{
					accessToken = lineElements[1].trim();
					line = bf.readLine();
					continue;
				}
				
				if (trimmedLine.equals("accessTokenSecret"))
				{
					accessTokenSecret = lineElements[1].trim();
					line = bf.readLine();
					continue;
				}
			}
		}
		
		if (accessToken.equals("") || accessTokenSecret.equals("") || consumerSecret.equals("") || consumerKey.equals(""))
		{
			System.err.println("Authentication isn't complete");
			System.exit(1);
		}
			
		// Verifying output folder
		if (args.length <= 0)
		{
			System.err.println("You need to specify Output");
			System.exit(1);
		}
		
		String output = args[0];
		
		File folderTweets = new File(output);  
		  
		if (!folderTweets.isDirectory())
		{
			System.err.println("Directory doesn't exist.");
			System.exit(1);
		}
		
		// Parsing terms
		ArrayList<String> search = new ArrayList<String>();
		
		String[] keywords;
		
		if (args.length > 1)
		{
			String lastKeyword = null;
			
			for (int i = 1; i < args.length; i++)
			{
				keywords = args[i].split(",");
				
				if (keywords.length > 0)
				{
					if (lastKeyword == null)
					{
						lastKeyword = keywords[0];
					}
					else
					{
						lastKeyword = lastKeyword + " " + keywords[0];
					}
									
					for (int j = 1; j < keywords.length; j++)
					{
						search.add(lastKeyword);
						
						lastKeyword = keywords[j];
					}
				}
			}
			search.add(lastKeyword);
		}
		else
		{
			search.add("hashtag");
		}
		

		// Preparing hosebird connection...
		// Create an appropriately sized blocking queue
		BlockingQueue<String> queue = new LinkedBlockingQueue<String>(1000);
		
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
		
		// Let's connect and iterate!
		hosebirdClient.connect();
		while (!hosebirdClient.isDone()) 
		{			
			queue.drainTo(tweets, 500);

			if(!tweets.isEmpty()) 
			{
				// Generating output...
				PrintWriter printWriter = new PrintWriter(output + "/tweets" + i);
				
				for (String tweet : tweets) 
				{
					printWriter.print(tweet);
				}
				
				System.out.println(tweets.size() + " tweets were written in tweets" + i);
				
				i++;
				
				printWriter.close();
			}
			else
			{
				System.out.println("No new tweets found...");
			}
			
			tweets.clear();
			
			Thread.sleep(60000);
		}
	}
}
