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

    public ArrayList<Habit> habitList;
    public ItemAdapter habitAdapter;
    private FloatingActionButton fab;
    FirebaseFirestore db;

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

        habitList = (ArrayList<Habit>) getArguments().getSerializable(
                "HABIT");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_today, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.today_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        habitAdapter = new ItemAdapter(habitList);
        recyclerView.setAdapter(habitAdapter);


        db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection("Habits");

        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(v -> new AddHabitFragment().show(getActivity().getSupportFragmentManager(), "ADD_HABIT"));


//        db.collection("Users")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d("Get habit", "Get data successfully");
//                            }
//                        } else {
//                            Log.d("Get habit", "Error getting documents: ", task.getException());
//                        }
//                    }
//                });



//                FragmentManager fm = getSupportFragmentManager();
//                fragmentadapter = new FragmentAdapter(fm, getLifecycle(), habitList, habitEventList);
//                pager2.setAdapter(fragmentadapter);

        return view;
    }

}
