package com.example.recurring_o_city;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddHabitFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddHabitFragment extends DialogFragment {

    private EditText habitTitle;
    private EditText habitReason;
    private EditText habitDate;
    private OnFragmentInteractionListener listener;
    private Switch habitPrivacy;
    static int priv = 0;
    Calendar cal;
    DatePickerDialog dpd;


    public interface OnFragmentInteractionListener{
        void onSavePressed(Habit newHabit);
    }

    public AddHabitFragment() {
        // Required empty public constructor
    }


    public static AddHabitFragment newInstance() {
        AddHabitFragment fragment = new AddHabitFragment();
        Bundle args = new Bundle();

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

        habitTitle = view.findViewById(R.id.habit_title);
        habitReason = view.findViewById(R.id.habit_reason);
        habitDate = view.findViewById(R.id.habit_date);
        habitPrivacy = view.findViewById(R.id.privacy);


        habitDate.setOnClickListener(view1 -> {
            cal = Calendar.getInstance();
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int month = cal.get(Calendar.MONTH);
            int year = cal.get(Calendar.YEAR);

            // Get the date formatted
            dpd = new DatePickerDialog(getActivity(), (datePicker, n_year, n_month, n_day) -> {
                String selected_date = n_year + "-" + (n_month + 1) + "-" + n_day;
                habitDate.setText(selected_date);
            }, year, month, day);
            dpd.show();
        });

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
                                priv = 0;
                            } else {
                                priv = 1;
                            }
                        }
                    });

                    // Check if input is valid and proceed
                    if (!title.equals("") && !title.equals("") && newDate != null) {
                        //When user clicks save button, add new medicine
                        listener.onSavePressed(new Habit(title, reason, newDate, priv));
                    }
                }).create();
    }

}