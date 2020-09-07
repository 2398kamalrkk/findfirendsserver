package com.java.resources;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.java.context.UserContext;
import com.java.context.UserSignUpContext;
import com.java.database.MongoCommands;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;

@Path("/login")
public class UserSignUpServices {
	@POST
	   @Path("/userSignUp")
	   @Produces(MediaType.TEXT_PLAIN)
	   @Consumes({MediaType.APPLICATION_JSON})
	   public static String userSignUp(UserSignUpContext context)
	   {
		String prof = context.getProfilePicture();
		if(prof.equals("https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png"))
		{
			switch(context.getName().toLowerCase().charAt(0))
			{
				case 'a':
					prof = "https://i.imgur.com/aVUXMCH.png";
					break;
				case 'b':
					prof = "https://i.imgur.com/Pbz4GYt.png";
					break;
				case 'c':
					prof = "https://i.imgur.com/1HxECed.png";
					break;
				case 'd':
					prof = "https://i.imgur.com/S9qUZdl.png";
					break;
				case 'e':
					prof = "https://i.imgur.com/8Ai32gi.png";
					break;
				case 'f':
					prof = "https://i.imgur.com/3HEK5mz.png";
					break;
				case 'g':
					prof = "https://i.imgur.com/9Tu5Dfi.png";
					break;
				case 'h':
					prof = "https://i.imgur.com/1bFAYJo.png";
					break;
				case 'i':
					prof = "https://i.imgur.com/mB2AUm4.png";
					break;
				case 'j':
					prof = "https://i.imgur.com/tZNiInd.png";
					break;
				case 'k':
					prof = "https://i.imgur.com/SBaTU2k.png";
					break;
				case 'l':
					prof = "https://i.imgur.com/cr2HTYk.png";
					break;
				case 'm':
					prof = "https://i.imgur.com/XKrzWmn.png";
					break;
				case 'n':
					prof = "https://i.imgur.com/4mUO4Dx.png";
					break;
				case 'o':
					prof = "https://i.imgur.com/6oNo6JF.png";
					break;
				case 'p':
					prof = "https://i.imgur.com/No7WQoY.png";
					break;
				case 'q':
					prof = "https://i.imgur.com/X2FHBfh.png";
					break;
				case 'r':
					prof = "https://i.imgur.com/dvmqEy5.png";
					break;
				case 's':
					prof = "https://i.imgur.com/oMkcixz.png";
					break;
				case 't':
					prof = "https://i.imgur.com/PSc23Od.png";
					break;
				case 'u':
					prof = "https://i.imgur.com/Eks4JOO.png";
					break;
				case 'v':
					prof = "https://i.imgur.com/EoTDB7K.png";
					break;
				case 'w':
					prof = "https://i.imgur.com/VW0hCKY.png";
					break;
				case 'x':
					prof = "https://i.imgur.com/OzM5pJf.png";
					break;
				case 'y':
					prof = "https://i.imgur.com/Plmbb4Q.png";
					break;
				case 'z':
					prof = "https://i.imgur.com/JkCixLY.png";
					break;
					
			}
		}
		    Document doc = new Document("mobile" , context.getMobile())
		    		.append("password" , context.getPassword())
		    		.append("profilePicture", prof)
    				.append("name", context.getName());

		    MongoCommands.insertData("userSignUp", "Information", doc);
			return "SUCCESS";
	   }
	@POST
	   @Path("/getProfile")
	   @Produces(MediaType.APPLICATION_JSON)
	   @Consumes({MediaType.APPLICATION_JSON})
	   public static Document getProfile(UserSignUpContext context)
	   {
			BasicDBObject queryNew = new BasicDBObject();
	   		queryNew.put("mobile", new BasicDBObject("$eq",context.getMobile()));
			return MongoCommands.retrieveDataWithCondition("userSignUp", "Information", queryNew).first();
	   }
	@POST
	   @Path("/alreadySignedUp")
	   @Produces(MediaType.TEXT_PLAIN)
	   @Consumes({MediaType.APPLICATION_JSON})
	   public static String alreadySignedUp(UserSignUpContext context)
	   {
		BasicDBObject queryNew = new BasicDBObject();
   		queryNew.put("mobile", new BasicDBObject("$eq",context.getMobile()));
	   	FindIterable<Document> docsNew = MongoCommands.retrieveDataWithCondition("userSignUp", "Information", queryNew);
	   	if(docsNew.first() == null)
	   	{
			return "SUCCESS";
	   	}
	   	return "FAILED";
	   }
	@POST
	   @Path("/login")
	   @Produces(MediaType.APPLICATION_JSON)
	   @Consumes({MediaType.APPLICATION_JSON})
	   public static Document login(UserSignUpContext context)
	   {
		BasicDBObject queryNew = new BasicDBObject();
		queryNew.put("mobile", new BasicDBObject("$eq",context.getMobile()));
	   	FindIterable<Document> docsNew = MongoCommands.retrieveDataWithCondition("userSignUp", "Information", queryNew);
	   	if(docsNew.first() == null)
	   	{
	   		Document doc=new Document("status" , "NO USER");
	   		return doc;
	   	}
	   	else if(docsNew.first().getString("password").equals(context.getPassword()))
	   	{
	   		Document doc=new Document("status" , "SUCCESS")
	   				.append("profile",docsNew.first().getString("profilePicture"));
	   		return doc;
	   	}
	   	Document doc=new Document("status" , "FAILED");
   		return doc;	   
   		}
	@POST
	   @Path("/reset")
	   @Produces(MediaType.TEXT_PLAIN)
	   @Consumes({MediaType.APPLICATION_JSON})
	   public static String reset(UserSignUpContext context)
	   {
		Bson filter, document;
	   	filter = eq("mobile", context.getMobile());
	   	document =  set("password", context.getPassword());
	    MongoCommands.updateData("userSignUp","Information",document, filter);
		return "SUCCESS";	   
		}
	@POST
	   @Path("/delete")
	   @Produces(MediaType.TEXT_PLAIN)
	   @Consumes({MediaType.APPLICATION_JSON})
	   public static String delete(UserSignUpContext context)
	   {
		BasicDBObject queryNew = new BasicDBObject();
		queryNew.put("mobile", new BasicDBObject("$eq",context.getMobile()));
		MongoCommands.deleteDataWithCondition("userSignUp", "Information", queryNew);
		return "SUCCESS";
		}
}