package com.reazon.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.reazon.model.User;



public class UserDAO {
	private String url = "jdbc:mysql://localhost:3308/demo?useSSl=false";
	private String username = "root";
	private String password = "root";
	
	private static final String INSERT_USERS_SQL = "INSERT INTO users" + "  (name, email, country) VALUES " +
	        " (?, ?, ?);";
	private static final String SELECT_USER_BY_ID = "select id,name,email,country from users where id =?";
	
	private static final String SELECT_ALL_USERS = "select * from users";
	private static final String DELETE_USERS_SQL = "delete from users where id = ?;";
    private static final String UPDATE_USERS_SQL = "update users set name = ?,email= ?, country =? where id = ?;";
    
    
    protected Connection getConnection() {
    	Connection conn = null;
    	
    	try {
    		Class.forName("com.mysql.cj.jdbc.Driver");
    		conn = DriverManager.getConnection(url,username,password);
    	}catch(SQLException | ClassNotFoundException e) {
    		e.printStackTrace();
    	}
    	
    	return conn;
    }
    
    public void insertUser(User user) throws SQLException {
    	Connection conn = getConnection();
    	PreparedStatement preparedStatement = conn.prepareStatement(INSERT_USERS_SQL);
    	
    	preparedStatement.setString(1, user.getName());
    	preparedStatement.setString(2, user.getEmail());
    	preparedStatement.setString(3, user.getCountry());
    	
    	preparedStatement.executeUpdate();
    }
 
    public List<User> selectAllUsers() throws SQLException{
    	List<User> users = new ArrayList<>();
    	Connection conn = getConnection();
    	PreparedStatement preparedStatement = conn.prepareStatement(SELECT_ALL_USERS);
    	
    	ResultSet rs = preparedStatement.executeQuery();
    	
    	while(rs.next()) {
    		users.add(new User(rs.getInt("id"),rs.getString("name"),rs.getString("email"),rs.getString("country")));
    	}
    	
    	return users;
    }
}

