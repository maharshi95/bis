package com.choc.model;

public class SellerAddress extends Address {
	
	public static final int N_PARAMS = 10;
	
	private String sellerID;
	private String sellerAddrID;
	
	public SellerAddress() {
	
	}
	
	public SellerAddress(String[] params) {
		super(params);
		setSellerAddrID(params[0]);
		setSellerID(params[1]);
	}
	
	public String getSellerID() {
		return sellerID;
	}
	
	public void setSellerID(String sellerID) {
		this.sellerID = sellerID;
	}
	
	public String getSellerAddrID() {
		return sellerAddrID;
	}
	
	public void setSellerAddrID(String sellerAddrID) {
		this.sellerAddrID = sellerAddrID;
	}
	
	@Override
	public String toString() {
		return getSellerAddrID() + "\n"
				+ getSellerID() + "\n"
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
