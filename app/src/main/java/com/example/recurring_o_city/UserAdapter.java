package com.example.recurring_o_city;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
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
    private ImageButton accept, deny;
    private FirebaseFirestore db;
    private CollectionReference collectionReference;
    private FirebaseAuth mAuth;

    public UserAdapter(Context context, ArrayList<String> users) {
        super(context, 0);
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Users");
        mAuth = FirebaseAuth.getInstance();

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
        //username.setText(userEmail);
        user_email.setText(userEmail);

        return view;
    }

    // Update follower list of current user
    public void updateFollowerList(String email) {
        collectionReference
                .whereEqualTo("User Id", mAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Add email to follower current list
                            ArrayList<String> follower = (ArrayList<String>) task.getResult().getDocuments().get(0).get("Follower");
                            String email = task.getResult().getDocuments().get(0).get("Email").toString();
                            String name = task.getResult().getDocuments().get(0).get("Username").toString();

                            follower.add(email);
                            // Update the list to database
                            task.getResult().getDocuments().get(0).getReference().update("Follower", follower);

                            // Remove the email from current pending list
                            ArrayList<String> pending = (ArrayList<String>) task.getResult().getDocuments().get(0).get("Pending");
                            pending.remove(email);
                            // Update the list to database
                            task.getResult().getDocuments().get(0).getReference().update("Pending", pending);
                        }
                    }
                });
    }

    // Update the pending list of current user
    public void updatePendingList(String email) {
        collectionReference
                .whereEqualTo("User Id", mAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String email = task.getResult().getDocuments().get(0).get("Email").toString();
                            String name = task.getResult().getDocuments().get(0).get("Username").toString();
                            User newUser = new User(name, email);
                            // Remove the email from current pending list
                            ArrayList<String> pending = (ArrayList<String>) task.getResult().getDocuments().get(0).get("Pending");
                            pending.remove(newUser);
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
                            // Add to following list of that accepted user
                            ArrayList<User> following = (ArrayList<User>) task.getResult().getDocuments().get(0).get("Following");
                            String username = mAuth.getCurrentUser().getDisplayName();
                            String email = mAuth.getCurrentUser().getEmail();
                            following.add(new User(username, email));
                            // Update the list to database
                            task.getResult().getDocuments().get(0).getReference().update("Following", following);
                        }
                    }
                });
    }
}
