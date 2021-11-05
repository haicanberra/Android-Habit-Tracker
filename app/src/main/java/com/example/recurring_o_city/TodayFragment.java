package com.example.recurring_o_city;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TodayFragment extends Fragment{

    public ArrayList<Habit> todayList;
    public ItemAdapter habitAdapter;
    private FloatingActionButton fab;

    public TodayFragment() {
        // Required empty public constructor
    }

    public static TodayFragment newInstance(ArrayList<Habit> list) {
        TodayFragment fragment = new TodayFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("HABIT", list);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        todayList = (ArrayList<Habit>) getArguments().getSerializable(
                "HABIT");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_today, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.today_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        habitAdapter = new ItemAdapter(todayList, "today");
        recyclerView.setAdapter(habitAdapter);


        ItemAdapter myAdapter = new ItemAdapter(todayList, "today");
        recyclerView.setAdapter(myAdapter);

        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(v -> new AddHabitFragment().show(getActivity().getSupportFragmentManager(), "ADD_HABIT"));


        myAdapter.setOnItemClickListener(new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Habit selectedHabit = (Habit) todayList.get(position);
                ViewHabitFragment habitFrag = new ViewHabitFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.today_frame, habitFrag.newInstance(selectedHabit))
                        .addToBackStack(null).commit();
            }
        });


        return view;
    }

}
