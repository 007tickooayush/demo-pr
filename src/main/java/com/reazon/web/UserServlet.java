package com.reazon.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.reazon.dao.UserDAO;
import com.reazon.model.User;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private UserDAO userDAO;
    private Gson gson;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserServlet() {
       userDAO = new UserDAO();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
//    :::::::::::::::::::API CALLBACKs:::::::::::::::::::
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		response.getWriter().append("{response: \"Use POST request instead of GET\"}");
		
		String action = request.getServletPath();
		try {
			switch(action) {
			case "/home":
				showAllUsers(request, response);
				break;
			case "/user":
				getUserById(request, response);
				break;
			default:
				showAllUsers(request, response);
				break;
			}
		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			insertUser(request, response);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException,IllegalStateException {
		
		try {
			
			String action = req.getServletPath();
			
			switch(action) {
			case "/duser":
				deleteUser(req, resp);
				break;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		try {
			
			String action = req.getServletPath();
			
			switch(action) {
			case "/user":
				updateUser(req, resp);
				break;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
//	X:::::::::::::::::::API CALLBACKs:::::::::::::::::::X
	
	
//	:::::::::::::::::::AUXILLARY FUNCTIONS::::::::::::::::::: 
	private void insertUser(HttpServletRequest req,HttpServletResponse resp) throws SQLException, IOException {
		String name = req.getParameter("name");
		String email = req.getParameter("email");
		String country = req.getParameter("country");
		
		userDAO.insertUser(new User(name,email,country));
		
		resp.getWriter().append("DATA INSERTED");
		resp.sendRedirect("home");
		
	}
	
	private void showAllUsers(HttpServletRequest req,HttpServletResponse resp) throws SQLException, IOException {
		List<User> users = userDAO.selectAllUsers();
		
		gson = new Gson();
		
		String json = gson.toJson(users);
		resp.setContentType("application/json");
		resp.getWriter().write(json);
				
	}

	private void deleteUser(HttpServletRequest req,HttpServletResponse resp) throws SQLException, IOException {
		int id = Integer.parseInt(req.getParameter("id"));
		
		userDAO.deleteUser(id);
		
		resp.sendRedirect("home");
		
	}
	
	private void getUserById(HttpServletRequest req,HttpServletResponse resp) throws SQLException, IOException {
		int id = Integer.parseInt(req.getParameter("id"));
		User user = userDAO.getUserByID(id);
		
		gson = new Gson();
		
		String json = gson.toJson(user);
		resp.setContentType("application/json");
		resp.getWriter().print(json);
				
	}
	
	private void updateUser(HttpServletRequest req,HttpServletResponse resp) throws SQLException, IOException {
		int id = Integer.parseInt(req.getParameter("id"));
		String name = req.getParameter("name");
		String email = req.getParameter("email");
		String country = req.getParameter("country");
		
		User existingUser = new User(id,
				name,
				email,
				country
				);
		userDAO.updateUser(existingUser);
		
		gson = new Gson();
		
//		UPDATED [User]
		String json = gson.toJson(existingUser);
		resp.setContentType("application/json");
		resp.getWriter().print(json);
	}
//	X:::::::::::::::::::AUXILLARY FUNCTIONS:::::::::::::::::::X
}
