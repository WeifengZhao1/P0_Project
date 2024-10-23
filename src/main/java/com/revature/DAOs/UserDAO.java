package com.revature.DAOs;

import com.revature.models.Users;
import com.revature.utils.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;

public class UserDAO implements UsersDAOInterface {
    @Override
    public Users insertUser(Users user) {
        try (Connection conn = ConnectionUtil.getConnection()) {
            String sql = "INSERT INTO users(first_name, last_name, username, email, password) VALUES(?, ?, ?, ?, ?) RETURNING user_id";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user.getFirst_name());
            ps.setString(2, user.getLast_name());
            ps.setString(3, user.getUsername());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getPassword());

            // Execute the statement and retrieve the generated user_id
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // Get the generated user_id and set it in the user object
                user.setUser_id(rs.getInt("user_id")); // Set the generated ID
            }

            return user;

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Couldn't insert User");
        }
        return null;
    }

    public boolean isUsernameTaken(String username) {
        String sql = "SELECT COUNT(*) FROM Users WHERE username = ?";
        try (Connection conn = ConnectionUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) > 0; // Return true if username exists
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isEmailTaken(String email) {
        String sql = "SELECT COUNT(*) FROM Users WHERE email = ?";
        try (Connection conn = ConnectionUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) > 0; // Return true if email exists
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public Users getUserById(int id) {
        try (Connection conn = ConnectionUtil.getConnection()) {
            // Correct the SQL query to use user_id
            String sql = "SELECT * FROM users WHERE user_id = ?"; // Corrected here
            PreparedStatement ps = conn.prepareStatement(sql); // Changed to prepareStatement

            // Set the id parameter
            ps.setInt(1, id);

            // Execute the query
            ResultSet rs = ps.executeQuery(); // executing the query stored in the PreparedStatement

            // Check if the ResultSet contains any data
            if (rs.next()) {
                // Create a Users object from the ResultSet
                Users user = new Users(
                        rs.getInt("user_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("username"),
                        rs.getString("email")
                );
                return user; // Return the user object
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log the exception to the console
            System.out.println("Couldn't retrieve User by ID: " + id);
        }

        return null; // Return null if no user is found or if an error occurs
    }


    @Override
    public ArrayList<Users> getAllUsers() {
        ArrayList<Users> usersList = new ArrayList<>();

        try (Connection conn = ConnectionUtil.getConnection()) {
            // SQL query to select all users
            String sql = "SELECT * FROM users order by user_id";

            // Create a PreparedStatement
            PreparedStatement ps = conn.prepareStatement(sql);

            // Execute the query and get the ResultSet
            ResultSet rs = ps.executeQuery();

            // Loop through the ResultSet and create Users objects
            while (rs.next()) {
                Users user = new Users(
                        rs.getInt("user_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("username"),
                        rs.getString("email")
                );
                usersList.add(user); // Add the user to the list
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Couldn't retrieve all Users");
        }

        return usersList; // Return the list of users
    }


    @Override
    public String updateUserName(int id,String newUserName) {
        try (Connection conn = ConnectionUtil.getConnection()) {
            String sql = "UPDATE users SET username = ? WHERE user_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, newUserName); // Set the new username
            ps.setInt(2, id); // Use the user ID to find the correct user

            // Execute the update
            ps.executeUpdate();

            return newUserName; // Return true if at least one row was updated
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Couldn't update the Username");
        }
        return "";
    }


    @Override
    public boolean deleteUser(int userId) {
        try (Connection conn = ConnectionUtil.getConnection()) {
            String sql = "DELETE FROM users WHERE user_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId); // Set the user ID to delete

            // Execute the delete operation
            int rowsDeleted = ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Coudn't delete the user");
        }
        return false;
    }

    public Users getUserByUsernameAndPassword(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?"; // Ensure password handling is secure
        try (Connection conn = ConnectionUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password); // Note: Password should ideally be hashed and not stored in plain text

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Users(
                        rs.getInt("user_id"), // Adjust if the column name is different
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("username"),
                        rs.getString("email")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // User not found
    }
}




