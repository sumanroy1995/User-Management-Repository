package com.admin.usermanagement.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.admin.usermanagement.bean.User;

public class UserDao {
	private String jdbcDriver= "com.mysql.cj.jdbc.Driver";
	private String jdbcURL= "jdbc:mysql://localhost:3306/userdb?useSSL=false";
	private String jdbcUsername= "root";
	private String jdbcPassword= "root";
	
	private static final String Insert_Users_SQL= "INSERT INTO users (name, email, country) VALUES (?, ?, ?);";
	private static final String Select_Users_By_ID= "select id, name, email, country from users where id=?";
	private static final String Select_All_Users= "select * from users;";
	private static final String Delete_Users_SQL= "delete from users where id=?;";
	private static final String Update_Users_SQL= "update users set name = ?,email= ?, country =? where id = ?;";
	
	public UserDao() {}
	
	protected Connection getConnection() {
		Connection con= null;
		try {
			Class.forName(jdbcDriver);
			con= DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		return con;
	}
	
	public void insertUser(User user) {
		System.out.println(Insert_Users_SQL);
		Connection con= getConnection();
		try {
			PreparedStatement preparedState= con.prepareStatement(Insert_Users_SQL);
			preparedState.setString(1, user.getName());
			preparedState.setString(2, user.getEmail());
			preparedState.setString(3, user.getCountry());
			System.out.println(preparedState);
			preparedState.executeUpdate();
		}
		catch(SQLException e) {
			printSQLException(e);
		}
	}
	
	public User selectUser(int id) {
		User user= null;
		Connection con= getConnection();
		try {
			PreparedStatement preparedState= con.prepareStatement(Select_Users_By_ID);
			preparedState.setInt(1, id);
			System.out.println(preparedState);
			ResultSet resultSet= preparedState.executeQuery();
			while(resultSet.next()) {
				String name= resultSet.getString("name");
				String email= resultSet.getString("email");
				String country= resultSet.getString("country");
				user= new User(id, name, email, country);	
			}
		}
		catch(SQLException e) {
			printSQLException(e);
		}
		return user;
	}
	
	public List<User> selectAllUsers(){
		List<User> users= new ArrayList<>();
		Connection con= getConnection();
		try {
			PreparedStatement preparedState= con.prepareStatement(Select_All_Users);
			System.out.println(preparedState);
			ResultSet resultSet= preparedState.executeQuery();
			while(resultSet.next()) {
				int id= resultSet.getInt("id");
				String name= resultSet.getString("name");
				String email= resultSet.getString("email");
				String country= resultSet.getString("country");
				users.add(new User(id, name, email, country));	
			}
		}
		catch(SQLException e) {
			printSQLException(e);
		}
		return users;
	}
	
	public boolean deleteUser(int id)  {
		boolean rowDeleted = false;
		Connection connection = getConnection();
		try {
			PreparedStatement preparedState = connection.prepareStatement(Delete_Users_SQL);
			preparedState.setInt(1, id);
			rowDeleted = preparedState.executeUpdate() > 0;	
		}
		catch(SQLException e) {
			printSQLException(e);
		}
		return rowDeleted;
	}
	
	public boolean updateUser(User user) {
		boolean rowUpdated= false;
		Connection connection = getConnection();
		try {
			PreparedStatement preparedState = connection.prepareStatement(Update_Users_SQL);
			System.out.println("update users:"+preparedState);
			preparedState.setString(1, user.getName());
			preparedState.setString(2, user.getEmail());
			preparedState.setString(3, user.getCountry());
			preparedState.setInt(4, user.getId());
			rowUpdated= preparedState.executeUpdate()>0;
		}
		catch(SQLException e) {
			printSQLException(e);
		}
		return rowUpdated;
	}
	
	private void printSQLException(SQLException ex) {
		for (Throwable e : ex) {
			if (e instanceof SQLException) {
				e.printStackTrace(System.err);
				System.err.println("SQLState: " + ((SQLException) e).getSQLState());
				System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
				System.err.println("Message: " + e.getMessage());
				Throwable t = ex.getCause();
				while (t != null) {
					System.out.println("Cause: " + t);
					t = t.getCause();
				}
			}
		}
		
	}

	
}
