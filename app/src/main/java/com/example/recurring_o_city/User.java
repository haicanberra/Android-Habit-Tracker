package com.example.recurring_o_city;

import java.util.ArrayList;
import java.util.List;

// Information about the user

/**
 * data class for the user
 */
public class User {

    private String username, email;
    private ArrayList<String> pending_list;

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

    /**
     * set username
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * set email
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

}
