package com.example.recurring_o_city;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * This class manages the view permission of the habits.
 */
public class FollowingRequestFragment extends Fragment {

    private ArrayList<String> pending;
    private ImageButton backButton;
    private ListView userList;
    private UserAdapter userAdapter;

    public FollowingRequestFragment() {
        // Required empty public constructor
    }

    /**
     * get pending follow requests, put it in a new fragment
     * @param pending
     * @return fragment
     */
    public static FollowingRequestFragment newInstance(ArrayList<String> pending) {
        FollowingRequestFragment fragment = new FollowingRequestFragment();
        Bundle args = new Bundle();
        args.putSerializable("Pending", pending);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * get app state and pending follow requests
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pending = (ArrayList<String>) getArguments().getSerializable("Pending");
    }

    /**
     * create the UI object for the user to interact wtih
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_following_request, container, false);

        //Create custom adapter
        userList = view.findViewById(R.id.listview);
        backButton = view.findViewById(R.id.send_back_button);
        userAdapter = new UserAdapter(getContext(), pending, "frf");
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