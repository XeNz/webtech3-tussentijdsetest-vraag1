package edu.ap.jaxrs;

import java.io.*;
import java.util.List;
import java.util.Set;

import javax.ws.rs.*;

import com.sun.corba.se.impl.oa.poa.ActiveObjectMap.Key;
import com.sun.org.apache.regexp.internal.recompile;

import javax.json.*;
import redis.clients.jedis.*;

// SET 'author:1' 'Winston Churchill'
// SET 'author:2' 'Jeffrey Lebowski'
// SADD 'quote:1' 'I may be drunk, Miss, but in the morning I will be sober and you will still be ugly.'
// SADD 'quote:1' 'If you are going through hell, keep going.'
// SADD 'quote:2' 'The rug really tied the room together.'
// SADD 'quote:2' 'Yeah, well, you know, that's just, like, your opinion, man.'





@Path("/quotes")
public class QuoteResource {
	
	//redis client
	Jedis jedis = new Jedis("localhost");

	@GET
	@Produces({"text/html"})
	public String getQuotesHTML() {
		String htmlString = "<html><body>";
		try {
			Set<String> allAuthors = jedis.keys("author*");
	        java.util.Iterator<String> it = allAuthors.iterator();
	        while(it.hasNext()) {
	            String s = it.next();
	            htmlString += "Key : " + s + "Name : " + jedis.get(s);
	        }
		}
		catch(Exception ex) {
			htmlString = "<html><body>" + ex.getMessage();
		}
		
		return htmlString + "</body></html>";
	}
	
	@POST
	@Produces({"text/html"})
	public String getAuthorsQuotesHTML(@PathParam("author") String author) {
		String htmlString = "<html><body>";
		try {
			Set<String> allAuthors = jedis.keys("author*");
		    if(allAuthors.contains(author)){
		    	//key van author zoeken by value
		    	List<String> authorKey = jedis.hvals(author);
		    	//key van author is hier value
		    	String authorKeyString = authorKey.get(0);
				List<String> allQuotes = jedis.hvals(authorKeyString);
		        java.util.Iterator<String> it = allAuthors.iterator();
		        while(it.hasNext()) {
		            String s = it.next();
		            htmlString += "Quotes : " + jedis.get(s);
		        }
		    	
		    }
		}
		catch(Exception ex) {
			htmlString = "<html><body>" + ex.getMessage();
		}
		
		return htmlString + "</body></html>";
	}
	
}
