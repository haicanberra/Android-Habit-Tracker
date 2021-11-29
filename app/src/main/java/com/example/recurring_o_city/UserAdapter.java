package com.example.recurring_o_city;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UserAdapter extends ArrayAdapter<String> {
    private ArrayList<String> users;
    private Context context;
    private TextView username, user_email;
    private ImageView accept, deny;
    private FirebaseFirestore db;
    private CollectionReference collectionReference;
    private FirebaseAuth mAuth;
    private String currentEmail;
    private String currentFragment;

    public UserAdapter(Context context, ArrayList<String> users, String currentFragment) {
        super(context, 0, users);
        this.context = context;
        this.users = users;
        this.currentFragment = currentFragment;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Users");
        mAuth = FirebaseAuth.getInstance();

        currentEmail = mAuth.getCurrentUser().getEmail();
        String userEmail = users.get(position);

        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.user_adapter, parent, false);
        }

        // Get the view ids
        username = view.findViewById(R.id.user_name);
        user_email = view.findViewById(R.id.user_email);
        accept = view.findViewById(R.id.accept);
        deny = view.findViewById(R.id.deny);

        switch (currentFragment) {
            case ("frf"):
                accept.setVisibility(View.VISIBLE);
                deny.setVisibility(View.VISIBLE);
                break;
            default:
                accept.setVisibility(View.GONE);
                deny.setVisibility(View.GONE);
                break;
        }

        // When accept request
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add current user to follower list of that user and remove from pending
                updateFollowerList(userEmail);
                updatePendingList(userEmail);
                // Add that user to following list of current user
                updateFollowingList(userEmail);
            }
        });

        // When deny
        deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Remove from current user pending list
                updatePendingList(userEmail);
            }
        });


        // Set the fields
        // Get the username
        collectionReference
                .whereEqualTo("Email", userEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String userName = (String) task.getResult().getDocuments().get(0).get("Username");
                            username.setText(userName);
                        }
                    }
                });
        user_email.setText(userEmail);

        return view;
    }

    // Update follower list of current user
    public void updateFollowerList(String email) {
        collectionReference
                .whereEqualTo("Email", currentEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<String> follower = new ArrayList<>();
                            // Add email to follower current list
                            if (task.getResult().getDocuments().size() > 0) {
                                follower = (ArrayList<String>) task.getResult().getDocuments().get(0).get("Follower");

                            }
                            follower.add(email);

                            // Update the list to database
                            task.getResult().getDocuments().get(0).getReference().update("Follower", follower);

                        }
                    }
                });
    }

    // Update the pending list of current user
    public void updatePendingList(String email) {
        collectionReference
                .whereEqualTo("Email", currentEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Remove the email from current pending list
                            ArrayList<String> pending = (ArrayList<String>) task.getResult().getDocuments().get(0).get("Pending");
                            pending.remove(email);
                            // Update the list to database
                            task.getResult().getDocuments().get(0).getReference().update("Pending", pending);
                        }
                    }
                });
    }

    // Update the following list of that accepted user
    public void updateFollowingList(String email) {
        collectionReference
                .whereEqualTo("Email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<String> following = new ArrayList<>();
                            if (task.getResult().getDocuments().size() > 0) {
                                following = (ArrayList<String>) task.getResult().getDocuments().get(0).get("Following");
                            }
                            // Add to following list of that accepted user
                            following.add(currentEmail);
                            // Update the list to database
                            task.getResult().getDocuments().get(0).getReference().update("Following", following);
                        }
                    }
                });
    }
}

