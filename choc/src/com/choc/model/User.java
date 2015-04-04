package com.choc.model;

public class User {
	
	private String user_id;
	private String email_id;
	private String password;
	private String firstname;
	private String lastname;
	private boolean verified;
	private boolean login;
	private String contact;
	
	public User() {
		
	}
	
	public User(String email, String fname, String lname) {
		email_id = email;
		firstname = fname;
		lastname = lname;
	}
	
	public String getUserID() {
		return user_id;
	}
	
	public String getEmailID() {
		return email_id;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getFirstname() {
		return firstname;
	}
	
	public String getLastname() {
		return lastname;
	}

	public String getContact() {
		return contact;
	}
	
	public boolean isVerified() {
		return verified;
	}
	
	public boolean isLoggedIn() {
		return login;
	}
	
	public void setUserID(String id) {
		user_id = id;
	}
	
	public void setEmail(String email) {
		email_id = email;
	}
	
	public void setPassword(String pass) {
		password = pass;
	}
	
	public void setFirstName(String fname) {
		firstname = fname;
	}
	
	public void setLastName(String lname) {
		lastname = lname;
	}
	
	public void setValid(boolean enabled) {
		verified = enabled;
	}
	
	public void setLogin(boolean enabled) {
		login = enabled;
	}
	
	public void setContact (String number) {
		contact = number;
	}
	
	public String toString() {
		return "user_id\t: " + user_id  
				+ "\nemail_id: " + email_id
				+ "\npassword: " + password
				+ "\nName: " + firstname + " " + lastname 
				+ "\n" + (verified == true ? "verified" : "unverified") 
				+ "\n" + (login == true ? "logged in" : "logged out") 
				+ "\ncontact: "  + contact; 
	}
}
