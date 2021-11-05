package com.example.recurring_o_city;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * Implements the fragment for viewing the habit event details.
 */
public class ViewHabitEventFragment extends Fragment{
    /*
    Can be called using:

    ViewHabitEventFragment habitEventFrag = new ViewHabitEventFragment();
    getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.[Fragment Container], habitEventFrag.newInstance([Habit Object]))
                .addToBackStack(null).commit();
     */

    private String event_title;
    private String event_reason;
    private String event_date;
    private String event_repeat;
    private String event_comment;
    private String event_location;

    // Get the attributes from the Habit object.
    static ViewHabitEventFragment newInstance(Habit newHabit) {
        Bundle args = new Bundle();

        args.putString("event_title", newHabit.getTitle());
        args.putString("event_reason", newHabit.getReason());
        args.putString("event_date", newHabit.getDate().toString());

        ViewHabitEventFragment fragment = new ViewHabitEventFragment();
        fragment.setArguments(args);
        return fragment;
    }

    // Show View Habit Event Fragment.
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_habit_event_fragment, null);

        TextView titleText      = view.findViewById(R.id.habitevent_title);
        TextView reasonText     = view.findViewById(R.id.habitevent_reason_content);
        TextView dateText       = view.findViewById(R.id.habitevent_date_content);
        TextView repeatText     = view.findViewById(R.id.habitevent_repeat_content);
        TextView commentText    = view.findViewById(R.id.habitevent_comment_content);
        TextView locationText   = view.findViewById(R.id.habitevent_location_content);
        ImageView eventImage    = view.findViewById(R.id.habitevent_image);

        ImageButton editButton = view.findViewById(R.id.habitevent_edit_button);
        ImageButton backButton = view.findViewById(R.id.habitevent_back_button);

        event_title = getArguments().getString("event_title");
        event_reason = getArguments().getString("event_reason");
        event_date = getArguments().getString("event_date");

        if (event_repeat == null) {
            event_repeat = "No";
        }
        if (event_comment == null) {
            event_comment = "";
        }
        if (event_location == null) {
            event_location = "";
        }

        titleText.setText(event_title);
        reasonText.setText(event_reason);
        dateText.setText(event_date);
        repeatText.setText(event_repeat);
        commentText.setText(event_comment);
        locationText.setText(event_location);
        //eventImage.setImageResource(R.drawable.[image name]);

        // Calls the EditHabitEventFragment.
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call the EditHabitEventFragment
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