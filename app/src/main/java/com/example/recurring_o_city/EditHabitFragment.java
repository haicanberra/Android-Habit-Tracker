package com.example.recurring_o_city;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditHabitFragment extends DialogFragment {

    private EditText habitTitle;
    private EditText habitReason;
    private EditText habitDate;
    private Button button;
    private Switch habitPrivacy;
    static int priv = 0;
    private DatePickerDialog calDialog;

    private FirebaseFirestore db;
    CollectionReference collectionReference;
    DocumentReference editHabit;

    public EditHabitFragment () {
        // Required empty public constructor
    }

    public static EditHabitFragment newInstance(Habit oldHabit){
        Bundle args = new Bundle();

        // This string is going to be our key for retrieving Firebase document.
        args.putString("habit_title", oldHabit.getTitle());

        EditHabitFragment fragment = new EditHabitFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        // The layout of the edit habit fragment will be same as add habit fragment.
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_habit, null);

        habitTitle = view.findViewById(R.id.habit_name);
        habitReason = view.findViewById(R.id.habit_reason);
        habitDate = view.findViewById(R.id.habit_date);
        button = view.findViewById(R.id.button);
        habitPrivacy = view.findViewById(R.id.privacy);

        // Setup DatePickerDialog to pops up when "EDIT" button is clicked.
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        button.setOnClickListener(view1 -> {
            calDialog = new DatePickerDialog(getContext(), (datePicker, mYear, mMonth, mDay) -> habitDate.setText(mYear + "-" + (mMonth + 1) + "-" + mDay), year, month, day);
            calDialog.show();
        });

        // Set the collection reference from the Firebase.
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Habits");

        // Retrieve the old data to edit, using the String "key" from the newInstance().
        editHabit = collectionReference.document(getArguments().getString("habit_title"));

        // Set the old data on the editText and TextView.
        editHabit.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                // documentSnapshot contains data from the document in Firebase.
                // In this case, documentSnapshot contains the habit data.
                if (documentSnapshot.exists()) {
                    habitTitle.setText(getArguments().getString("habit_title"));
                    habitReason.setText(documentSnapshot.getString("Reason"));

                    Date oldDate = documentSnapshot.getDate("Date");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    habitDate.setText(dateFormat.format(oldDate));
                }
            }
        });

        // Create builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Edit Habit")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Save", (dialogInterface, i) -> {
                    Date newDate = null;
                    SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");

                    // Get and validate new input from user
                    String title = habitTitle.getText().toString();
                    String reason = habitReason.getText().toString();

                    // Check if the inputs are valid.
                    if (title.equals("")) {
                        habitTitle.setError("Title cannot be empty");
                        habitTitle.requestFocus();
                        return;
                    }
                    if (reason.equals("")) {
                        habitReason.setError("Reason cannot be empty");
                        habitReason.requestFocus();
                        return;
                    }

                    try {
                        newDate = d.parse(String.valueOf(habitDate.getText()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    habitPrivacy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                            if (isChecked) {
                                priv = 0;
                            } else {
                                priv = 1;
                            }
                        }
                    });

                    // Check if input is valid and proceed
                    if (!title.equals("") && newDate != null) {
                        editHabit.update("Reason", reason);
                        editHabit.update("Date", newDate);
                        editHabit.update("Privacy", priv);
                    }
                }).create();
    }

}
