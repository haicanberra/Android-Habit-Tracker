package com.example.recurring_o_city;

//signup class to register a new user into the database

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.database.FirebaseDatabase;

public class signup extends AppCompatActivity implements View.OnClickListener {

    private TextView registerUser;
    private EditText editTextFirstName, editTextLastName, editTextEmail, editTextPassword;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        registerUser = (Button) findViewById(R.id.btn_sign_up_to_main);
        registerUser.setOnClickListener(this);

        editTextFirstName = (EditText) findViewById(R.id.first_name);
        editTextLastName = (EditText) findViewById(R.id.last_name);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_sign_up_to_main:
                btn_sign_up_to_main();
        }

    }

    private void btn_sign_up_to_main() {
        String email = editTextEmail.getText().toString().trim();
        String firstname = editTextFirstName.getText().toString().trim();
        String lastname = editTextLastName.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(firstname.isEmpty()){
            editTextFirstName.setError("Please enter a first name");
            editTextFirstName.requestFocus();
            return;
        }

        if(lastname.isEmpty()){
            editTextLastName.setError("Please enter a last name");
            editTextLastName.requestFocus();
            return;
        }

        if(email.isEmpty()){
            editTextEmail.setError("Please enter an email address");
            editTextEmail.requestFocus();
            return;
        }

        if(!(Patterns.EMAIL_ADDRESS.matcher(email).matches())){
            editTextEmail.setError("Please enter a valid email address");
        }

        if(password.isEmpty()){
            editTextPassword.setError("Please enter a password");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 5){
            editTextPassword.setError("Password needs to be at least 5 characters long");
            editTextPassword.requestFocus();
        }

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            User user = new User(firstname,lastname,email);

                            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(signup.this,"You have been registered", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(signup.this,MainActivity.class));
                                    }
                                    else{
                                        Toast.makeText(signup.this,"Sorry, could not register", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                        else{
                            Toast.makeText(signup.this,"Sorry, could not register", Toast.LENGTH_LONG).show();
                        }
                    }
                });




    }
}