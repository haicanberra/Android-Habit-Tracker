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
import android.graphics.Bitmap;
import android.graphics.Picture;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AddHabitFragment.AddHabitFragmentListener{

    public ArrayList<Habit> habitList;
    public ArrayList<Habit> todayList;
    public ArrayList<HabitEvent> habitEventList;
    public int selectedTab = 0;

    private TabLayout tabLayout;
    private ViewPager2 pager2;
    private FragmentAdapter fragmentadapter;
    private FloatingActionButton fab;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private FirebaseFirestore db;
    private String UserId;
    CollectionReference collectionReference, collectionReference2;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the intent that started the activity and extract the user id
        Intent intent = getIntent();
        UserId = intent.getStringExtra("User Id");

        tabLayout = findViewById(R.id.tab_layout);
        pager2 = findViewById(R.id.view_pager2);
        drawerLayout = findViewById(R.id.drawer_layout);

        // When click add button, add habit fragment pops up
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> new AddHabitFragment().show(getSupportFragmentManager(), "ADD_HABIT"));

        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Habits");
        collectionReference2 = db.collection("Habit Events");


        habitList = new ArrayList<>();
        habitEventList = new ArrayList<>();
        todayList = new ArrayList<>();


        tabLayout.addTab(tabLayout.newTab().setText("Today"));
        tabLayout.addTab(tabLayout.newTab().setText("All"));
        tabLayout.addTab(tabLayout.newTab().setText("Event"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectedTab = tab.getPosition();
                pager2.setCurrentItem(selectedTab);
                hideButton(selectedTab);
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

        FragmentManager fm = getSupportFragmentManager();
        fragmentadapter = new FragmentAdapter(fm, getLifecycle(), habitList, habitEventList);
        pager2.setAdapter(fragmentadapter);

        // Get all the habits from database realtime update
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                // Clear the old list
                habitList.clear();

                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    // Retrieving the habit list
                    Log.d("New Habit", String.valueOf(doc.getData().get("Date")));

                    if (doc.getData().get("User Id").equals(UserId)) {
                        String title = (String) doc.getData().get("Title");
                        String reason = (String) doc.getData().get("Reason");
                        Date date = doc.getTimestamp("Date").toDate();
                        List<String> repeat = (List<String>) doc.getData().get("Repeat");
                        Integer privacy = Integer.valueOf(doc.getData().get("Privacy").toString());

                        Habit newHabit = new Habit(title, reason, date,repeat, privacy);
                        newHabit.setDone((String) doc.getData().get("Done"));
                        habitList.add(newHabit);
                    }
                }
                fragmentadapter.notifyDataSetChanged();
                pager2.setAdapter(fragmentadapter);
                pager2.setCurrentItem(selectedTab, false);
            }
        });

        // Get the habit event list
        collectionReference2.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                // Clear the old list
                habitEventList.clear();

                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    // Retrieving the habit list
                    //Log.d("New Habit", String.valueOf(document.getData().get("Date")));
                    if (document.getData().get("User Id").equals(UserId)) {
                        String title = (String) document.getData().get("Title");
                        String reason = (String) document.getData().get("Reason");
                        Date date = document.getTimestamp("Date").toDate();
                        Date dateCreated = document.getTimestamp("DateCreated").toDate();
                        List<String> repeat = (List<String>) document.getData().get("Repeat");
                        Integer privacy = Integer.valueOf(document.getData().get("Privacy").toString());
                        String comment = (String) document.getData().get("Comment");
                        String photograph = (String) document.getData().get("Photograph");
                        GoogleMap location = (GoogleMap) document.getData().get("Location");

                        Habit newHabit = new Habit(title, reason, date,repeat, privacy);
                        HabitEvent newHabitEvent = new HabitEvent(newHabit, dateCreated, comment, photograph, location);

                        habitEventList.add(newHabitEvent);
                    }

                }
                fragmentadapter.notifyDataSetChanged();
                pager2.setAdapter(fragmentadapter);
                pager2.setCurrentItem(selectedTab, false);
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

    private void hideButton(int tabIndex) {
        switch (tabIndex) {
            case 2:
                fab.hide();
                break;
            default:
                fab.show();
                break;
        }
    }
}
