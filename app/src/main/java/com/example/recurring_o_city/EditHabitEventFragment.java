package com.example.recurring_o_city;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class EditHabitEventFragment extends DialogFragment {

    private TextView eventTitle;
    private EditText eventComment;
    private EditText eventLocation;
    private ImageButton mapButton;
    private ImageButton photoButton;
    private ImageView imageView;
    private EditHabitEventFragment.EditHabitEventFragmentListener listener;
    private FirebaseFirestore db;
    CollectionReference collectionReference;
    DocumentReference editHabitEvent;

    public EditHabitEventFragment(){

    }

    public static EditHabitEventFragment newInstance(String HabitEventTitle, String UserId) {
        Bundle args = new Bundle();

        // String acts as key for retrieving Firebase document
        args.putSerializable("event_title", HabitEventTitle);
        args.putString("User_Id", UserId);

        EditHabitEventFragment fragment = new EditHabitEventFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public interface EditHabitEventFragmentListener{
        void onEditEventSavePressed(String newComment);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (EditHabitEventFragment.EditHabitEventFragmentListener) getParentFragment();
        } catch (RuntimeException e) {
            // The activity doesn't implement the interface, throw exception
            throw new RuntimeException(context.toString()
                    + " must implement EditHabitEventFragmentListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        imageView.setImageBitmap(bitmap);
    }

    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.edit_habit_event_fragment, null);

        eventTitle = view.findViewById(R.id.editevent_title);
        eventComment = view.findViewById(R.id.editevent_comment);
        eventLocation = view.findViewById(R.id.editevent_location);
        mapButton = view.findViewById(R.id.editevent_map);
        photoButton = view.findViewById(R.id.editevent_photo);
        imageView = view.findViewById(R.id.editevent_image);

        // Set the habit event title
        eventTitle.setText(getArguments().getString("event_title"));

        // Set the collection reference from the Firebase.
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Habit Events");

        // Get reference to the selected document
        collectionReference
                .whereEqualTo("User Id", getArguments().getString("User_Id"))
                .whereEqualTo("Title", getArguments().getString("event_title"))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Use the task.getResult() to get the querySnapshot...
                            QuerySnapshot querySnapshot = task.getResult();

                            // Get the querySnapshot to get document reference.
                            DocumentSnapshot docSnapshot = querySnapshot.getDocuments().get(0);

                            // Get reference to the selected document
                            editHabitEvent = docSnapshot.getReference();
                        }
                    }
                });

        // When click on the image button, go to take photo
        // Code to take photo implements here

        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });

        // When click on map image, open map activity
        // Code to implements map here


        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new MapsFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.drawer_layout, fragment)
                        .addToBackStack(null).commit();
            }
        });
        // Package the unique creation date of habit event along with intent.
        // Make sure the habit and user matches.
        //collectionReference
        //        .whereEqualTo("User Id", getArguments().getString("User_Id"))
        //        .whereEqualTo("Title", getArguments().getString("event_title"))

        // Create builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Edit Habit Event")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Save", (dialogInterface, i) -> {
                    // Get input from user, can be null as optional
                    String comment = eventComment.getText().toString();

                    // Get the image from camera

                    // Get the location

                    // Update to database
                    editHabitEvent.update("Comment", comment);


                    // When user clicks save button, add these details to habit event
                    listener.onEditEventSavePressed(comment);
                }).create();

    }

}
