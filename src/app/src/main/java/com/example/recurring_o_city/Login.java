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

/**
 * displays login UI to user
 */
public class Login extends AppCompatActivity implements View.OnClickListener,OnCompleteListener<AuthResult> {

    private TextView signup;
    private EditText editTextEmail, editTextPassword;
    private Button logIn;
    private FirebaseAuth mAuth;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signup = (TextView) findViewById(R.id.sign_up);

        logIn = (Button) findViewById(R.id.btn_login);
        logIn.setOnClickListener(this);

        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);

        mAuth = FirebaseAuth.getInstance();

        // For clicking Signup
        SpannableString s = new SpannableString(signup.getText().toString());
        ClickableSpan clickableSpan = new ClickableSpan() {
            /**
             * @param view
             */
            @Override
            public void onClick(@NonNull View view) {
                startActivity(new Intent(Login.this, com.example.recurring_o_city.Signup.class));
            }
        };
        s.setSpan(clickableSpan, 23, 30, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        signup.setText(s);
        signup.setMovementMethod(LinkMovementMethod.getInstance());
        signup.setHighlightColor(Color.TRANSPARENT);
    }

    /**
     * get account of current user
     */
    @Override
    protected void onStart()
    {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null)
        {
            Toast.makeText(this, "Logged in as " + user.getEmail(),Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("User Id", mAuth.getCurrentUser().getUid());
            startActivity(intent);
        }
    }

    /**
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login:
                userLogin();
        }
    }

    /**
     * @returns null
     */
    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if ((email.isEmpty())){
            editTextEmail.setError("Username is required");
            editTextEmail.requestFocus();
            return;
        }

        if ((password.isEmpty())){
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();

            return;
        }

        if (password.length() < 5){
            editTextPassword.setError("Minimum password length is 5");
            editTextPassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, this);
    }

    /**
     * @param task
     */
    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()) {
            Intent intent = new Intent(Login.this, MainActivity.class);
            intent.putExtra("User Id", mAuth.getCurrentUser().getUid());
            startActivity(intent);
            //
        } else {
            Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_LONG).show();
        }
    }
}

