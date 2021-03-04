package com.user.request;

public class UserRequest {
	
	
	private String userName;

	public String getUserName() {
		System.out.println("i am working here " + this.userName);
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	

}
