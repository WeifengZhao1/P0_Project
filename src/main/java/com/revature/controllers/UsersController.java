package com.revature.controllers;

import com.revature.DAOs.UserDAO;
import com.revature.SessionManager;
import com.revature.models.Users;

import com.revature.utils.ConnectionUtil;
import io.javalin.http.Handler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UsersController {

    UserDAO usersDAO = new UserDAO();

    // Handler for user registration
    public Handler registerUserHandler = ctx -> {
        try {
            // Extract user details from the request body
            Users newUser = ctx.bodyAsClass(Users.class);

            // Check if username or email is already taken
            if (usersDAO.isUsernameTaken(newUser.getUsername()) || usersDAO.isEmailTaken(newUser.getEmail())) {
                ctx.status(400).result("Username or email is already taken");
                return;
            }

            // Hash the user's password before saving it (optional, depending on security implementation)
//            String hashedPassword = PasswordUtils.hashPassword(newUser.getPassword());
            newUser.setPassword(newUser.getPassword());

            // Insert the new user into the database
            Users insertedUser = usersDAO.insertUser(newUser);

            if (insertedUser != null) {
                ctx.status(201).json(insertedUser); // Return the created user with ID
            } else {
                ctx.status(500).result("User registration failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("An error occurred while registering the user: " + e.getMessage());
        }
    };

    // Login Handler
    public Handler loginHandler = ctx -> {
        Users loginUser = ctx.bodyAsClass(Users.class);

        // Validate login credentials
        Users user = usersDAO.getUserByUsernameAndPassword(loginUser.getUsername(), loginUser.getPassword());

        if (user != null) {
            // Generate a session ID
            String sessionId = UUID.randomUUID().toString();
            // Create a session with the user ID
            SessionManager.createSession(sessionId, user.getUserId());

            ctx.status(200);
            ctx.json(sessionId); // Return session ID to the client
        } else {
            ctx.status(401); // Unauthorized
            ctx.result("Invalid credentials");
        }
    };


    public Handler getUserByIdHandler = ctx -> {
        try {
            int userId = Integer.parseInt(ctx.pathParam("user_id")); // Get the user_id from the path

            // Retrieve the user from the DAO
            Users user = usersDAO.getUserById(userId);

            // Return the user object with all specified fields
            if (userId < 1){
                ctx.status(400).result("User Id must be greater than 0");
            }
            else if (user != null) {
                ctx.status(200).json(user); // Return user details with status 200
            } else {
                ctx.status(404).result("User with ID " + userId + " not found"); // Handle user not found
            }
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid user ID format"); // Handle invalid format
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("An error occurred while retrieving the user: " + e.getMessage()); // Handle server error
        }
    };






    // Method to retrieve all users from the database
    public Handler getAllUsersHandler = ctx -> {
        try {
            // Retrieve all users from the DAO
            ArrayList<Users> allUsers = usersDAO.getAllUsers();

            if (allUsers.isEmpty()) {
                ctx.status(404).result("No users found");
            } else {
                ctx.status(200).json(allUsers); // Return all users in JSON format
            }
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("An error occurred while fetching users: " + e.getMessage());
        }
    };

    // Handler to update the username of a user
    public Handler updateUserNameHandler = ctx -> {
        try {
            int userId = Integer.parseInt(ctx.pathParam("user_id")); // Get user ID from path parameter
            String newUserName = ctx.bodyAsClass(String.class); // Get the new username from request body

            // Update the username
            String updatedUsername = usersDAO.updateUserName(userId, newUserName);

            if (!updatedUsername.isEmpty()) {
                ctx.status(200).result("Username updated to: " + updatedUsername);
            } else {
                ctx.status(400).result("Failed to update username");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("An error occurred while updating the username: " + e.getMessage());
        }
    };

    // Handler to delete a user
    public Handler deleteUserHandler = ctx -> {
        try {
            int userId = Integer.parseInt(ctx.pathParam("user_id")); // Get user ID from path parameter

            // Delete the user
            boolean deleted = usersDAO.deleteUser(userId);

            if (deleted) {
                ctx.status(200).result("User deleted successfully");
            } else {
                ctx.status(400).result("Failed to delete user");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("An error occurred while deleting the user: " + e.getMessage());
        }
    };
}
