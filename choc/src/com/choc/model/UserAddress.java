package com.choc.model;

public class UserAddress extends Address {

	public static final int N_PARAMS = 10;
	private String userID;
	private String userAddrID;
	
	public UserAddress() {

	}
	
	public UserAddress(String[] params) {
		super(params);
		setUserAddrID(params[0]);
		setUserID(params[1]);
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getUserAddrID() {
		return userAddrID;
	}

	public void setUserAddrID(String userAddrID) {
		this.userAddrID = userAddrID;
	}
	
	@Override
	public String toString() {
		return getUserAddrID() + "\n" 
				+ getUserID() + "\n"
				+ getName() +"\n"
				+ getStreet1() + "\n"
				+ getStreet2() + "\n"
				+ getCity() + "\n"
				+ getState() + "\n"
				+ getCountry() + "\n"
				+ getPincode() + "\n"
				+ getContactNo() + "\n";
	}
}