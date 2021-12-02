package com.example.recurring_o_city;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * displays a popup of whatever habit needs to be done
 */
public class HabitEventFragment extends Fragment {

    private ArrayList<HabitEvent> habitEventList;
    private ItemAdapter habitAdapter;

    /**
     * Empty constructor required for instantiation of this class.
     */
    public HabitEventFragment() {
        // Required empty public constructor
    }

    /**
     * Creates new instance of {@link HabitEventFragment} class.
     * @param list
     * {@link ArrayList} of type {@link HabitEvent} to add to the {@link HabitEventFragment}.
     * @return Fragment
     * New fragment instantiated with {@link Bundle} containing {@link HabitEvent} objects.
     */
    public static HabitEventFragment newInstance(ArrayList<HabitEvent> list) {
        HabitEventFragment fragment = new HabitEventFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("HABIT_EVENT", list);
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * @param savedInstanceState
     * Using the saved instance, gets the list of {@link HabitEvent}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        habitEventList = (ArrayList<HabitEvent>) getArguments().getSerializable(
                "HABIT_EVENT");

    }

    /**
     * display UI of Habit Event to user
     * Using {@link RecyclerView}, sets up the list of {@link HabitEvent}.
     * Sets up {@link com.example.recurring_o_city.ItemAdapter.OnItemClickListener} for each {@link HabitEvent} object.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return view
     * Contains the list of {@link HabitEvent} objects.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_habit_event, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.habit_event_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        habitAdapter = new ItemAdapter(habitEventList, "event");
        recyclerView.setAdapter(habitAdapter);

        // When click to view habit event
        habitAdapter.setOnItemClickListener(new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                HabitEvent selectedHabitEvent = (HabitEvent) habitEventList.get(position);
                ViewHabitEventFragment habitEventFrag = new ViewHabitEventFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.drawer_layout, habitEventFrag.newInstance(selectedHabitEvent))
                        .addToBackStack(null).commit();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}