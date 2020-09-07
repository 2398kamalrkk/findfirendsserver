package com.java.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.json.JSONException;
import org.json.JSONObject;


@ServerEndpoint("/echo")

public class clientSocketConnection {
	public static Map<String,Session> mobileToSession = new HashMap<String,Session>();
	public static Map<String,String> mobileToGroup = new HashMap<String,String>();
	public static Map<String,List<Session>> groupToSession = new HashMap<String,List<Session>>();
	public static Map<Session,String> sessionToGroup = new HashMap<Session,String>();
	public static Map<Session,String> sessionToMobile = new HashMap<Session,String>();


	 @OnOpen
	    public void onOpen(Session session) throws IOException 
	    {
	    	
	    }

	    @OnMessage
	    public String echo(String message,Session session) throws JSONException, IOException 
	    {
	        JSONObject jsonObject = new JSONObject(message);
	        if(jsonObject.getString("context").equals("REGISTER") && !jsonObject.isNull("mobile") && !jsonObject.isNull("groupid"))
	        {
	        	List<Session> groupTemp = groupToSession.get(jsonObject.getString("groupid"));
	        	
	        	if(groupTemp == null)
	        	{
	        		groupTemp = new ArrayList<Session>();
	        	}
	        	if(groupToSession.get(jsonObject.getString("groupid")) != null)
	        	{
	        		for(Session singleSession : groupToSession.get(jsonObject.getString("groupid")))
			    	{
			    		String jsonData = "{\"context\":\"ONLINE\",\"mobile\":\""+ jsonObject.getString("mobile") +"\"}";
			    		singleSession.getBasicRemote().sendText(jsonData);
			    	}
	        	}
	        	if(!groupTemp.contains(session))
	        	{
		        	groupTemp.add(session);
	        	}
	        	sessionToMobile.put(session,jsonObject.getString("mobile"));
	        	sessionToGroup.put(session,jsonObject.getString("groupid"));
	        	groupToSession.put(jsonObject.getString("groupid"), groupTemp);
	        	mobileToGroup.put(jsonObject.getString("mobile"), jsonObject.getString("groupid"));
	        	mobileToSession.put(jsonObject.getString("mobile"), session);
	        	
		    	System.out.println(jsonObject.getString("mobile"));
	        }
	        else if(jsonObject.getString("context").equals("SENDDATA") && !jsonObject.isNull("latitude") && !jsonObject.isNull("longitude") && !jsonObject.isNull("groupid") && !jsonObject.isNull("mobile"))
	        {
	        	System.out.println("wertyuiopoiuytrewsdfghjkkjhgfdssxcvbnmnbvcdfghjkuytr");
	    		System.out.println(groupToSession.get(jsonObject.getString("groupid")));
	        	System.out.println(jsonObject.getString("groupid"));
		    	for(Session singleSession : groupToSession.get(jsonObject.getString("groupid")))
		    	{
		    		String jsonData = "{\"latitude\":\""+jsonObject.getString("latitude")+"\",\"longitude\":\""+jsonObject.getString("longitude")+"\",\"mobile\":\""+jsonObject.getString("mobile")+"\"}";
		    		singleSession.getBasicRemote().sendText(jsonData);
		    	}
	        }
	        
	        return "HELLO";
	    }

	    @OnError
	    public void onError(Throwable t) 
	    {
	        t.printStackTrace();
	    }

	    @OnClose
	    public void onClose(Session session) throws IOException 
	    {
	    	
    			System.out.println(session);
				if(groupToSession.get(sessionToGroup.get(session)) != null)
				{
					for(Session singleSession : groupToSession.get(sessionToGroup.get(session)))
			    	{
		    			if(singleSession != session)
		    			{
		    				String jsonData = "{\"context\":\"OFFLINE\",\"mobile\":\""+ sessionToMobile.get(session) +"\"}";
				    		singleSession.getBasicRemote().sendText(jsonData);
				    	}
			    	}
					String temp = sessionToGroup.get(session);
			    	List<Session> tempGroup = groupToSession.get(temp);
			    	tempGroup.remove(session);
			    	groupToSession.put(temp,tempGroup);
				}
	    		
		    		
	    	
	    	
	    	sessionToGroup.remove(session);
	    	
	    	mobileToSession.remove(sessionToMobile.get(session));
	    	sessionToMobile.remove(session);
	    }
}
