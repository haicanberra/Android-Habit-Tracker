package com.example.recurring_o_city;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class TodayFragment extends Fragment{

    private ArrayList<Habit> habitList;
    private ArrayList<Habit> todayList = new ArrayList<>();
    private ItemAdapter habitAdapter;

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

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat name_format = new SimpleDateFormat("EEE");
        Date today = Calendar.getInstance().getTime();
        String date_s = format.format(today);
        String date_name = null;
        try {
            today = format.parse(date_s);
            date_name = name_format.format(today);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Get today habit list
        for (int i = 0; i<habitList.size(); i++) {
            Date date = habitList.get(i).getDate();
            Date nextDate = habitList.get(i).getNext_date();
            List<String> repeat_strg = habitList.get(i).getRepeat();
            List<String> repeat_box = new ArrayList<>();
            if (repeat_strg != null && repeat_strg.size() > 3) {
                repeat_box = repeat_strg.subList(2, repeat_strg.size()-1);
            }
            
            if (today.compareTo(date) == 0 || today.equals(nextDate) || repeat_box.contains(date_name)) {
                todayList.add(habitList.get(i));
                //Update next date here
                habitList.get(i).setNext_date(habitList.get(i).getNext_date());
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

        // Click on item to view
        habitAdapter.setOnItemClickListener(new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Habit selectedHabit = (Habit) todayList.get(position);
                ViewHabitFragment habitFrag = new ViewHabitFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.drawer_layout, habitFrag.newInstance(selectedHabit))
                        .addToBackStack(null).commit();
            }
        });
        return view;
    }

}
