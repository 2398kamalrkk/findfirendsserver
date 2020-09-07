package com.java.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class MongoServerConnection {
	//public static MongoClient mongo = new MongoClient( "localhost" , 27017 ); 

	public static MongoClientURI uri = new MongoClientURI("mongodb://findfriends:Santhomian@2398@cluster0-shard-00-00-gbif3.mongodb.net:27017,cluster0-shard-00-01-gbif3.mongodb.net:27017,cluster0-shard-00-02-gbif3.mongodb.net:27017/test?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin&retryWrites=true&w=majority");
	public static MongoClient mongo = new MongoClient(uri); 


}
