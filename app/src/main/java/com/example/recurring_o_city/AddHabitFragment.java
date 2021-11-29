package com.example.recurring_o_city;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddHabitFragment extends DialogFragment
        implements RepeatDialog.RepeatDialogListener {

    private EditText habitTitle;
    private EditText habitReason;
    private EditText habitDate;
    private EditText habitRepeat;
    private ImageButton button;
    private ImageButton repeat;
    //private OnFragmentInteractionListener listener;
    private AddHabitFragmentListener listener;
    private Switch habitPrivacy;
    static Integer privacy;
    private boolean duplicate;
    private DatePickerDialog calDialog;
    private List<String> repeat_strg;

    public interface AddHabitFragmentListener{
        void onAddSavePressed(Habit newHabit);
    }

    public AddHabitFragment() {
        // Required empty public constructor
    }

    public static AddHabitFragment newInstance(){
        AddHabitFragment fragment = new AddHabitFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (AddHabitFragmentListener) context;
        } catch (RuntimeException e) {
            // The activity doesn't implement the interface, throw exception
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_habit, null);

        habitTitle = view.findViewById(R.id.habit_name);
        habitReason = view.findViewById(R.id.habit_reason);
        habitDate = view.findViewById(R.id.habit_date);
        habitRepeat = view.findViewById(R.id.habit_frequency);
        button = view.findViewById(R.id.button);
        repeat = view.findViewById(R.id.repeat_button);
        habitPrivacy = view.findViewById(R.id.privacy);

        // Setup DatePickerDialog to pops up when "EDIT" button is clicked.
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        button.setOnClickListener(view1 -> {
            calDialog = new DatePickerDialog(getContext(), (datePicker, mYear, mMonth, mDay)
                    -> habitDate.setText(mYear + "/" + (mMonth + 1) + "/" + mDay), year, month, day);
            calDialog.show();
        });

        habitRepeat.setText("Does not repeat");
        // Set up the repeat fragment to pop up when Edit calender is clicked
        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RepeatDialog repeatDialog = new RepeatDialog();
                repeatDialog.show(getChildFragmentManager(), "Repeat");
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

        // Set up the switch
        habitPrivacy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    // Set to private
                    privacy = 1;
                } else {
                    // Set to public
                    privacy = 0;
                }
            }
        });

        // Create builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Add Habit")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Save", (dialogInterface, i) -> {
                    Date newDate = null;
                    SimpleDateFormat d = new SimpleDateFormat("yyyy/MM/dd");

                    // Get and validate new input from user
                    String title = habitTitle.getText().toString();
                    String reason = habitReason.getText().toString();
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
                    if (!title.equals("") && !reason.equals("") && newDate != null) {
                        //When user clicks save button, add new medicine
                        listener.onAddSavePressed(new Habit(title, reason, newDate, repeat_strg, privacy));
                    }
                }).create();
    }

    @Override
    public void onRepeatSavePressed(List<String> repeat_list) {
        String repeat_display ="";
        if (repeat_list.size() <= 1) {
            repeat_display = "Does not repeat";
        } else {
            Utility util = new Utility();
            repeat_display = util.convertRepeat(repeat_list);
        }
        habitRepeat.setText(repeat_display);
        repeat_strg = repeat_list;
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