package com.java.context;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonSetter;

public class UserContext implements Serializable{
	private static final long serialVersionUID = 1L; 
	   private String groupId;
	   private Long userMobile;
	   private boolean status;
	   private String passcode;

	public boolean getStatus() {
		return status;
	}


	public void setStatus(boolean status) {
		this.status = status;
	}


	public UserContext(String groupId,Long userMobile,boolean status,String passcode) {
		this.groupId = groupId;
		this.userMobile = userMobile;
		this.status = status;
		this.passcode = passcode;
	}
	
	public UserContext()
	{
		
	}

	public String getPasscode() {
		return passcode;
	}


	public void setPasscode(String passcode) {
		this.passcode = passcode;
	}


	public String getGroupId() {
		return groupId;
	}


	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}


	public Long getUserMobile() {
		return userMobile;
	}


	public void setUserMobile(Long userMobile) {
		this.userMobile = userMobile;
	}
}




