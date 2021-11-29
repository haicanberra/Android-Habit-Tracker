package com.example.recurring_o_city;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Implements the fragment for viewing the habit details.
 */
public class ViewHabitFragment extends Fragment
        implements EditHabitFragment.EditHabitFragmentListener{
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
    private TextView titleText, reasonText, dateText, repeatText, privacyText;
    private FirebaseAuth mAuth;
    private String hide;

    // Get the attributes from the Habit object.
    public ViewHabitFragment newInstance(Habit newHabit, String hide) {
        Bundle args = new Bundle();

        args.putString("habit_title", newHabit.getTitle());
        args.putString("habit_reason", newHabit.getReason());
        args.putString("habit_privacy", newHabit.getPrivacy().toString());
        args.putString("hide", hide);

        if (newHabit.getRepeat() == null){
            habit_repeat = "No repeat";
        } else {
            Utility util = new Utility();
            habit_repeat = util.convertRepeat(newHabit.getRepeat());
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

        titleText      = view.findViewById(R.id.habit_title);
        reasonText     = view.findViewById(R.id.habit_reason_content);
        dateText       = view.findViewById(R.id.habit_date_content);
        repeatText     = view.findViewById(R.id.habit_repeat_content);
        privacyText    = view.findViewById(R.id.habit_privacy_content);

        ImageButton editButton = view.findViewById(R.id.habit_edit_button);
        ImageButton backButton = view.findViewById(R.id.habit_back_button);

        habit_title = getArguments().getString("habit_title");
        habit_reason = getArguments().getString("habit_reason");
        habit_date = getArguments().getString("habit_date");
        habit_repeat = getArguments().getString("habit_repeat");
        habit_privacy = getArguments().getString("habit_privacy");
        mAuth = FirebaseAuth.getInstance();
        hide = getArguments().getString("hide");

        if (habit_privacy.equals("0")) {
            habit_privacy = "Public";
        } else if (habit_privacy.equals("1")){
            habit_privacy = "Private";
        }

        titleText.setText(habit_title);
        reasonText.setText(habit_reason);
        dateText.setText(habit_date);
        repeatText.setText(habit_repeat);
        privacyText.setText(habit_privacy);

        if (hide.equals("hide")) {
            editButton.setVisibility(View.GONE);
        }else{
            editButton.setVisibility(View.VISIBLE);
        }

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // We only need the habit title, which is the firebase document ID.
                //new EditHabitFragment().newInstance(habit_title).show(getActivity().getSupportFragmentManager(), "EDIT_HABIT");
                new EditHabitFragment().newInstance(habit_title, mAuth.getCurrentUser().getUid()).show(getChildFragmentManager(), "EDIT_HABIT");
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

    // When save pressed
    @Override
    public void onEditSavePressed(Habit newHabit) {
        titleText.setText(newHabit.getTitle());
        reasonText.setText(newHabit.getReason());

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        Date date = newHabit.getDate();
        String date_string = format.format(date);
        dateText.setText(date_string);
        Utility util = new Utility();
        repeatText.setText(util.convertRepeat(newHabit.getRepeat()));

        if (newHabit.getPrivacy().toString().equals("0")){
            privacyText.setText("Public");
        } else {
            privacyText.setText("Private");
        }

    }
}