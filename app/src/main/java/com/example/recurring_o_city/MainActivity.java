package com.example.recurring_o_city;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView signup, forgetpass;
    private EditText editTextUsername, editTextPassword;
    private Button logIn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signup = (TextView) findViewById(R.id.sign_up);
        forgetpass = (TextView) findViewById(R.id.forgotPassword);

        logIn = (Button) findViewById(R.id.btn_login);
        logIn.setOnClickListener(this);

        editTextUsername = (EditText) findViewById(R.id.username);
        editTextPassword = (EditText) findViewById(R.id.password);

        mAuth = FirebaseAuth.getInstance();

        // For clicking Signup
        SpannableString s = new SpannableString(signup.getText().toString());
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                startActivity(new Intent(MainActivity.this, signup.class));
            }
        };
        s.setSpan(clickableSpan, 23, 30, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        signup.setText(s);
        signup.setMovementMethod(LinkMovementMethod.getInstance());
        signup.setHighlightColor(Color.TRANSPARENT);




    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.forgotPassword:
                //startActivity(new Intent(this, forgot.class));
            case R.id.btn_login:
                userLogin();
        }

    }


    private void userLogin() {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if ((username.isEmpty())){
            editTextUsername.setError("Username is required");
            editTextUsername.requestFocus();
            return;
        }

        if ((password.isEmpty())){
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        if(!(Patterns.EMAIL_ADDRESS.matcher(username).matches())){
            editTextUsername.setError("Not a valid email");
            editTextUsername.requestFocus();
            return;
        }

        if (password.length() < 5){
            editTextPassword.setError("Minimum password length is 5");
            editTextPassword.requestFocus();
            return;
        }
        mAuth.signInWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(MainActivity.this,UserProfile.class));

                }
                else{
                    Toast.makeText(MainActivity.this,"Failed to Login",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}