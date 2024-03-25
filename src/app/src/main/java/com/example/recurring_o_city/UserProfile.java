package com.example.recurring_o_city;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/**
 * display user info to user and allow editing
 */
public class UserProfile extends AppCompatActivity {

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
    }
}