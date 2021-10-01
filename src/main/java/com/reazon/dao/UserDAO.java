package com.reazon.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.reazon.model.User;

// Database Access Object Class {UserDAO}
public class UserDAO {
	
	private String url = "jdbc:mysql://localhost:3308/demo?useSSl=false";
	private String username = "root";
	private String password = "root";
	
	private static final String INSERT_QUERY = "INSERT_QUERY INTO users (name, email, country) VALUES (?, ?, ?);";
	private static final String SELECT_ALL = "select * from users";
	private static final String SELECT_BY_ID = "select id,name,email,country from users where id =?";
	private static final String DELETE_QUERY = "delete from users where id = ?;";
    private static final String UPDATE_QUERY = "update users set name = ?,email= ?, country =? where id = ?;";
    
    
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
//    INSERT_QUERY [User]
    public void insertUser(User user) throws SQLException {
    	Connection conn = getConnection();
    	PreparedStatement preparedStatement = conn.prepareStatement(INSERT_QUERY);
    	
    	preparedStatement.setString(1, user.getName());
    	preparedStatement.setString(2, user.getEmail());
    	preparedStatement.setString(3, user.getCountry());
    	
    	preparedStatement.executeUpdate();
    }
 
//    SELECT [User] entity set
    public List<User> selectAllUsers() throws SQLException{
    	List<User> users = new ArrayList<>();
    	Connection conn = getConnection();
    	PreparedStatement preparedStatement = conn.prepareStatement(SELECT_ALL);
    	
    	ResultSet rs = preparedStatement.executeQuery();
    	
    	while(rs.next()) {
    		users.add(new User(rs.getInt("id"),rs.getString("name"),rs.getString("email"),rs.getString("country")));
    	}
    	
    	return users;
    }
    
//    SELECT [User] BY ID 
    public User getUserByID(int id) throws SQLException {
    	User existingUser = null;
    	Connection conn = getConnection();
    	PreparedStatement preparedStatement = conn.prepareStatement(SELECT_BY_ID);
    	preparedStatement.setInt(1, id);
    	ResultSet rs = preparedStatement.executeQuery();
    	
    	while(rs.next()) {
    		existingUser = new User(rs.getInt("id"),rs.getString("name"),rs.getString("email"),rs.getString("country"));
    	}
    	return existingUser;
    }
    
//    DELETE_QUERY [User]
    public boolean deleteUser(int id) throws SQLException {
    	
    	Connection conn = getConnection();
    	PreparedStatement preparedStatement = conn.prepareStatement(DELETE_QUERY);
    	
    	preparedStatement.setInt(1, id);
    	
    	boolean rowDeleted = preparedStatement.executeUpdate() > 0;
    	
    	return rowDeleted;
    }
    
//    UPDATE [User]
    public boolean updateUser(User existingUser) throws SQLException {
    	
    	Connection conn = getConnection();
    	PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_QUERY);
    	
    	preparedStatement.setInt(4, existingUser.getId());
    	
    	preparedStatement.setString(1, existingUser.getName());
    	preparedStatement.setString(2, existingUser.getEmail());
    	preparedStatement.setString(3, existingUser.getCountry());
    	
    	boolean rowUpdated = preparedStatement.executeUpdate() > 0;
    	
    	return rowUpdated;
    }
}

