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

import android.view.LayoutInflater;
import android.view.View;

import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

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
    private ImageButton button;
    private ImageButton repeat;
    private OnFragmentInteractionListener listener;
    private Switch habitPrivacy;
    static Integer privacy = 0;
    private DatePickerDialog calDialog;
    private List<String> repeat_strg;

    private RepeatDialog repeatDialog;

    @Override
    public void onRepeatSavePressed(List<String> repeat_list) {
        repeat_strg = repeat_list;
    }

    public interface OnFragmentInteractionListener{
        void onSavePressed(Habit newHabit);
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
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
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
                    -> habitDate.setText(mYear + "-" + (mMonth + 1) + "-" + mDay), year, month, day);
            calDialog.show();
        });

        // Set up the repeat fragment to pop up when Edit calender is clicked
        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RepeatDialog repeatDialog = new RepeatDialog();
//                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
//                ft.add(R.id.repeat_frame, repeatDialog).commit();
                //repeatDialog.show(ft, "Repeat");
                repeatDialog.show(getChildFragmentManager(), "Repeat");
            }
        });
//        repeat.setOnClickListener(v -> new RepeatDialog()
//                .show(getActivity().getFragmentManager(), "Repeat"));



        // Create builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Add Habit")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Save", (dialogInterface, i) -> {
                    Date newDate = null;
                    SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");

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

                    try {
                        newDate = d.parse(String.valueOf(habitDate.getText()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }



                    habitPrivacy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                            if (isChecked) {
                                privacy = 0;
                            } else {
                                privacy = 1;
                            }
                        }
                    });

                    // Check if input is valid and proceed
                    if (!title.equals("") && !title.equals("") && newDate != null) {
                        //When user clicks save button, add new medicine
                        listener.onSavePressed(new Habit(title, reason, newDate, repeat_strg, privacy));
                    }
                }).create();
    }

}