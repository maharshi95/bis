package test;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/FileCounter")
public class FileCounter extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	int count;
	private FileDAO dao;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		session.setMaxInactiveInterval(5);
		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();
		if(session.isNew()) {
			count++;
		}
		out.println("This Site has been accessed " + count + " times.");
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String user = req.getParameter("username");
		String pass = req.getParameter("password");
		PrintWriter out = resp.getWriter();
		out.println("username: " + user + "\n" + "password:" + pass);	
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

}
