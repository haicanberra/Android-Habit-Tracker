package com.example.recurring_o_city;

import java.util.ArrayList;
import java.util.List;

// Information about the user
public class User {

    private String username, email;
    private ArrayList<String> pending_list;

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

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
