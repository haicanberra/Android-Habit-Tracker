package com.example.recurring_o_city;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class YouFollowFragment extends Fragment {

    private ArrayList<User> following;
    private ListView userList;
    private UserAdapter userAdapter;
    public YouFollowFragment() {
        // Required empty public constructor
    }


    public static YouFollowFragment newInstance(ArrayList<User> following) {
        YouFollowFragment fragment = new YouFollowFragment();
        Bundle args = new Bundle();
        args.putSerializable("Following", following);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        following = (ArrayList<User>) getArguments().getSerializable("Following");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_you_follow, container, false);
        // Create custom adapter
        userList = view.findViewById(R.id.listview);
        userAdapter = new UserAdapter(getContext(), following);
        userList.setAdapter(userAdapter);

        return view;

    }
}