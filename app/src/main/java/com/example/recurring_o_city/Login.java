package com.example.recurring_o_city;

import android.content.Intent;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends MainActivity {



    private void userLogin() {

        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);


        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        mAuth = FirebaseAuth.getInstance();

        if ((email.isEmpty())){
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if(!(Patterns.EMAIL_ADDRESS.matcher(email).matches())){
            editTextEmail.setError("Not a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if (password.length() < 5){
            editTextPassword.setError("Minimum password length is 5");
            editTextPassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(Login.this,UserProfile.class));

                }
                else{
                    Toast.makeText(Login.this,"Failed to Login",Toast.LENGTH_LONG).show();
                }
            }
        });
    }




}
