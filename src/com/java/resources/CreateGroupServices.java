package com.java.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.java.context.GroupContext;
import com.java.context.UserContext;
import com.java.database.MongoCommands;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Path("/group") 
public class CreateGroupServices {
	 	@POST
	   @Path("/createGroup")
	   @Produces(MediaType.TEXT_PLAIN)
	   @Consumes({MediaType.APPLICATION_JSON})
	   public static String createGroup(GroupContext context) throws IOException
	   {
	 		
	 	   
		   	List<Long> userFirst = new ArrayList<Long>();
		   	
		   	
		 	BasicDBObject queryNew = new BasicDBObject();
		 	queryNew.put("userMobile", new BasicDBObject("$eq", context.getOwnerMobile()));
		 	queryNew.put("status", new BasicDBObject("$eq", true));
		   	FindIterable<Document> docsNew = MongoCommands.retrieveDataWithCondition("usersGroup", "Information", queryNew);
		   	if(docsNew.first() != null)
		   	{
		   		return "ALREADY EXISTS";
		   	}
		   	BasicDBObject query = new BasicDBObject();
	 	   	query.put("ownerMobile", new BasicDBObject("$eq", context.getOwnerMobile()));
	 	   	query.put("isActive", new BasicDBObject("$eq", true));
		   	FindIterable<Document> docs = MongoCommands.retrieveDataWithCondition("group", "Information", query);
		   	if(docs.first() == null)
		   	{
		   		int groupId = generateRandom();
		 		Document doc = new Document("groupId",groupId + "")
		 				.append("password", context.getPassword())
		 				.append("ownerMobile", context.getOwnerMobile())
		 				.append("users", userFirst)
		 				.append("isActive", true)
		 				.append("groupName", context.getGroupName());
		 		MongoCommands.insertData("group", "Information", doc);
		 		UserContext usercontext = new UserContext();
		 		usercontext.setGroupId(groupId + "");
		 		usercontext.setUserMobile(context.getOwnerMobile());
		 		usercontext.setStatus(true);
		 		usercontext.setPasscode(context.getPassword());
		 		UserGroupServices.joinGroup(usercontext);
		 		return "SUCCESS";
		   	}
		   	else
		   	{
		   		return "ALREADY EXISTS";
		   	}
		   	
	   }
	 	@POST
		   @Path("/deleteGroup")
		   @Produces(MediaType.TEXT_PLAIN)
		   @Consumes({MediaType.APPLICATION_JSON})
		   public static String deleteGroup(GroupContext context)
		   {
		 		
	 		List<String> userFirst = new ArrayList<String>();
	 		BasicDBObject query = new BasicDBObject();
   			BasicDBObject queryUser = new BasicDBObject();

	   		query.put("groupId", new BasicDBObject("$eq",context.getGroupId()));
	   		FindIterable<Document> list = MongoCommands.retrieveDataWithCondition("group", "Information", query);
	   		List<String> usersMobile = (List<String>) list.first().get("users");
	   		for(String user : usersMobile)
	   		{
	   			queryUser.clear();
	   			queryUser.put("userMobile", new BasicDBObject("$eq",user));
			    MongoCommands.deleteDataWithCondition("usersGroup","Information",queryUser);
	   		}
	 		BasicDBObject queryNew = new BasicDBObject();
	   		queryNew.put("groupId", new BasicDBObject("$eq",context.getGroupId()));
	   		queryNew.put("userMobile", new BasicDBObject("$eq",context.getOwnerMobile()));
		    MongoCommands.deleteDataWithCondition("usersGroup","Information",queryNew);
		    
	 		Bson filter, document;
		   	filter = eq("groupId", context.getGroupId());
		   	document =  set("isActive", false);
		    MongoCommands.updateData("group","Information",document, filter);
			return "SUCCESS";
		   }
	 	@POST
		   @Path("/getActiveGroup")
		   @Produces(MediaType.APPLICATION_JSON)
		   @Consumes({MediaType.APPLICATION_JSON})
		   public static Document getActiveGroup(GroupContext context)
		   {
	 		System.out.println(context.getGroupId());
	 		BasicDBObject query = new BasicDBObject();
	 	   	query.put("groupId", new BasicDBObject("$eq", context.getGroupId()));
	 	   	query.put("isActive", new BasicDBObject("$eq", true));
		   	FindIterable<Document> docs = MongoCommands.retrieveDataWithCondition("group", "Information", query);
			return docs.first();
		   }
	 	private static int generateRandom()
	 	{
	 		Random rand = new Random();
	 	    int upperbound = 99999999;
	 	    int int_random = rand.nextInt(upperbound); 
	 	    System.out.println(int_random);
	 	    BasicDBObject query = new BasicDBObject();
	 	   	query.put("groupId", new BasicDBObject("$eq", int_random));
		   	FindIterable<Document> docs = MongoCommands.retrieveDataWithCondition("group", "Information", query);
		   	if(docs.first() == null)
		   	{
		 	    return int_random;
		   	}
		  	generateRandom();
		  	return 0;
	 	}
}