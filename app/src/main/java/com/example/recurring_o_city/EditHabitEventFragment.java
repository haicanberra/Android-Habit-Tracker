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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditHabitEventFragment extends DialogFragment {

    private TextView habitEventTitle;
    private EditText habitEventComment;
    private String title;

    private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener{
        void onSavePressed(HabitEvent habitEvent);
    }

    public EditHabitEventFragment() {
        // Required empty public constructor
    }
    public EditHabitEventFragment(String title) {
        // Required empty public constructor
        this.title = title;
    }

    public static EditHabitEventFragment newInstance(){
        EditHabitEventFragment fragment = new EditHabitEventFragment();
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

        habitEventTitle = view.findViewById(R.id.editevent_title);
        habitEventComment = view.findViewById(R.id.editevent_editText);


        // Create builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Edit Habit Event")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Save", (dialogInterface, i) -> {

                    habitEventTitle.setText(this.title);

                    // Get and validate new input from user
                    String title = habitEventTitle.getText().toString();
                    String reason = habitEventComment.getText().toString();
                    if (title.equals("")) {
                        habitEventTitle.setError("Title cannot be empty");
                        habitEventTitle.requestFocus();
                        return;
                    }
                    if (reason.equals("")) {
                        habitEventComment.setError("Comment cannot be empty");
                        habitEventComment.requestFocus();
                        return;
                    }


                    // Check if input is valid and proceed
                    if (!title.equals("") && !title.equals("") ) {
                        Date today = Calendar.getInstance().getTime();
                        listener.onSavePressed(new HabitEvent(new Habit("1","1",today,1),habitEventComment.getText().toString()));
                    }
                }).create();
    }

}