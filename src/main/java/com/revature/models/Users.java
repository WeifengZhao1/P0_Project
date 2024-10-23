package com.revature.models;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Users {
    private int user_id;  // User ID for managing user identity
    private String first_name;
    private String last_name;
    private String username;
    private String email;
    private String password;
    private Timestamp created_at;

    // Default constructor for Jackson
    public Users() {
    }

    // Constructor without user_id, for registration or login
    public Users(String first_name, String last_name, String username, String email, String password) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Constructor with user_id
    public Users(int user_id, String first_name, String last_name, String username, String email) {
        this.user_id = user_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.username = username;
        this.email = email;
    }


    // Getters and Setters
    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    // Add the getUserId method
    public int getUserId() {
        return user_id; // Return the user_id field
    }

    @Override
    public String toString() {
        return "Users{" +
                "user_id=" + user_id +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
