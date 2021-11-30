package com.example.recurring_o_city;

import android.app.Activity;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.robotium.solo.Solo;



import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class SignupActivityTest {

    private Solo solo;
    private FirebaseFirestore db;
    private CollectionReference collectionReference;
    private FirebaseAuth mAuth;
    private String email;

    @Rule
    public ActivityTestRule<Signup> rule = new ActivityTestRule<>(Signup.class,true,true);

    @Before
    public void setUp(){
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());

    }

    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();

    }
    @Test
    public void checkSignUp(){
        solo.assertCurrentActivity("Wrong Activity",Signup.class);
        solo.clickOnButton("SIGN UP");
        solo.enterText((EditText) solo.getView(R.id.username),"Rock");
        solo.enterText((EditText) solo.getView(R.id.email),"nice123@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.password),"123456");
        solo.enterText((EditText) solo.getView(R.id.confirm_pass),"123456");
        solo.clickOnButton("SIGN UP");


    }
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
