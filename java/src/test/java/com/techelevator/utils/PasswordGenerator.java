package com.techelevator.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10); // Ensure bcrypt strength matches

        // 🔥 List of users to generate new hashes for
        String[] users = {
                "djzedd", "djmustard", "djkhaled", "tiësto", "calvinharris",
                "hostlisa", "hostmark", "hosttina", "userbob", "usersarah",
                "davidguetta", "marshmello", "zedd", "steveaoki",
                "hostjessica", "hostkevin", "partygoerashley", "partygoermike"
        };

        String rawPassword = "Password1"; // 🔥 Default password for all users

        System.out.println("\n🔑 New Hashed Passwords:");
        for (String user : users) {
            String hashedPassword = encoder.encode(rawPassword);
            System.out.println(user + " -> " + hashedPassword);
        }
    }
}
