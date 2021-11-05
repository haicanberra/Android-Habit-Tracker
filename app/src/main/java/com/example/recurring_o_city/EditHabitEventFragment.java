package com.example.recurring_o_city;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class EditHabitEventFragment extends DialogFragment {

    private String oldComment;

    static EditHabitEventFragment newInstance(HabitEvent oldHabitEvent) {
        Bundle args = new Bundle();

        Habit oldHabit = oldHabitEvent.getEventHabit();

        args.putString("editable_comment", oldHabitEvent.getEventComment());

        EditHabitEventFragment fragment = new EditHabitEventFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.edit_habit_event_fragment, null);

        EditText editText = view.findViewById(R.id.editevent_editText);

        Button saveButton = view.findViewById(R.id.save_button);
        Button cancelButton = view.findViewById(R.id.cancel_button);

        oldComment = getArguments().getString("editable_comment");
        editText.setText(oldComment);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ...
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }
}
