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

/**
 * this fragment contains all the following request fragments
 */
public class FollowingUserHabitFragment extends Fragment {

    private ArrayList<Habit> allHabit;
    private ItemAdapter habitAdapter;
    private ImageView backButton;

    /**
     * Empty constructor required for instantiation of this class.
     */
    public FollowingUserHabitFragment() {
        // Required empty public constructor
    }

    /**
     * instantiate the class, takes in a list of all habits
     * Creates new instance of {@link FollowingRequestFragment} class.
     * @param list
     * {@link ArrayList} of type {@link Habit} to add to the {@link FollowingUserHabitFragment}.
     * @return fragment
     * New fragment instantiated with {@link Bundle} containing {@link Habit} objects.
     */
    public static FollowingUserHabitFragment newInstance(ArrayList<Habit> list) {
        FollowingUserHabitFragment fragment = new FollowingUserHabitFragment();
        Bundle args = new Bundle();
        args.putSerializable("USER_HABIT", list);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * get app state and list of habits
     * Using the saved instance, gets the list of user habits {@link Habit} .
     * @param savedInstanceState
     *  {@link Bundle} object created from {@link #newInstance(ArrayList)} function.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allHabit = (ArrayList<Habit>) getArguments().getSerializable(
                "USER_HABIT");
    }

    /**
     * Using {@link RecyclerView}, sets up the list of user habits {@link Habit}.
     * Sets up {@link com.example.recurring_o_city.ItemAdapter.OnItemClickListener} for each {@link Habit} object.
     * @param inflater
     *  Layout .xml file instantiated into a {@link View}.
     * @param container
     *  Container for the {@link View} created by {@link LayoutInflater}.
     * @param savedInstanceState
     *  {@link Bundle} instantiated from {@link #newInstance(ArrayList)}.
     * @return view
     *  Contains the list of {@link Habit} objects.
     */
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