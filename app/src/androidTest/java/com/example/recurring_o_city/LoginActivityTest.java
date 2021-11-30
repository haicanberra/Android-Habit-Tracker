package com.example.recurring_o_city;

import static junit.framework.TestCase.assertEquals;

import android.app.Activity;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class LoginActivityTest {
    private Solo solo;

    private FirebaseFirestore db;
    private CollectionReference collectionReference;
    private FirebaseAuth mAuth;
    private String email;

    @Rule
    public ActivityTestRule<Login> rule = new ActivityTestRule<>(Login.class,true,true);

    @Before
    public void setUp(){
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());

    }

    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();

    }

    @Test
    public void checkLogin(){
        solo.assertCurrentActivity("Wrong Activity",Login.class);
        solo.enterText((EditText) solo.getView(R.id.email),"cena123@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.password),"123456");
        solo.clickOnButton("Login");

        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Users");
        mAuth = FirebaseAuth.getInstance();


    }
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}