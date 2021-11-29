package com.example.recurring_o_city;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

public class YouFollowFragment extends Fragment {

    private ArrayList<String> following;
    private ListView userList;
    private UserAdapter userAdapter;
    private ImageButton backButton;

    public YouFollowFragment() {
        // Required empty public constructor
    }


    public static YouFollowFragment newInstance(ArrayList<String> following) {
        YouFollowFragment fragment = new YouFollowFragment();
        Bundle args = new Bundle();
        args.putSerializable("Following", following);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        following = (ArrayList<String>) getArguments().getSerializable("Following");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_you_follow, container, false);
        backButton = view.findViewById(R.id.send_back_button);

        // Create custom adapter
        userList = view.findViewById(R.id.listview);
        userAdapter = new UserAdapter(getContext(), following,"yff");
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