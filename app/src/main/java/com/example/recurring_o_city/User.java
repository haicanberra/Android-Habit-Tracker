package com.example.recurring_o_city;

// Information about the user
public class User {

    public String username, email;

    public User(String username, String email){
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
