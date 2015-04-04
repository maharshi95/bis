package com.choc.util;

import java.util.HashMap;
import java.util.UUID;

public class SessionManager {
	private static SessionManager sessionManager = new SessionManager();
	
	private HashMap<String, String> sessionMap;
	
	public static SessionManager getInstance() {
		return sessionManager;
	}
	
	private SessionManager() {
		sessionMap = new HashMap<String, String>();
	}
	
	public String createSession(String emailID) {
		String uuid = UUID.randomUUID().toString();
		sessionMap.put(uuid, emailID);
		return uuid;
	}
	
	public String getEmailID(String sessionID) {
		return sessionMap.get(sessionID);
	}
	
	public void destroySession(String uuid) {
		sessionMap.remove(uuid);
	}
	
	public boolean sessionExists(String uuid) {
		return sessionMap.containsValue(uuid);
	}
	
}
