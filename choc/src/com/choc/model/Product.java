package com.choc.model;

public class Product {
	private String productID;
	private String brandName;
	private String productName;
	private String category;
	private int quantity;
	private float base_cost;
	private float discount;
	private String sellerID;
	private String sellerName;
	private String tagID;
	private String tagName;
	
	public Product() {
		
	}
	
	public String getProductID() {
		return productID;
	}
	
	public void setProductID(String productID) {
		this.productID = productID;
	}
	
	public String getBrandName() {
		return brandName;
	}
	
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	
	public String getProductName() {
		return productName;
	}
	
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int total_quantity) {
		this.quantity = quantity;
	}

   public float getBase_cost()
   {
      return base_cost;
   }

   public void setBase_cost(float base_cost)
   {
      this.base_cost = base_cost;
   }

   public float getDiscount()
   {
      return discount;
   }

   public void setDiscount(float discount)
   {
      this.discount = discount;
   }

   public String getSellerID()
   {
      return sellerID;
   }

   public void setSellerID(String sellerID)
   {
      this.sellerID = sellerID;
   }

   public String getSellerName()
   {
      return sellerName;
   }

   public void setSellerName(String sellerName)
   {
      this.sellerName = sellerName;
   }

   public String getTagID()
   {
      return tagID;
   }

   public void setTagID(String tagID)
   {
      this.tagID = tagID;
   }

   public String getTagName()
   {
      return tagName;
   }

   public void setTagName(String tagName)
   {
      this.tagName = tagName;
   }

   
}
