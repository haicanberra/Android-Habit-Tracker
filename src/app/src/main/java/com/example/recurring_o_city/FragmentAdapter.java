package com.example.recurring_o_city;

import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

/**
 * Main class for holding {@link ArrayList} of various type on the main menu.
 */
public class FragmentAdapter extends FragmentStateAdapter {

    private ArrayList<Habit> habitList;
    private ArrayList<HabitEvent> habitEventList;

    /**
     * Constructor for the {@link FragmentAdapter} class.
     * @param fragmentManager
     *  Responsible for the {@link Fragment} activities.
     * @param lifecycle
     *  Holds state of the current {@link Fragment}'s {@link Lifecycle} information.
     * @param habitList
     *  {@link ArrayList} of type {@link Habit} and {@link HabitEvent} to add to the {@link android.widget.ArrayAdapter}.
    */
    public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle,ArrayList<Habit> habitList,
                           ArrayList<HabitEvent> habitEventList) {
        super(fragmentManager, lifecycle);
        this.habitList = habitList;
        this.habitEventList = habitEventList;
    }

    /**
     * Depending on which tab is selected, either return list of {@link Habit} or list of {@link HabitEvent}.
     * @param position
     *  The tab position the user is currently located within {@link MainActivity}.
     * @return habitList
     *  {@link ArrayList} holding {@link Habit} objects.
     * @return habitEventList
     *  {@link ArrayList} holding {@link HabitEvent} objects.
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
     * Returns the total numbers of tabs present in the app.
     * @return int
     *  Where {@link Integer} return value corresponds to the total size of the {@link FragmentAdapter} list.
     */
    @Override
    public int getItemCount() {
        return 3;
    }
}
