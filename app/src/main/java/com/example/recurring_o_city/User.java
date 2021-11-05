package com.example.recurring_o_city;

/**
 * carries user info
 */
// Information about the user
public class User {

    public String username, email;

    /**
     * @param username
     * @param email
     */
    public User(String username, String email){
        this.username = username;
        this.email = email;
    }

    /**
     * @return String
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return String
     */
    public String getEmail() {
        return email;
    }
}
