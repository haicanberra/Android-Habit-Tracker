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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class TodayFragment extends Fragment implements
        AddHabitFragment.AddHabitFragmentListener{

    private ArrayList<Habit> habitList;
    private ArrayList<Habit> todayList = new ArrayList<>();
    private ItemAdapter habitAdapter;
    private FloatingActionButton fab;
    private FirebaseFirestore db;
    CollectionReference collectionReference;
    private FirebaseAuth mAuth;

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

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date today = Calendar.getInstance().getTime();
        String date_s = format.format(today);
        try {
            today = format.parse(date_s);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (int i = 0; i<habitList.size(); i++) {
            Date date = habitList.get(i).getDate();
            if (today.compareTo(date) == 0) {
                todayList.add(habitList.get(i));
            }
        }

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

        // When click add button, add habit fragment pops up
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(v -> new AddHabitFragment().show(getChildFragmentManager(), "ADD_HABIT"));

        // Click on item to view
        habitAdapter.setOnItemClickListener(new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Habit selectedHabit = (Habit) todayList.get(position);
                ViewHabitFragment habitFrag = new ViewHabitFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        //.replace(R.id.today_frame, habitFrag.newInstance(selectedHabit))
                        .replace(R.id.drawer_layout, habitFrag.newInstance(selectedHabit))
                        .addToBackStack(null).commit();
            }
        });

        return view;
    }

    // Press save in Add habit fragment, add habit to database
    @Override
    public void onAddSavePressed(Habit newHabit) {
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Habits");
        mAuth = FirebaseAuth.getInstance();

        HashMap<String, Object> data = new HashMap<>();
        data.put("User Id", mAuth.getCurrentUser().getUid());
        data.put("Title", newHabit.getTitle());
        data.put("Reason", newHabit.getReason());
        data.put("Date", newHabit.getDate());
        data.put("Repeat", newHabit.getRepeat());
        data.put("Privacy", newHabit.getPrivacy());
        data.put("Done", newHabit.getDone());

        collectionReference
                .document()
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void avoid) {
                        Log.d("New Habit", "Data has been added successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("New Habit", "Data could not be added" + e.toString());
                    }
                });
    }
}
