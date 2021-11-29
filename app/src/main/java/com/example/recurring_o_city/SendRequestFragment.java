package com.example.recurring_o_city;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class SendRequestFragment extends Fragment {

    private EditText userEmail;
    private Button sendButton;
    private ImageButton backButton;
    private FirebaseFirestore db;
    private CollectionReference collectionReference;
    private FirebaseAuth mAuth;
    private String currentEmail;
    private Boolean valid = false;



    public SendRequestFragment() {
        // Required empty public constructor
    }


    public static SendRequestFragment newInstance() {
        SendRequestFragment fragment = new SendRequestFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_send_request, container, false);

        userEmail = view.findViewById(R.id.send_username);
        sendButton = view.findViewById(R.id.send_button);
        backButton = view.findViewById(R.id.send_back_button);
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Users");
        mAuth = FirebaseAuth.getInstance();


        currentEmail = mAuth.getCurrentUser().getEmail();
        // When click send button
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Send the request (current user email to the entered user)
                String email = userEmail.getText().toString();

                // Validate the user email
                collectionReference
                        .whereEqualTo("Email", email)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    int list = task.getResult().getDocuments().size();
                                    if (list == 0 || email.equals(currentEmail)) {
                                        Toast.makeText(getContext(), "Invalid email", Toast.LENGTH_LONG).show();
                                    } else {
                                        sendRequest(email);
                                    }

                                }
                            }
                        });
            }
        });


        // If click on  back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }

    public void sendRequest(String email) {
        // Update to pending list of that user
        collectionReference
                .whereEqualTo("Email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().getDocuments().size() > 0) {
                                // Get the current pending list
                                ArrayList<String> pending = (ArrayList<String>) task.getResult().getDocuments().get(0).get("Pending");
                                ArrayList<String> follower = (ArrayList<String>) task.getResult().getDocuments().get(0).get("Follower");

                                // Add current user email to that pending list if not yet added
                                if (!pending.contains(currentEmail) && !follower.contains(currentEmail)) {
                                    Toast.makeText(getContext(), "Send request successfully", Toast.LENGTH_LONG).show();
                                    pending.add(currentEmail);
                                } else {
                                    Toast.makeText(getContext(), "Already sent request", Toast.LENGTH_LONG).show();
                                }
                                // Update the new pending list
                                task.getResult().getDocuments().get(0).getReference().update("Pending", pending);
                            }

                        }
                    }
                });
    }
}