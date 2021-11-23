package com.example.recurring_o_city;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Implements the fragment for viewing the habit details.
 */
public class ViewHabitFragment extends Fragment{
    /*
    Can be called using:

    ViewHabitFragment habitFrag = new ViewHabitFragment();
    getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.[Fragment Container], habitFrag.newInstance([Habit Object]))
                .addToBackStack(null).commit();
     */

    private String habit_title;
    private String habit_reason;
    private String habit_date;
    private String habit_repeat;
    private String habit_privacy;

    // Get the attributes from the Habit object.
    public ViewHabitFragment newInstance(Habit newHabit) {
        Bundle args = new Bundle();

        args.putString("habit_title", newHabit.getTitle());
        args.putString("habit_reason", newHabit.getReason());
        args.putString("habit_privacy", String.valueOf(newHabit.getPrivacy()));

        if (newHabit.getRepeat() == null){
            habit_repeat = "No repeat";
        } else {
            habit_repeat = String.join(",", newHabit.getRepeat());
        }
        args.putString("habit_repeat", habit_repeat);
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        Date date = newHabit.getDate();
        String date_string = format.format(date);
        args.putString("habit_date", date_string);

        ViewHabitFragment fragment = new ViewHabitFragment();
        fragment.setArguments(args);
        return fragment;
    }

    // Show View Habit Event Fragment.
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_habit_fragment, null);

        TextView titleText      = view.findViewById(R.id.habit_title);
        TextView reasonText     = view.findViewById(R.id.habit_reason_content);
        TextView dateText       = view.findViewById(R.id.habit_date_content);
        TextView repeatText     = view.findViewById(R.id.habit_repeat_content);
        TextView privacyText    = view.findViewById(R.id.habit_privacy_content);

        ImageButton editButton = view.findViewById(R.id.habit_edit_button);
        ImageButton backButton = view.findViewById(R.id.habit_back_button);

        habit_title = getArguments().getString("habit_title");
        habit_reason = getArguments().getString("habit_reason");
        habit_date = getArguments().getString("habit_date");
        habit_repeat = getArguments().getString("habit_repeat");

        if (habit_repeat == "") {
            habit_repeat = "No repeat";
        }
        if (habit_privacy == "0") {
            habit_privacy = "Public";
        }
        else if (habit_privacy == "1"){
            habit_privacy = "Private";
        }

        titleText.setText(habit_title);
        reasonText.setText(habit_reason);
        dateText.setText(habit_date);
        repeatText.setText(habit_repeat);
        privacyText.setText(habit_privacy);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // We only need the habit title, which is the firebase document ID.
                Habit currentHabit = new Habit(habit_title, null, null, null, 0);
                new EditHabitFragment().newInstance(currentHabit).show(getActivity().getSupportFragmentManager(), "EDIT_HABIT");
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        // Pops out a stack, returning to previous fragment.
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }
}