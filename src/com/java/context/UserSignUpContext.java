package com.java.context;
import java.io.Serializable;

public class UserSignUpContext implements Serializable{
	private static final long serialVersionUID = 1L; 
	   private String mobile;
	   private String password;
	   private String profilePicture;
	   private String name;
	   
	     
	   
	public UserSignUpContext(String mobile,String password,String profilePicture,String name) {
		this.mobile = mobile;
		this.password = password;
		this.profilePicture = profilePicture;
		this.name = name;
	}
	

	public UserSignUpContext()
	{
		
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getMobile() {
		return mobile;
	}


	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getProfilePicture() {
		return profilePicture;
	}


	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}
	
}
