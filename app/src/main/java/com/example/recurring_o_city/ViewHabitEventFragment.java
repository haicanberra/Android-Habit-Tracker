package com.example.recurring_o_city;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Implements the fragment for viewing the habit event details.
 */
public class ViewHabitEventFragment extends Fragment
        implements EditHabitEventFragment.EditHabitEventFragmentListener{
    /*
    Can be called using:

    ViewHabitEventFragment habitEventFrag = new ViewHabitEventFragment();
    getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.[Fragment Container], habitEventFrag.newInstance([Habit Event Object]))
                .addToBackStack(null).commit();
     */

    private String event_title;
    private String event_reason;
    private String event_date;
    private String event_repeat;
    private String event_privacy;
    private String event_comment;
    private String event_location;
    private TextView titleText, reasonText, dateText, repeatText, privacyText, commentText, datedoneText, locationText;
    private FirebaseAuth mAuth;

    // Get the attributes from the Habit object.
    public ViewHabitEventFragment newInstance(HabitEvent newHabitEvent) {
        Bundle args = new Bundle();

        args.putString("event_title", newHabitEvent.getEventHabit().getTitle());
        args.putString("event_reason", newHabitEvent.getEventHabit().getReason());
        args.putString("event_privacy", newHabitEvent.getEventHabit().getPrivacy().toString());

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        Date date = newHabitEvent.getEventHabit().getDate();
        String date_string = format.format(date);
        args.putString("event_date", date_string);

        if (newHabitEvent.getEventHabit().getRepeat() == null){
            event_repeat = "No repeat";
        } else {
            Utility util = new Utility();
            event_reason = util.convertRepeat(newHabitEvent.getEventHabit().getRepeat());
        }
        args.putString("habit_repeat", event_repeat);

        ViewHabitEventFragment fragment = new ViewHabitEventFragment();
        fragment.setArguments(args);
        return fragment;
    }

    // Show View Habit Event Fragment.
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_habit_event_fragment, null);

        titleText      = view.findViewById(R.id.habitevent_title);
        reasonText     = view.findViewById(R.id.habitevent_reason_content);
        dateText       = view.findViewById(R.id.habitevent_date_content);
        repeatText     = view.findViewById(R.id.habitevent_repeat_content);
        privacyText    = view.findViewById(R.id.habitevent_privacy_content);
        commentText    = view.findViewById(R.id.habitevent_comment_content);
        datedoneText   = view.findViewById(R.id.habitevent_datedone_content);
        locationText   = view.findViewById(R.id.habitevent_location_content);
        ImageView eventImage    = view.findViewById(R.id.habitevent_image);

        ImageButton editButton = view.findViewById(R.id.habitevent_edit_button);
        ImageButton backButton = view.findViewById(R.id.habitevent_back_button);
        mAuth = FirebaseAuth.getInstance();


        event_title = getArguments().getString("event_title");
        event_reason = getArguments().getString("event_reason");
        event_date = getArguments().getString("event_date");
        event_repeat = getArguments().getString("event_repeat");
        event_privacy = getArguments().getString("event_privacy");

        if (event_privacy.equals("0")) {
            event_privacy = "Public";
        } else if (event_privacy.equals("1")){
            event_privacy = "Private";
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
        privacyText.setText(event_privacy);
        commentText.setText(event_comment);
        locationText.setText(event_location);
        //eventImage.setImageResource(R.drawable.[image name]);

        // Calls the EditHabitEventFragment.
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call the EditHabitEventFragment
                new EditHabitEventFragment()
                        .newInstance(event_title, mAuth.getCurrentUser().getUid()).show(getChildFragmentManager(), "EDIT_HABITEVENT");
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

    @Override
    public void onEditEventSavePressed(String comment) {
        commentText.setText(comment);
        // Set the location

        // Set the image
    }
}