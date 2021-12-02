package com.example.recurring_o_city;

import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * This fragment deals with displaying everyone that follow the user
 */
public class FollowYouFragment extends Fragment {

    private ArrayList<String> follower;
    private ImageButton backButton;
    private ListView userList;
    private UserAdapter userAdapter;

    /**
     * Empty constructor required for instantiation of this class.
     */
    public FollowYouFragment() {
        // Required empty public constructor
    }

    /**
     * instantiate the class with the list of followers
     * Creates new instance of {@link FollowYouFragment} class.
     * @param follower
     * {@link ArrayList} of type {@link String} to add to the {@link FollowYouFragment}.
     * @return fragment
     * New fragment instantiated with {@link Bundle} containing {@link String} objects.
     */
    public static FollowYouFragment newInstance(ArrayList<String> follower) {
        FollowYouFragment fragment = new FollowYouFragment();
        Bundle args = new Bundle();
        args.putSerializable("Follower", follower);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * get app state and list of followers
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        follower = (ArrayList<String>) getArguments().getSerializable("Follower");
    }

    /**
     * Display UI to user
     * Using {@link RecyclerView}, sets up the list of {@link String} of followers.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return view
     * Contains the list of {@link String} objects.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_follow_you, container, false);
        backButton = view.findViewById(R.id.send_back_button);

        // Custom adapter
        userList = view.findViewById(R.id.listview);
        userAdapter = new UserAdapter(getContext(), follower,"fyf");
        userList.setAdapter(userAdapter);

        // When click back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        return view;
    }
}