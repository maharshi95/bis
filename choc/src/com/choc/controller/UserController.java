package com.choc.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.choc.dao.UserDao;
import com.choc.exceptions.UserAlreadyExistsException;
import com.choc.model.User;

import test.FileDAO;
import static com.choc.dao.DbSchema.*;

@WebServlet("/ronak")
/**
 * Unused Class
 * only used for code reference
 * to be deleted
 *
 */

public class UserController extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private static String INSERT_OR_EDIT = "./user.jsp";
	private static String LIST_USER = "./listUser.jsp";
	private UserDao userDao;
	
	public UserController() {
		super();
		userDao = UserDao.getInstance();
	}
	
	private int count;
	private FileDAO dao;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = req.getSession(true);
		session.setMaxInactiveInterval(5);
		resp.setContentType("text/plain");
		PrintWriter out = resp.getWriter();
		if(session.isNew()) {
			count++;
		}
		out.println("This Site has been accessed " + count + " times.");
	}
	
	public void init() throws ServletException {
		dao = new FileDAO();
		try {
			count = dao.getFileCount();
		} catch(Exception e) {
			getServletContext().log("An exception occured in File COunter");
			throw new ServletException("An exception occured in File Counter " + e.getMessage());
		}
	}
	
	public void destroy() {
		super.destroy();
		try {
			dao.save(count);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		PrintWriter out = resp.getWriter();
		resp.setContentType("text/plain");
		String type = req.getParameter("type");
		
		if(type.equals("login")) {
			String user = req.getParameter("username");
			String pass = req.getParameter("password");
			out.println("username: " + user + "\n" + "password:" + pass);
			if(userDao.authorizeUser(user, pass)) {
				userDao.loginUser(user, pass);
				out.println("User Successfully logged in");
			}
			else {
				out.println("Wrong emailID or password!!");
			}
		}
		
		else if(type.equals("signup")){
			String firstname = req.getParameter("fname");
			String lastname = req.getParameter("lname");
			String emailID = req.getParameter("email");
			String password = req.getParameter("pass");
			String contactNo = req.getParameter("contact");
			
			User user = new User();
			user.setFirstName(firstname);
			user.setLastName(lastname);
			user.setEmail(emailID);
			user.setPassword(password);
			user.setContact(contactNo);
			try {
				userDao.insertUser(user);
				out.println("User successfully inserted... please verify the registeration by clicking the link sent to you email");
				out.println("Thanks \nChocolatte \n");
			} catch (UserAlreadyExistsException e) {
				out.println("User with emailID "+ emailID +" already exists!!");
				e.printStackTrace();
			}
		}
	}
}
