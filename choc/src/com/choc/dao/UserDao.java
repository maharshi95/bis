package com.choc.dao;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.choc.exceptions.ObjectNotFoundException;
import com.choc.exceptions.UserAlreadyExistsException;
import com.choc.model.User;
import com.choc.model.UserAddress;
import com.choc.util.DBConnectionPool;
import com.mysql.jdbc.Statement;

import static com.choc.dao.DbSchema.*;

/**
 * Data Access Object(DAO) singleton class for CRUD operations on User.<br>
 * All the operations on User data is done via this class.
 * @author Maharshi
 *
 */
public class UserDao {
	/**
	 * The static singleton instance of UserDAO
	 */
	private static UserDao dao;
	
	/**
	 * MySQL connection object
	 */
	private Connection connection = null;
	private SecureRandom randomGen = null;
	
	private UserDao() {
		connection = DBConnectionPool.getInstance().getConnection();
		randomGen = new SecureRandom();
	}
	
	public static UserDao getInstance() {
		if(dao == null) {
			dao = new UserDao();
		}
		return dao;
	}
	
	/**
	 * This Method is use to get the email_id of the User whose user_id is provided.
	 * @param id: user_id of the user
	 * @return email_id of the user if it exists else null
	 * @throws UserIDNotFoundException
	 */
	public String getEmailByUserID(String id) throws ObjectNotFoundException {
		String email = null;
		try{
			String query = "select " + user_email + " from " + table_users + " where " + user_id + " = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, id);
			ResultSet rs = statement.executeQuery();
			if(rs.next()) {
				email = rs.getString(user_email);
			}
			else {
				throw new ObjectNotFoundException(user_id + " " + id + " does not exist");
			}
		}catch (SQLException e){
			e.printStackTrace();
		}
		return email;
	}
	
	/**
	 * This Method is use to get the user_id of the User whose email_id is provided.
	 * @param email : email_id of the user
	 * @return user_id of the User if it exists else null
	 * @throws UserEmailNotFoundException
	 */
	public String getUserIDByEmail(String email) throws ObjectNotFoundException {
		String id = null;
		try{
			String query = "select " + user_id + " from " + table_users + " where " + user_email + " = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, email);
			ResultSet rs = statement.executeQuery();
			if(rs.next()) {
				id = rs.getString(user_id);
			}
			else {
				throw new Exception();
			}
		}catch (Exception e){
			throw new ObjectNotFoundException(user_id + "\"" + id + "\" does not exist");
		}
		return id;
	}
	
