package com.techelevator.dao;

import com.techelevator.model.RegisterUserDto;
import com.techelevator.model.User;

import java.util.List;

public interface UserDao {
    //TODO: Retrieve all users
    List<User> getUsers();

    // TODO: Error handling if user ID doesn't exist
    User getUserById(int id);

    // TODO: Handle username as case-insensitive
    User getUserByUsername(String username);

    // TODO: Validate password hashing
    User createUser(RegisterUserDto user);
}
