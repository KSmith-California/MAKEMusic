package com.techelevator.dao;

import com.techelevator.model.RegisterUserDto;
import com.techelevator.model.User;

import java.util.List;

public interface UserDao {
    //Retrieve all users
    List<User> getUsers();

    //Error handling if user ID doesn't exist
    User getUserById(int id);

    // Handle username as case-insensitive
    User getUserByUsername(String username);

    //Validate password hashing
    User createUser(RegisterUserDto user);

    //Retrieve DJs only
    List<User> getDJs();

    //Retrieve Hosts only
    List<User> getHosts();
}