	public Map<String,Object> getUserAttributes(String userID, List<String> attributes) {
		HashMap<String,Object> map = null;
		try {
			String colums = "";
			for(String str : attributes) {
				colums += " ," + str;
			}
			String query = "select " + user_id + colums + " from " + table_users + " where " + user_id + " = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, userID);
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
	
	public boolean userExists(String email) {
		try {
			String querry = "select exists(select 1 from " + table_users + " where " + user_email + " = ?) as cnt";
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
	
	public void setUserHash(String emailID, String hash) {
		String query = "update "  +table_users + " set hashcode = ?  where " + user_email + " = ?";
		try {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, hash);
			statement.setString(2, emailID);
			statement.executeUpdate();
		} catch(SQLException e) {
		}
	}
	
	public void verifyUserByHash(String hash) throws ObjectNotFoundException {
		String query = "update "  +table_users + " set "  +user_verify_bit + " = ? " + " where hashcode = ?";
		try {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setBoolean(1, true);
			statement.setString(2, hash);
			statement.executeUpdate();
			statement.close();
		} catch(SQLException e) {
			throw new ObjectNotFoundException("undeclared hash value : " + hash);
		}
	}
	
	public String getUserHash(String emailID) {
		String query = "select " + user_hashcode + " from " + table_users + " where " + user_email + " = ?";
		String hashcode = null;
		try {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, emailID);
			ResultSet rs = statement.executeQuery();
			if(rs.next()) {
				hashcode = rs.getString(user_hashcode);
			}
			rs.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return hashcode;
	}
	
	public void verifyUser(String email) {
		try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("update " + table_users + " set " + user_verify_bit + " = ? " +
                            "where "+ user_email+" = ?");
            preparedStatement.setBoolean(1, true);
            preparedStatement.setString(2, email);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
	
	public boolean authorizeUser(String email, String password) {
		boolean ret = false;
		try {
		if(userExists(email)) {
			String querry = "select count(" + user_email + ") as cnt from " + table_users + 
					" where " +user_email + " = ? and " + 
							user_password + " = ? and " + 
							user_verify_bit + " = ?";
			
			PreparedStatement statement = connection.prepareStatement(querry);
			statement.setString(1, email);
			statement.setString(2, password);
			statement.setBoolean(3, true);
			System.out.println(statement);
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
		boolean status = authorizeUser(email, password);
		if(status == true) {
			try {
				String querry = "update " + table_users + " set " + user_login_bit + " = true where " + user_email + " = ?";
				PreparedStatement statement = connection.prepareStatement(querry);
				statement.setString(1,email);
				System.out.println(statement);
				statement.executeUpdate();
			} catch(SQLException e) {
				e.printStackTrace();
				status = false;
			}
		}
		return status;
	}
	
	public boolean logoutUser(String email) {
		boolean status = true;
		try {
			String querry = "update " + table_users + " set " + user_login_bit
					+ " = false where " + user_email + " = ?";
			PreparedStatement statement = connection.prepareStatement(querry);
			statement.setString(1, email);
			System.out.println(statement);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			status = false;
		}
		return status;
	}
	
	public void insertUser(User user) throws UserAlreadyExistsException {
		if(userExists(user.getEmailID()))
			throw new UserAlreadyExistsException("User with email_id " + user.getEmailID() + " already exists");
		try {
			String querry = "insert into " + table_users + "("
					+ user_email + ", " 
					+ user_password + ", " 
					+ user_fname + ", " 
					+ user_lname + ", "
					+ user_contact + ", "
					+ user_hashcode
					+ ") values (?, ?, ?, ?, ?, ?)";
			PreparedStatement statement = connection.prepareStatement(querry);
			statement.setString(1, user.getEmailID());
			statement.setString(2, user.getPassword());
			statement.setString(3, user.getFirstname());
			statement.setString(4, user.getLastname());
			statement.setString(5, user.getContact());
			String hashcode = UUID.randomUUID().toString();
			statement.setString(6, hashcode);
			System.out.println(statement.toString());
			statement.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public User getUserByUserID(String userID) {
		User user = new User();
		try {
			String querry = "select * from  " + table_users + " where " + user_id + " = ?";
			PreparedStatement statement = connection.prepareStatement(querry);
			statement.setString(1, userID);
			ResultSet rs = statement.executeQuery();
			
			if(rs.next()) {
				user.setUserID(rs.getString(user_id));
				user.setEmail(rs.getString(user_email));
				user.setFirstName(rs.getString(user_fname));
				user.setLastName(rs.getString(user_lname));
				user.setValid(rs.getBoolean(user_verify_bit));
				user.setLogin(rs.getBoolean(user_login_bit));
				user.setPassword(rs.getString(user_password));
				user.setContact(rs.getString(user_contact));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}
	
	public User getUserByEmailId(String emailId) {
		User user = new User();
		try {
			String querry = "select * from  " + table_users + " where " + user_email + " = ?";
			PreparedStatement statement = connection.prepareStatement(querry);
			statement.setString(1, emailId);
			ResultSet rs = statement.executeQuery();
			
			if(rs.next()) {
				user.setUserID(rs.getString(user_id));
				user.setEmail(rs.getString(user_email));
				user.setFirstName(rs.getString(user_fname));
				user.setLastName(rs.getString(user_lname));
				user.setValid(rs.getBoolean(user_verify_bit));
				user.setLogin(rs.getBoolean(user_login_bit));
				user.setPassword(rs.getString(user_password));
				user.setContact(rs.getString(user_contact));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}
	
	public List<User> getAllUsers() {
		List<User> users = new ArrayList<User>();
		try {
			String querry = "select * from  " + table_users;
			PreparedStatement statement = connection.prepareStatement(querry);
			ResultSet rs = statement.executeQuery();
			
			while(rs.next()) {
				User user = new User();
				user.setUserID(rs.getString(user_id));
				user.setEmail(rs.getString(user_email));
				user.setFirstName(rs.getString(user_fname));
				user.setLastName(rs.getString(user_lname));
				user.setValid(rs.getBoolean(user_verify_bit));
				user.setLogin(rs.getBoolean(user_login_bit));
				user.setPassword(rs.getString(user_password));
				user.setContact(rs.getString(user_contact));
				users.add(user);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return users;
	}
	
	public void updateUser(User user) {
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("update users set "+user_fname+"=?, "+user_lname+"=?, "+user_email+"=?" +
                            "where "+ user_id+"=?");
            preparedStatement.setString(1, user.getFirstname());
            preparedStatement.setString(2, user.getLastname());
            preparedStatement.setString(3, user.getEmailID());
            preparedStatement.setString(4, user.getUserID());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
	public boolean changeUserPassword(String email, String newPassword) {
		boolean success  = true;
		try {
			String querry = "update " +table_users + " set " + user_password + " = ? where " + user_email + " = ?";
			PreparedStatement statement = connection.prepareStatement(querry);
			statement.setString(1, newPassword);
			statement.setString(2, email);
			System.out.println(statement);
			statement.executeUpdate();
		} catch(SQLException e) {
			success = false;
			e.printStackTrace();
		}
		return success;
	}
	public boolean modifyUserProp(String userID, Map<String,Object> map) {
		boolean success = true;
		try {
			ArrayList<String> keys = new ArrayList<String>();
			ArrayList<Object> values = new ArrayList<Object>();
			
			for(String key : map.keySet()) {
				keys.add(key);
				values.add(map.get(key));
			}
			
			String column = "";
			for(int i=0; i<keys.size()-1; i++) {
				column += keys.get(i) + " = ?, ";
			}
			column += keys.get(keys.size()-1) +" = ?";
			
			String querry = "update " + table_users + " set " + column +" where " + user_id + " = ?" ;
			PreparedStatement statement = connection.prepareStatement(querry);
			for(int i=0; i<values.size(); i++){
				statement.setObject(i+1, values.get(i));
			}
			statement.setObject(values.size()+1, userID);
			
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			success = false;
		}
		return success;
	}
	
	public List<UserAddress> getUserAddresses(String userID) {
		List<UserAddress> addresses = new ArrayList<UserAddress>();
		try {
			String query = "select * from "  +table_addr + " where " + addr_user_id + " = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, userID);
			ResultSet rs = statement.executeQuery();
			
			UserAddress addr = new UserAddress();
			while(rs.next()) {
				addr.setUserAddrID(rs.getString(addr_address_id));
				addr.setUserID(rs.getString(addr_user_id));
				addr.setName(rs.getString(addr_name));
				addr.setStreet1(rs.getString(addr_street1));
				addr.setStreet2(rs.getString(addr_street2));
				addr.setCity(rs.getString(addr_city));
				addr.setState(rs.getString(addr_state));
				addr.setCountry(rs.getString(addr_country));
				addr.setPincode(rs.getString(addr_pincode));
				addr.setContactNo(rs.getString(addr_contact));
				addresses.add(addr);
			} 
			
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return addresses;
	}
	
	public void insertUserAddress(String userID, UserAddress address) {
		try {
			String query = "insert into " + table_addr + "("
					+ addr_user_id + ", "
					+ addr_name + ", " 
					+ addr_street1 + ", " 
					+ addr_street2 + ", " 
					+ addr_city + ", "
					+ addr_state + ", "
					+ addr_country + ", "
					+ addr_pincode + ", "
					+ addr_contact
					+ ") values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, userID);
			statement.setString(2, address.getName());
			statement.setString(3, address.getStreet1());
			statement.setString(4, address.getStreet2());
			statement.setString(5, address.getCity());
			statement.setString(6, address.getState());
			statement.setString(7, address.getCountry());
			statement.setString(8, address.getPincode());
			statement.setString(9, address.getContactNo());
			System.out.println(statement);
			statement.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public UserAddress getUserAddressByAddressID(String addressID) {
		UserAddress addr = new UserAddress();
		try {
			String query = "select * from " + table_addr + " where " + addr_address_id + " = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, addressID);
			ResultSet rs = statement.executeQuery();
			if(rs.next()) {
				addr.setUserAddrID(rs.getString(addr_address_id));
				addr.setUserID(rs.getString(addr_user_id));
				addr.setName(rs.getString(addr_name));
				addr.setStreet1(rs.getString(addr_street1));
				addr.setStreet2(rs.getString(addr_street2));
				addr.setCity(rs.getString(addr_city));
				addr.setState(rs.getString(addr_state));
				addr.setCountry(rs.getString(addr_country));
				addr.setPincode(rs.getString(addr_pincode));
				addr.setContactNo(rs.getString(addr_contact));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return addr;
	}
	
	public boolean isProductInWishList(String userID, String productID) {
		boolean present = false;
		try {
			String query = "select * from " + table_users_wishlist + " where " + user_id + " = ? and " + product_id + " = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, userID);
			statement.setString(2, productID);
			ResultSet rs = statement.executeQuery();
			if(rs.next()) {
				present = true;
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return present;
	}
	
	public void removeProductFromWishList(String userID, String productID) {
		try {
			String query = "delete from " + table_users_wishlist + " where " + user_id + " = ? and " + product_id + " = ?" ;
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, userID);
			statement.setString(2, productID);
			statement.executeUpdate();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addProductToWishList(String userID, String productID) {
		try {
			String query = "insert into " + table_users_wishlist + "("
								+ user_id + ", "
								+ product_id + ") "
								+ "values(?, ?)";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, userID);
			statement.setString(2, productID);
			statement.executeUpdate();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<String> getProductIDsFromWishlist(String userID) {
		List<String> productIDs = new ArrayList<String>();
		try {
			String query = "select " + product_id + " from " + table_users_wishlist + " where " + user_id + " = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, userID);
			ResultSet rs = statement.executeQuery();
			
			while(rs.next()) {
				String pid = rs.getString(product_id);
				productIDs.add(pid);
			}
			rs.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return productIDs;
	}
}
