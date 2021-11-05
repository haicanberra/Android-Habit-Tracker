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

public class AllHabitFragment extends Fragment{

    public ArrayList<Habit> allHabitList;
    public ItemAdapter habitAdapter;

    public AllHabitFragment() {
        // Required empty public constructor
    }

    public static AllHabitFragment newInstance(ArrayList<Habit> list) {
        AllHabitFragment fragment = new AllHabitFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("ALL_HABIT", list);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        allHabitList = (ArrayList<Habit>) getArguments().getSerializable(
                "ALL_HABIT");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_habit, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.all_habit_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        habitAdapter = new ItemAdapter(allHabitList);
        recyclerView.setAdapter(habitAdapter);


        ItemAdapter myAdapter = new ItemAdapter(allHabitList);
        recyclerView.setAdapter(myAdapter);



        myAdapter.setOnItemClickListener(new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Habit selectedHabit = (Habit) allHabitList.get(position);
                ViewHabitFragment habitFrag = new ViewHabitFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.all_habit_frame, habitFrag.newInstance(selectedHabit))
                        .addToBackStack(null).commit();
            }
        });


        return view;
    }

}
