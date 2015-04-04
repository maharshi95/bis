package com.choc.model;

public abstract class Address {

	private String name;
	private String street1;
	private String street2;
	private String city;
	private String state;
	private String country;
	private String pincode;
	private String contactNo;
	
	public Address() {
		
	}
	public Address(String[] params) {
		setName(params[2]);
		setStreet1(params[3]);
		setStreet2(params[4]);
		setCity(params[5]);
		setState(params[6]);
		setCountry(params[7]);
		setPincode(params[8]);
		setContactNo(params[9]);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStreet1() {
		return street1;
	}

	public void setStreet1(String street1) {
		this.street1 = street1;
	}

	public String getStreet2() {
		return street2;
	}

	public void setStreet2(String street2) {
		this.street2 = street2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}
}
