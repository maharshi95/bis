package com.choc.model;

import java.util.List;

public class WishList {
	private String userID;
	private List<String> productIDs;
	
	public WishList() {
		
	}
	
	public String getUserID() {
		return userID;
	}
	
	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	public void addProductID(String pID) {
		productIDs.add(pID);
	}
	
	public List<String> list() {
		return productIDs;
	}
}
