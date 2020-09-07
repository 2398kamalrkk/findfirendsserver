package com.java.context;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonSetter;

public class GroupContext implements Serializable{
	private static final long serialVersionUID = 1L; 
	   private String groupId;
	   private Long ownerMobile;
	   private List<Long> users;
	   private String password;
	   private boolean isActive;
	   private String groupName;
	     
	   
	public GroupContext(String groupId,Long ownerMobile,List<Long> users,String password,boolean isActive,String groupName) {
		this.groupId = groupId;
		this.ownerMobile = ownerMobile;
		this.users = users;
		this.password = password;
		this.isActive = isActive;
		this.groupName = groupName;
	}
	

	public GroupContext()
	{
		
	}


	public String getGroupName() {
		return groupName;
	}

	@JsonSetter
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}


	public boolean isActive() {
		return isActive;
	}
	
	@JsonSetter
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}


	public String getGroupId() {
		return groupId;
	}



	@JsonSetter
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}




	public Long getOwnerMobile() {
		return ownerMobile;
	}



	@JsonSetter
	public void setOwnerMobile(Long ownerMobile) {
		this.ownerMobile = ownerMobile;
	}




	public List<Long> getUsers() {
		return users;
	}



	@JsonSetter
	public void setUsers(List<Long> users) {
		this.users = users;
	}




	public String getPassword() {
		return password;
	}



	@JsonSetter
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
	
}




