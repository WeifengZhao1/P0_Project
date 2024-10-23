package com.revature.DAOs;

import com.revature.models.Users;

import java.util.ArrayList;

public interface UsersDAOInterface {
    Users insertUser(Users user);

    Users getUserById(int id);

    ArrayList<Users> getAllUsers();

    String updateUserName(int id,String newUserName);

    boolean deleteUser(int userId);
}
