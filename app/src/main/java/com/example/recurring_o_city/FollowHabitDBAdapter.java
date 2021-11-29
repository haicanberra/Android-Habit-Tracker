package com.example.recurring_o_city;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class FollowHabitDBAdaptor<mDataBase> extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseFirestore db;

    public void update_db(Habit habit, ArrayList<String> followers, ArrayList<String> pending, FollowRequest target) {
        db = FirebaseFirestore.getInstance();
        DocumentReference habits = db.collection("Users").document(target.getPerson()).collection("Habit").document(habit.getTitle());

        Map<String, Object> data = new HashMap<>();
        data.put("Followers", followers);
        data.put("Pending", pending);
        habits.update(data);

        Toast.makeText(FollowHabitDBAdaptor.this, "Changed Permissions", Toast.LENGTH_LONG).show();
    };

    public DocumentReference get_pending(Habit habit, FollowRequest target) {
        db = FirebaseFirestore.getInstance();
        DocumentReference habits = db.collection("Users").document(target.getPerson()).collection("Habit").document(habit.getTitle());

        habits.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Toast.makeText(FollowHabitDBAdaptor.this, "Pending Followers", Toast.LENGTH_LONG).show();
                        Map<String, Object> dataFollowers = document.getData();
                        if (dataFollowers.get("Pending") != null) {
                            ArrayList<String> pending = (ArrayList<String>) dataFollowers.get("Pending");
                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }

        });
        return habits;
    };
}
