package com.choc.controller;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONObject;

import test.MailTest;

import com.choc.dao.UserDao;
import com.choc.exceptions.BadJsonException;
import com.choc.exceptions.UserAlreadyExistsException;
import com.choc.model.User;
import com.choc.util.SessionManager;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

@Path("/api")
public class LoginControl {

	@GET
	@Path("multiply/{a}/{b}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Long multiply(@PathParam("a") Long a, @PathParam("b") Long b) {
		System.out.println("multiply:A: " + a + " B:" + b);
		return a * b;
	}
	
	@GET
	@Path("verify/{hash}")
	public void validateUser(@PathParam("hash") String hash) {
		try {
			UserDao dao = UserDao.getInstance();
			dao.verifyUserByHash(hash);
			System.out.println("user verified!!");
		} catch (Exception e) {
			System.out.println("Unknown Hash value recieved!!");
		}
	}

	@POST
	@Path("/login")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String loginUser(String jsonReq) {
		String emailID = "";
		String password = "";
		HashMap<String, String> map = new HashMap<String, String>();
		boolean badJSON = false;
		try {
			JSONObject jobj = new JSONObject(jsonReq);
			System.out.println(jobj);
			emailID = jobj.getString("username");
			password = jobj.getString("password");
		} catch (Exception e) {
			map.put("status", "failure");
			map.put("message", "Bad Json");
			badJSON = true;
		}
		if (!badJSON) {
			SessionManager sessionManager = SessionManager.getInstance();
			UserDao dao = UserDao.getInstance();
			boolean success = dao.loginUser(emailID, password);
			if (success) {
				User user = dao.getUserByEmailId(emailID);
				map.put("status", "success");
				map.put("message", "User login successful");
				map.put("session_id", sessionManager.createSession(emailID));
				map.put("name", user.getFirstname() + " " + user.getLastname());
				System.out.println(map.get("session_id"));
			} else {
				map.put("status", "failure");
				map.put("message", "invalid credentials");
			}
		}
		return new JSONObject(map).toString();
	}
	
	@POST
	@Path("/logout")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String logutUser(String jsonReq) {
		String emailID = "";
		String sessionID = "";
		HashMap<String, String> map = new HashMap<String, String>();
		boolean badJSON = false;
		try {
			JSONObject jobj = new JSONObject(jsonReq);
			System.out.println(jobj);
			sessionID = jobj.getString("session_id");
		} catch (Exception e) {
			map.put("status", "failure");
			map.put("message", "Bad Json");
			badJSON = true;
		}
		if (!badJSON) {
			SessionManager sessionManager = SessionManager.getInstance();
			UserDao dao = UserDao.getInstance();
			emailID = sessionManager.getEmailID(sessionID);
			boolean success = dao.logoutUser(emailID);
			if (success) {
				map.put("status", "success");
				map.put("message", "User logout successful");
				sessionManager.destroySession(sessionID);
			} else {
				map.put("status", "failure");
				map.put("message", "sessionID expired");
			}
		}
		return new JSONObject(map).toString();
	}
	
	@POST
	@Path("/signup")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public void signupUser(String jsonReq){
		boolean badJSON = false;
		User user = new User();
		try {
			JSONObject json = new JSONObject(jsonReq);
			user.setEmail(json.getString("email_id"));
			user.setPassword(json.getString("password"));
			user.setFirstName(json.getString("firstname"));
			user.setLastName(json.getString("lastname"));
			user.setContact(json.getString("contact_no"));
		}
		catch (Exception e) {
			System.out.println("BAD JSON!!");
			badJSON = true;
		}
		
		if(!badJSON) {
			System.out.println("JSON Accepted");
			UserDao dao = UserDao.getInstance();
			boolean userInserted = true;
			try {
				dao.insertUser(user);
			} catch (UserAlreadyExistsException e) {
				System.out.println("EMail id already used!!");
				userInserted  = false;
			}
			if(userInserted) {
				String hash = dao.getUserHash(user.getEmailID());
				String link = "http://localhost:8080/com.sample/rest/api/verify/" + hash;
				MailTest.sendVerificationMail(user.getEmailID(), link);
			}
		}
	}

}
