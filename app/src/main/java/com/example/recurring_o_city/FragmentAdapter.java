package com.example.recurring_o_city;

import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class FragmentAdapter extends FragmentStateAdapter {

    private ArrayList<Habit> habitList;
    private ArrayList<HabitEvent> habitEventList;

    public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle,ArrayList<Habit> habitList) {
        super(fragmentManager, lifecycle);
        this.habitList = habitList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch(position){
            case 1:
                return AllHabitFragment.newInstance(this.habitList);
            case 2:
                return HabitEventFragment.newInstance(this.habitEventList);
        }
        return TodayFragment.newInstance(this.habitList);
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
