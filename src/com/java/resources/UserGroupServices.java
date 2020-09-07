package com.java.resources;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.websocket.Session;
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
import com.java.websocket.clientSocketConnection;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;

@Path("/user") 
public class UserGroupServices {
	 	@POST
	   @Path("/joinGroup")
	   @Produces(MediaType.TEXT_PLAIN)
	   @Consumes({MediaType.APPLICATION_JSON})
	   public static String joinGroup(UserContext context) throws IOException
	   {
	 		BasicDBObject query = new BasicDBObject();
	 	   	query.put("userMobile", new BasicDBObject("$eq", context.getUserMobile()));
	 	   	query.put("status", new BasicDBObject("$eq", true));
		   	FindIterable<Document> docs = MongoCommands.retrieveDataWithCondition("usersGroup", "Information", query);
		   	if(docs.first() != null)
		   	{
		   		return "ALREADY IN GROUP";
		   	}
		   	else
		   	{
		   		BasicDBObject queryNew = new BasicDBObject();
		   		queryNew.put("groupId", new BasicDBObject("$eq",context.getGroupId()));
			   	FindIterable<Document> docsNew = MongoCommands.retrieveDataWithCondition("group", "Information", queryNew);
			   	if(docsNew.first() == null)
			   	{
			   		System.out.println("1");
			   		return "INVALID";
			   	}
			   	else if(!(docsNew.first().getString("password").equals(context.getPasscode())))
			   	{
			   		System.out.println("2");
			   		return "INVALID";
			   	}
			   	else
			   	{
			   		List<String> users = (List<String>) docsNew.first().get("users");
			   		users.add(context.getUserMobile().toString());
			   		Bson filter, document;
				   	filter = eq("groupId", context.getGroupId());
				   	document =  set("users", users);
				    MongoCommands.updateData("group","Information",document, filter);
				    Document doc = new Document("userMobile" , context.getUserMobile())
			   				.append("status", true)
			   				.append("groupId", context.getGroupId());
			   		MongoCommands.insertData("usersGroup", "Information", doc);
			   		if(clientSocketConnection.groupToSession.get(context.getGroupId()) != null)
		        	{
		        		for(Session singleSession : clientSocketConnection.groupToSession.get(context.getGroupId()))
				    	{
				    		String jsonData = "{\"context\":\"REFRESH\"}";
				    		singleSession.getBasicRemote().sendText(jsonData);
				    	}
		        		
		        	}
			   	}
		   		return "SUCCESS";
		   	}
	   }
	 	@POST
		   @Path("/leaveGroup")
		   @Produces(MediaType.TEXT_PLAIN)
		   @Consumes({MediaType.APPLICATION_JSON})
		   public static String leaveGroup(UserContext context) throws IOException
		   {
		   		List<String> userFirst = new ArrayList<String>();
		 		BasicDBObject queryNew = new BasicDBObject();
		   		queryNew.put("groupId", new BasicDBObject("$eq",context.getGroupId()));
		   		queryNew.put("userMobile", new BasicDBObject("$eq",context.getUserMobile()));
			    MongoCommands.deleteDataWithCondition("usersGroup","Information",queryNew);
			    
				BasicDBObject query = new BasicDBObject();
		 	   	query.put("groupId", new BasicDBObject("$eq", context.getGroupId()));
		 	   	query.put("isActive", new BasicDBObject("$eq", true));
			   	FindIterable<Document> docs = MongoCommands.retrieveDataWithCondition("group", "Information", query);
			   	userFirst = (List<String>) docs.first().get("users");
			   	userFirst.remove(context.getUserMobile().toString());
			   	
				Bson filter, document;
			   	filter = eq("groupId", context.getGroupId());
			   	document =  set("users", userFirst);
			    MongoCommands.updateData("group","Information",document, filter);
			    if(clientSocketConnection.groupToSession.get(context.getGroupId()) != null)
	        	{
	        		for(Session singleSession : clientSocketConnection.groupToSession.get(context.getGroupId()))
			    	{
			    		String jsonData = "{\"context\":\"REFRESH\"}";
			    		singleSession.getBasicRemote().sendText(jsonData);
			    	}
	        	}
			    
		    	List<Session> tempGroup = clientSocketConnection.groupToSession.get((context.getGroupId()));
		    	tempGroup.remove(clientSocketConnection.mobileToSession.get(context.getUserMobile().toString()));
		    	clientSocketConnection.groupToSession.put((context.getGroupId()),tempGroup);
				return "SUCCESS";
		   }
	 	@POST
		   @Path("/getGroup")
		   @Produces(MediaType.APPLICATION_JSON)
		   @Consumes({MediaType.APPLICATION_JSON})
		   public static Document getGroup(UserContext context)
		   {
		 		BasicDBObject query = new BasicDBObject();
		 	   	query.put("userMobile", new BasicDBObject("$eq", context.getUserMobile()));
		 	   	query.put("status", new BasicDBObject("$eq", true));
			   	FindIterable<Document> docs = MongoCommands.retrieveDataWithCondition("usersGroup", "Information", query);
			   	return docs.first();
		   }
	 	@POST
		   @Path("/getProfile")
		   @Produces(MediaType.APPLICATION_JSON)
		   @Consumes({MediaType.APPLICATION_JSON})
		   public static Document getProfile(GroupContext context)
		   {
	 		List<String> urls = new ArrayList<String>();
	 		List<String> allMobile = new ArrayList<String>();
	 		List<String> status = new ArrayList<String>();
	 		List<String> names = new ArrayList<String>();

	 		BasicDBObject query = new BasicDBObject();
	 	   	query.put("groupId", new BasicDBObject("$eq", context.getGroupId()));
		   	FindIterable<Document> docs = MongoCommands.retrieveDataWithCondition("group", "Information", query);
		   	System.out.println(docs.first().get("users"));
	 		for(String mob : (List<String>)docs.first().get("users"))
	 		{
	 			BasicDBObject queryNew = new BasicDBObject();
	 			queryNew.put("mobile", new BasicDBObject("$eq", mob.toString()));
			   	FindIterable<Document> docsNew = MongoCommands.retrieveDataWithCondition("userSignUp", "Information", queryNew);
			   	urls.add(docsNew.first().getString("profilePicture"));
			   	allMobile.add(docsNew.first().getString("mobile"));
			   	names.add(docsNew.first().getString("name"));
			   	if(clientSocketConnection.mobileToSession.containsKey(mob.toString()))
			   	{
				   	status.add("true");
			   	}
			   	else
			   	{
				   	status.add("false");
			   	}
	 		}
	 		Document doc = new Document("profile" , urls)
	 				.append("allMobile", allMobile)
	 				.append("status", status)
	 				.append("names", names);
		 	return doc;
		   }
}