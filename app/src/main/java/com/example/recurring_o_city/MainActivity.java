package com.example.recurring_o_city;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
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


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AddHabitFragment.OnFragmentInteractionListener {

    public ArrayList<Habit> habitList;
    public ArrayList<Habit> todayList;
    public ArrayList<HabitEvent> habitEventList;

    private TabLayout tabLayout;
    private ViewPager2 pager2;
    private FragmentAdapter fragmentadapter;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private FirebaseFirestore db;
    CollectionReference collectionReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tab_layout);
        pager2 = findViewById(R.id.view_pager2);
        drawerLayout = findViewById(R.id.drawer_layout);

        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Habits");

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String date = df.format(Calendar.getInstance().getTime());


        habitList = new ArrayList<>();
        habitEventList = new ArrayList<>();
        todayList = new ArrayList<>();


        tabLayout.addTab(tabLayout.newTab().setText("Today"));
        tabLayout.addTab(tabLayout.newTab().setText("All"));
        tabLayout.addTab(tabLayout.newTab().setText("Event"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
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

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                // Clear the old list
                habitList.clear();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    Log.d("New Habit", String.valueOf(doc.getData().get("Date")));
                    String title = (String) doc.getId();
                    String reason = (String) doc.getData().get("Reason");
                    Date date = doc.getTimestamp("Date").toDate();
                    int priv = Integer.valueOf(doc.getData().get("Privacy").toString());
                    habitList.add(new Habit(title, reason, date, priv));

                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Date today = Calendar.getInstance().getTime();
                    String date_s = format.format(today);
                    try {
                        today = format.parse(date_s);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (today.compareTo(date) == 0) {
                        todayList.add(new Habit(title, reason, date, priv));
                    }
                }
                FragmentManager fm = getSupportFragmentManager();
                fragmentadapter = new FragmentAdapter(fm, getLifecycle(), todayList, habitList, habitEventList);
                pager2.setAdapter(fragmentadapter);
            }

        });


    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

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

    @Override
    public void onSavePressed(Habit newHabit) {
//        habitList.add(newHabit);
//        FragmentManager fm = getSupportFragmentManager();
//        fragmentadapter = new FragmentAdapter(fm, getLifecycle(), habitList, habitEventList);
//        pager2.setAdapter(fragmentadapter);


        HashMap<String, Object> data = new HashMap<>();
        data.put("Reason", newHabit.getReason());
        data.put("Date", newHabit.getDate());
        data.put("Privacy", newHabit.getPrivacy());

        collectionReference
                .document(newHabit.getTitle())
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
