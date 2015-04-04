package com.choc.dao;

public class DbSchema {

	public static final String db_name = "bisdb";
	public static final String db_username = "root";
	public static final String db_password = "root";

	/**
	 * Table names in DB schema
	 */
	public static final String table_users = "table_users";
	public static final String table_addr = "table_users_address";
	public static final String table_users_objects = "table_users_objects";
	public static final String table_users_carts = "table_users_carts";
	public static final String table_users_wishlist = "table_users_wishlist";
	public static final String table_users_products_likes = "table_users_products_likes";
	public static final String table_users_feedback = "table_users_feedback";

	public static final String table_products = "table_products";
	public static final String table_products_tags = "table_products_tags";

	public static final String table_sellers = "table_sellers";
	public static final String table_seller_addr = "table_sellers_address";
	public static final String table_seller_products = "table_sellers_products";

	public static final String table_orders = "table_orders";
	public static final String table_orders_items = "table_order_items";

	public static final String table_tags = "table_tags";

	public static final String user_auto_id = "auto_id";
	public static final String user_id = "user_id";
	public static final String user_email = "email_id";
	public static final String user_password = "pass";
	public static final String user_fname = "fname";
	public static final String user_lname = "lname";
	public static final String user_verify_bit = "verify_bit";
	public static final String user_login_bit = "login_bit";
	public static final String user_contact = "contact_no";
	public static final String user_hashcode = "hashcode";

	public static final String addr_user_id = "user_id";
	public static final String addr_auto_id = "auto_id";
	public static final String addr_address_id = "address_id";
	public static final String addr_name = "name";
	public static final String addr_street1 = "street_1";
	public static final String addr_street2 = "street_2";
	public static final String addr_city = "city";
	public static final String addr_state = "state";
	public static final String addr_country = "country";
	public static final String addr_pincode = "pincode";
	public static final String addr_contact = "contact_no";

	public static final String seller_auto_id = "auto_id";
	public static final String seller_id = "seller_id";
	public static final String seller_email = "email_id";
	public static final String seller_password = "pass";
	public static final String seller_name = "name";
	public static final String seller_verify_bit = "verify_bit";
	public static final String seller_login_bit = "login_bit";
	public static final String seller_contact = "contact_no";

	public static final String product_auto_id = "auto_id";
	public static final String product_id = "product_id";
	public static final String product_brand_name = "brand_name";
	public static final String product_name = "product_name";
	public static final String product_category = "category";
	public static final String product_quantity = "quantity";
	public static final String product_total_quantity = "total_quantity";
	public static final String product_seller_id = "seller_id";
	public static final String product_seller_name = "name";
	public static final String product_base_cost = "base_cost";
	public static final String product_discount = "discount";
	public static final String product_tag_id = "tag_id";
	public static final String product_tag_name = "tag_name";

	public static final String seller_addr_id = "seller_id";
	public static final String order_id = "order_id";
	public static final String order_state = "state";
	public static final String order_cancelled = "cancelled";
	public static final String pkg_id = "pkg_id";

	public static final String prefix_user_id = "U";

}
