package com.example.recurring_o_city;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * class to represent who the user is following
 */
public class YouFollowFragment extends Fragment {

    private ArrayList<String> following;
    private ArrayList<Habit> following_habit;
    private ListView userList;
    private UserAdapter userAdapter;
    private ImageButton backButton;
    private FirebaseFirestore db;
    private CollectionReference userReference, habitReference;
    private String userID;

    /**
     * Empty constructor required for instantiation of this class.
     */
    public YouFollowFragment() {
        // Required empty public constructor
    }

    /**
     * instantiate class
     * Creates new instance of {@link YouFollowFragment} class.
     *  {@link ArrayList} of type {@link String} to add to the {@link YouFollowFragment}.
     * @param following
     * @return fragment
     *  New fragment instantiated with {@link Bundle} containing {@link String} objects.
     */
    public static YouFollowFragment newInstance(ArrayList<String> following) {
        YouFollowFragment fragment = new YouFollowFragment();
        Bundle args = new Bundle();
        args.putSerializable("Following", following);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    /**
     * get app state
     * Using the saved instance, gets the list of {@link String} of user is following.
     * @param savedInstanceState
     * New {@link Bundle} object instantiated from {@link #newInstance(ArrayList)} method.
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        following = (ArrayList<String>) getArguments().getSerializable("Following");

    }

    @Override
    /**
     * Sets up {@link com.example.recurring_o_city.ItemAdapter.OnItemClickListener} for each {@link String} object.
     * create UI for user to interact with
     * @return view
     *   Contains the list of {@link Habit} objects.
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_you_follow, container, false);
//        View view = inflater.from(getActivity()).inflate(R.layout.fragment_you_follow, container, false);
        backButton = view.findViewById(R.id.send_back_button);
        db = FirebaseFirestore.getInstance();
        userReference = db.collection("Users");
        habitReference = db.collection("Habits");
        following_habit = new ArrayList<>();

        // Create custom adapter
        userList = view.findViewById(R.id.listview);
        userAdapter = new UserAdapter(getContext(), following,"yff");
        userList.setAdapter(userAdapter);


        // When click on an user, go to new fragment
        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //Get the user ID given that user email
                userReference
                        .whereEqualTo("Email", userList.getItemAtPosition(position))
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    userID = task.getResult().getDocuments().get(0).getId();

                                    // Get the list of all public habit of that user
                                    habitReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                                            // Clear the old list
                                            following_habit.clear();
                                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                                if ((String.valueOf(doc.getData().get("Privacy")).equals("0")) && doc.getData().get("User Id").equals(userID)) {
                                                    String title = (String) doc.getData().get("Title");
                                                    String reason = (String) doc.getData().get("Reason");
                                                    Date date = doc.getTimestamp("Date").toDate();
                                                    List<String> repeat = (List<String>) doc.getData().get("Repeat");
                                                    Integer privacy = Integer.valueOf(doc.getData().get("Privacy").toString());
                                                    Habit newHabit = new Habit(title, reason, date,repeat, privacy);
                                                    following_habit.add(newHabit);
                                                    FollowingUserHabitFragment newFrag = new FollowingUserHabitFragment();
                                                    getActivity().getSupportFragmentManager()
                                                            .beginTransaction()
                                                            .replace(R.id.you_follow_frag, newFrag.newInstance(following_habit))
                                                            .addToBackStack(null)
                                                            .commit();
                                                }
                                            }
                                        }
                                    });
                                }
                            }
                        });
            }
        });

        // When click back button
        /**
         * go back to previous view if backbutton is pressed
         */
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        return view;

    }
}