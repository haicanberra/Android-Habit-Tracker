package com.example.recurring_o_city;

//signup class to register a new user into the database

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * creates interface for someone to create an account, connect firebase to user
 */
public class Signup extends AppCompatActivity implements View.OnClickListener,OnCompleteListener<AuthResult> {

    private TextView registerUser;
    private EditText editTextUsername, editTextEmail, editTextPassword, editTextConfPass;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;


    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        registerUser = (Button) findViewById(R.id.btn_sign_up_to_main);
        registerUser.setOnClickListener(this);

        editTextUsername = (EditText) findViewById(R.id.username);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);
        editTextConfPass = (EditText) findViewById(R.id.confirm_pass);

    }

    /**
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_sign_up_to_main:
                btn_sign_up_to_main();
        }
    }

    /**
     * @return null
     */
    private void btn_sign_up_to_main() {
        String username = editTextUsername.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String conf_pass = editTextConfPass.getText().toString().trim();
        db = FirebaseFirestore.getInstance();
        CollectionReference users = db.collection("Users");

        /**
         * @param Boolean
         */
        if (username.isEmpty()) {
            editTextUsername.setError("Please enter a username");
            editTextUsername.requestFocus();
            return;
        }
        /**
         * @param Boolean
         */
        if (email.isEmpty()) {
            editTextEmail.setError("Please enter an email address");
            editTextEmail.requestFocus();
            return;
        }
        /**
         * @param String
         */
        if (!(Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
            editTextEmail.setError("Please enter a valid email address");
        }
        /**
         * @param Boolean
         */
        if (password.isEmpty()) {
            editTextPassword.setError("Please enter a password");
            editTextPassword.requestFocus();
            return;
        }
        /**
         * @param int
         */
        if (password.length() < 5) {
            editTextPassword.setError("Password needs to be at least 5 characters long");
            editTextPassword.requestFocus();
        }

        /**
         * @param Boolean
         */
        if (conf_pass.isEmpty()) {
            editTextConfPass.setError("Please enter a password");
            editTextConfPass.requestFocus();
            return;
        }
        /**
         * @param Boolean
         */
        if (!conf_pass.equals(password)) {
            editTextConfPass.setError("Passwords don't match");
            editTextConfPass.requestFocus();
            return;
        }

        /**
         * @param Boolean
         * @param String
         * @param int
         */
        if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty()
                && password.length() >= 5
                && conf_pass.equals(password)
                && (Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, this);
        }
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if(task.isSuccessful()){
            FirebaseUser user = mAuth.getCurrentUser();

            Map<String, Object> data = new HashMap<>();
            data.put("Username", editTextUsername.getText().toString());
            data.put("Email", editTextEmail.getText().toString());
            db.collection("Users").document(user.getUid().toString()).set(data);
            Log.d("Firebase User", user.getDisplayName() + user.getEmail());

            Toast.makeText(Signup.this,"You have signed up", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Signup.this, Login.class));
        } else{
            Toast.makeText(Signup.this,"Sorry, could not sign up", Toast.LENGTH_LONG).show();
        }
    }

}