package com.choc.model;

public class Seller implements Cloneable{

	private int autoID;
	private String sellerID;
	private String emailID;
	private String password;
	private String name;
	private String contactNo;
	
	private boolean verify;
	private boolean login;
	
	public Seller() {
		
	}
	
	public int getAutoID() {
		return autoID;
	}
	
	public String sellerID() {
		return sellerID;
	}
	
	public void setSellerID(String sellerID) {
		this.sellerID = sellerID;
	}

	public String getEmailID() {
		return emailID;
	}

	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public boolean isVerify() {
		return verify;
	}

	public void setVerify(boolean verify) {
		this.verify = verify;
	}

	public boolean isLogin() {
		return login;
	}

	public void setLogin(boolean login) {
		this.login = login;
	}
}
