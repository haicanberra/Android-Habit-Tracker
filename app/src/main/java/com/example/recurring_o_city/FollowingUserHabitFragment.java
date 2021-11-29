package com.example.recurring_o_city;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;


public class FollowingUserHabitFragment extends Fragment {

    private ArrayList<Habit> allHabit;
    private ItemAdapter habitAdapter;
    private ImageView backButton;

    public FollowingUserHabitFragment() {
        // Required empty public constructor
    }


    public static FollowingUserHabitFragment newInstance(ArrayList<Habit> list) {
        FollowingUserHabitFragment fragment = new FollowingUserHabitFragment();
        Bundle args = new Bundle();
        args.putSerializable("USER_HABIT", list);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allHabit = (ArrayList<Habit>) getArguments().getSerializable(
                "USER_HABIT");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_following_user_habit, container, false);
        backButton = view.findViewById(R.id.send_back_button);

        RecyclerView recyclerView = view.findViewById(R.id.following_habit_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        habitAdapter = new ItemAdapter(allHabit, "fyhf");
        recyclerView.setAdapter(habitAdapter);

        // Click on item to view
        habitAdapter.setOnItemClickListener(new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Habit selectedHabit = (Habit) allHabit.get(position);
                ViewHabitFragment habitFrag = new ViewHabitFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.follow_user_habit_frag, habitFrag.newInstance(selectedHabit, "hide"))
                        .addToBackStack(null).commit();
            }
        });

        // When click back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }
}