package com.techelevator.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a User in the system.
 * A user has a unique ID, username, password, assigned roles, and a handle.
 */
public class User {

   private int id;
   private String username;
   @JsonIgnore
   private String password;
   private Set<Authority> authorities;
   private String handle; // Added handle

   public User() {
      this.authorities = new HashSet<>();
   }

   public User(int id, String username, String password, String role) {
      this.id = id;
      this.username = username;
      this.password = password;
      this.authorities = new HashSet<>();

      // Convert role string into a Set of Authority objects
      if (role != null) {
         this.authorities.add(new Authority(role));
      }
   }

   // Getters and Setters
   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public Set<Authority> getAuthorities() {
      return authorities;
   }

   public void setAuthorities(Set<Authority> authorities) {
      this.authorities = authorities;
   }

   public String getHandle() { // Added getter
      return handle;
   }

   public void setHandle(String handle) { // Added setter
      this.handle = handle;
   }
}
