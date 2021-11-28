package com.example.recurring_o_city;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EditHabitFragment extends DialogFragment
        implements RepeatDialog.RepeatDialogListener {

    private EditText habitTitle;
    private EditText habitReason;
    private EditText habitDate;
    private EditText habitRepeat;
    private ImageButton button;
    private ImageButton repeat;
    private Switch habitPrivacy;
    private int privacy;
    private EditHabitFragmentListener listener;
    private DatePickerDialog calDialog;
    private List<String> repeats;
    private boolean duplicate;

    private FirebaseFirestore db;
    CollectionReference collectionReference;
    DocumentReference editHabit;

    public EditHabitFragment () {
        // Required empty public constructor
    }

    public static EditHabitFragment newInstance(String oldHabitTitle, String UserId){
        Bundle args = new Bundle();

        // This string acts as key for retrieving Firebase document.
        args.putString("habit_title", oldHabitTitle);
        args.putString("User_Id", UserId);

        EditHabitFragment fragment = new EditHabitFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public interface EditHabitFragmentListener{
        void onEditSavePressed(Habit newHabit);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (EditHabitFragment.EditHabitFragmentListener) getParentFragment();
        } catch (RuntimeException e) {
            // The activity doesn't implement the interface, throw exception
            throw new RuntimeException(context.toString()
                    + " must implement EditHabitFragmentListener");
        }
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
        repeat = view.findViewById(R.id.repeat_button);
        habitPrivacy = view.findViewById(R.id.privacy);
        habitRepeat = view.findViewById(R.id.habit_frequency);

        // Setup DatePickerDialog to pops up when "EDIT" button is clicked.
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        button.setOnClickListener(view1 -> {
            calDialog = new DatePickerDialog(getContext(), (datePicker, mYear, mMonth, mDay)
                    -> habitDate.setText(mYear + "-" + (mMonth + 1) + "-" + mDay), year, month, day);
            calDialog.show();
        });

        // Set up the repeat fragment to pop up when Edit calender is clicked
        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RepeatDialog repeatDialog = new RepeatDialog();
                repeatDialog.show(getChildFragmentManager(), "Repeat");
            }
        });


        // Set the collection reference from the Firebase.
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Habits");

        // Set the document editHabit, and pull the old data from that document.
        // Use .whereEqualTo() to get the query...
        collectionReference
                .whereEqualTo("User Id", getArguments().getString("User_Id"))
                .whereEqualTo("Title", getArguments().getString("habit_title"))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Use the task.getResult() to get the querySnapshot...
                            QuerySnapshot querySnapshot = task.getResult();

                            // Get the querySnapshot to get document reference.
                            DocumentSnapshot docSnapshot = querySnapshot.getDocuments().get(0);

                            // Update values using the documentSnapshot.
                            editHabit = docSnapshot.getReference();
                            habitTitle.setText(docSnapshot.getString("Title"));
                            habitReason.setText(docSnapshot.getString("Reason"));

                            repeats = (List<String>)docSnapshot.get("Repeat");
                            if (repeats != null) {
                                Utility util = new Utility();
                                habitRepeat.setText(util.convertRepeat(repeats));
                            }

                            Date oldDate = docSnapshot.getDate("Date");
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            habitDate.setText(dateFormat.format(oldDate));
                        }
                    }
                });

        habitPrivacy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    privacy = 1;
                } else {
                    privacy = 0;
                }
            }
        });

        habitTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Empty method for TextWatcher.
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Empty method for TextWatcher.
            }
            @Override
            public void afterTextChanged(Editable editable) {
                duplicateTitle(editable.toString());
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
                    if (duplicate) {
                        habitTitle.setError("Title must be unique");
                        habitTitle.requestFocus();
                        return;
                    }

                    try {
                        newDate = d.parse(String.valueOf(habitDate.getText()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (habitPrivacy.isChecked()) {
                        privacy = 1;
                    } else {
                        privacy = 0;
                    }

                    // Check if input is valid and proceed
                    if (!title.equals("") && newDate != null) {
                        editHabit.update("Title", title);
                        editHabit.update("Reason", reason);
                        editHabit.update("Date", newDate);
                        editHabit.update("Repeat", repeats);
                        editHabit.update("Privacy", privacy);
                    }

                    if (!title.equals("")  && !reason.equals("") && newDate != null) {
                        //When user clicks save button, add new medicine
                        listener.onEditSavePressed(new Habit(title, reason, newDate, repeats, privacy));
                    }
                }).create();
    }

    @Override
    public void onRepeatSavePressed(List<String> repeat_list) {
        // Get the List<String> of repeats, and set it to habitRepeat text field.
        Utility util = new Utility();
        String repeat_display = util.convertRepeat(repeat_list);
        habitRepeat.setText(repeat_display);
        repeats = repeat_list;
    }

    private void duplicateTitle(String title) {
        String userId = getActivity().getIntent().getStringExtra("User Id");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection("Habits");
        Query query = collectionReference
                .whereEqualTo("Title", title)
                .whereEqualTo("User Id", userId);

        // This query modifies global boolean variable "duplicate".
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    duplicate = task.getResult().size() != 0;
                }
            }
        });
    }
}
