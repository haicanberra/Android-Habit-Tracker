package com.example.recurring_o_city;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;



import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * the base which everything executes off of
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AddHabitFragment.OnFragmentInteractionListener{

    ArrayList<Habit> habitList;
    ArrayList<HabitEvent> habitEventList;

    TabLayout tabLayout;
    ViewPager2 pager2;
    FragmentAdapter fragmentadapter;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tab_layout);
        pager2 = findViewById(R.id.view_pager2);
        drawerLayout = findViewById(R.id.drawer_layout);

        String dtStart = "12/05/2021";
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        Date date1 = null;
        try {
            date1 = format.parse(dtStart);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        habitList = new ArrayList<>();
        habitList.add(new Habit("asd", "nice", date1, 1));
        habitList.add(new Habit("2131t", "nice", date1, 1));
        habitList.add(new Habit("Casdat", "nice", date1, 1));

        Habit habit = new Habit("nice","2",date1,1);
        habitEventList = new ArrayList<>();
        habitEventList.add(new HabitEvent(habit, "comment"));


        FragmentManager fm = getSupportFragmentManager();
        fragmentadapter = new FragmentAdapter(fm, getLifecycle(), habitList, habitEventList);
        pager2.setAdapter(fragmentadapter);

        tabLayout.addTab(tabLayout.newTab().setText("Today"));
        tabLayout.addTab(tabLayout.newTab().setText("All"));
        tabLayout.addTab(tabLayout.newTab().setText("Event"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            /**
             * @param tab
             */
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager2.setCurrentItem(tab.getPosition());
            }
            /**
             * @param tab
             */
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            /**
             * @param tab
             */
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            /**
             * @param position
             */
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });


        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


    }

    /**
     * @return null
     */
    @Override
    public void onBackPressed(){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    /**
     * @param item
     * @return Boolean
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_home:
                break;
            case R.id.nav_you_follow:
                break;
            case R.id.nav_follow_you:
                break;
            case R.id.nav_follow_request:
                break;
            case R.id.nav_send_request:
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, Login.class));
                finish();
                break;
        }
        return true;
    }

    /**
     * @param newHabit
     */
    @Override
    public void onSavePressed(Habit newHabit) {
        habitList.add(newHabit);
        FragmentManager fm = getSupportFragmentManager();
        fragmentadapter = new FragmentAdapter(fm, getLifecycle(), habitList, habitEventList);
        pager2.setAdapter(fragmentadapter);
    }
}