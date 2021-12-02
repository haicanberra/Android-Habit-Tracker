package com.example.recurring_o_city;

import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

/**
 * This class generate ArrayLists that hold the habits
 */
public class FragmentAdapter extends FragmentStateAdapter {

    private ArrayList<Habit> habitList;
    private ArrayList<HabitEvent> habitEventList;

    /**
     * @param fragmentManager
     * @param lifecycle
     *  {@link ArrayList} of type {@link Habit} and {@link HabitEvent} to add to the {@link android.widget.ArrayAdapter}.
    */
    public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle,ArrayList<Habit> habitList,
                           ArrayList<HabitEvent> habitEventList) {
        super(fragmentManager, lifecycle);
        this.habitList = habitList;
        this.habitEventList = habitEventList;
    }

    /**
     * depending on int selection return either HabitList or or habitEventList
     * @param position
     * @return habitList
     * @return habitEventList
     */
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

    /**
     * @return int
     */
    @Override
    public int getItemCount() {
        return 3;
    }
}
