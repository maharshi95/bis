package com.choc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.choc.exceptions.ObjectNotFoundException;
import com.choc.exceptions.UserAlreadyExistsException;
import com.choc.model.Seller;
import com.choc.util.DBConnectionPool;

import static com.choc.dao.DbSchema.*;
public class SellerDAO {
	
	private static SellerDAO sellerDAO;
	private Connection connection;
	
	private SellerDAO() {
		connection = DBConnectionPool.getInstance().getConnection();
	}
	
	public static SellerDAO getInstance() {
		if(sellerDAO == null)
			sellerDAO = new SellerDAO();
		return sellerDAO;
	}
	
	public String getEmailIDBySellerID(String sellerID) throws ObjectNotFoundException {
		String email = null;
		try {
			String query = "select " + seller_email + " from " + table_sellers + " where " + seller_id + " = ?";
			PreparedStatement statment = connection.prepareStatement(query);
			statment.setString(1, sellerID);
			ResultSet rs  = statment.executeQuery();
			if(rs.next()) {
				email = rs.getString(seller_email);
			}
			else {
				throw new ObjectNotFoundException("SellerID '" + sellerID + "' not found");
				
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return email;
	}
	
	public String getSellerIDByEmailID(String emailID) throws ObjectNotFoundException {
		String sellerID = null;
		try {
			String query = "select " + seller_id +" from " + table_sellers + " where " + seller_email + " = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, emailID);
			ResultSet rs = statement.executeQuery();
			if(rs.next()) {
				sellerID = rs.getString(seller_id);
			}
			else {
				throw new ObjectNotFoundException("SellerEmail '" + emailID + "' not found");
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return sellerID;
	}
	
	public Seller getSellerBySellerID(String sellerID) throws ObjectNotFoundException {
		Seller seller = new Seller();
		try {
			String query = "select * from " + table_sellers  +" where " + seller_id + " = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, sellerID);
			ResultSet rs = statement.executeQuery();
			if(rs.next()) {
				seller.setSellerID(sellerID);
				seller.setEmailID(rs.getString(seller_email));
				seller.setName(rs.getString(seller_name));
				seller.setPassword(seller_password);
				seller.setContactNo(seller_contact);
				seller.setVerify(rs.getBoolean(seller_verify_bit));
				seller.setLogin(rs.getBoolean(seller_login_bit));
			}
			else {
				throw new ObjectNotFoundException("SellerID '" + sellerID + "' not found");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return seller;
	}
	
	public Map<String,Object> getSellerAttributes(String sellerID, List<String> attributes) {
		HashMap<String,Object> map = null;
		try {
			String colums = "";
			for(String str : attributes) {
				colums += " ," + str;
			}
			String query = "select " + seller_id + colums + " from " + table_sellers + " where " + seller_id + " = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, sellerID);
			ResultSet rs = statement.executeQuery();
			if(rs.next()) {
				map = new HashMap<String, Object>();
				for(String str : attributes) {
					map.put(str, rs.getObject(str));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public List<Seller> getAllSellers() {
		List<Seller> sellers = new ArrayList<Seller>();
		try {
			String querry = "select * from  " + table_sellers;
			PreparedStatement statement = connection.prepareStatement(querry);
			ResultSet rs = statement.executeQuery();
			
			while(rs.next()) {
				Seller seller = new Seller();
				seller.setSellerID(rs.getString(seller_id));
				seller.setEmailID(rs.getString(seller_email));
				seller.setPassword(rs.getString(seller_password));
				seller.setName(rs.getString(seller_name));
				seller.setVerify(rs.getBoolean(seller_verify_bit));
				seller.setLogin(rs.getBoolean(seller_login_bit));
				seller.setContactNo(rs.getString(seller_contact));
				sellers.add(seller);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return sellers;
	}
	
	public boolean sellerExists(String email) {
		try {
			String querry = "select exists(select 1 from " + table_sellers + " where " + seller_email + " = ?) as cnt";
			PreparedStatement statement = connection.prepareStatement(querry);
			statement.setString(1, email);
			ResultSet rs = statement.executeQuery();
			if(rs.next()) {
				if(rs.getInt("cnt") == 1)
					return true;
				else
					return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void verifySeller(String email) {
		try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("update " + table_sellers + " set " + user_verify_bit + " = ? " +
                            "where "+ user_email+" = ?");
            preparedStatement.setBoolean(1, true);
            preparedStatement.setString(2, email);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
	
	public boolean authorizeSeller(String email, String password) {
		boolean ret = false;
		try {
			if(sellerExists(email)) {
				String querry = "select count(" + seller_email + ") as cnt from " + table_sellers + " where " +seller_email + " = ? and " + seller_password + " =  ?";
				PreparedStatement statement = connection.prepareStatement(querry);
				statement.setString(1, email);
				statement.setString(2, password);
				ResultSet rs = statement.executeQuery();
				if(rs.next()) {
					if(rs.getInt("cnt") == 1)
						ret = true;
				}
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	public boolean loginUser(String email, String password) {
		boolean status = authorizeSeller(email, password);
		if(status == true) {
			try {
				String querry = "update " + table_sellers + " set " + seller_login_bit + " = true where " + seller_email + " = ?";
				PreparedStatement statement = connection.prepareStatement(querry);
				statement.setString(1,email);
				statement.executeUpdate();
			} catch(SQLException e) {
				e.printStackTrace();
				status = false;
			}
		}
		return status;
	}
	
	public boolean logoutUser(String email, String password) {
		boolean status = authorizeSeller(email, password);
		if(status == true) {
			try {
				String querry = "update " + table_sellers + " set " + seller_login_bit + " = false where " + user_email + " = ?";
				PreparedStatement statement = connection.prepareStatement(querry);
				statement.setString(1,email);
				statement.executeUpdate();
			} catch(SQLException e) {
				e.printStackTrace();
				status = false;
			}
		}
		return status;
	}
	
	public void insertSeller(Seller seller) throws UserAlreadyExistsException {
		if(sellerExists(seller.getEmailID()))
			throw new UserAlreadyExistsException("Seller with email_id " + seller.getEmailID() + " already exists");
		
		try {
			String querry = "insert into " + table_sellers + "("
					+ seller_email + ", " 
					+ seller_password + ", " 
					+ seller_name + ", " 
					+ seller_contact
					+ ") values (?, ?, ?, ?)";
			PreparedStatement statement = connection.prepareStatement(querry);
			statement.setString(1, seller.getEmailID());
			statement.setString(2, seller.getPassword());
			statement.setString(3, seller.getName());
			statement.setString(5, seller.getContactNo());
			
			System.out.println(statement.toString());
			statement.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
