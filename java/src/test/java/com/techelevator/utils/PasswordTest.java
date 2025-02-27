package com.techelevator.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordTest {
    public static void main(String[] args) {
        // Step 1: Create a BCryptPasswordEncoder with 10 rounds (matching your stored hash)
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

        // Step 2: Replace this with the hash from your database
        String storedHash = "$2a$10$7QnW03JxI31yEmJ6L4y3sOEKj/9Bj2mkEDtM5n2MvwhL5Rv.n7dAu";

        // Step 3: Test the password "Password1"
        String testPassword = "Password1";
        boolean matches = encoder.matches(testPassword, storedHash);

        // Step 4: Print result
        System.out.println("Does password match? " + matches);
    }
}
