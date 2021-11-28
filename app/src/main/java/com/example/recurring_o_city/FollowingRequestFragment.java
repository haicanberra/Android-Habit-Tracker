package com.example.recurring_o_city;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class FollowingRequestFragment extends Fragment {

    private ArrayList<User> pending;
    public FollowingRequestFragment() {
        // Required empty public constructor
    }

    public static FollowingRequestFragment newInstance(ArrayList<User> pending) {
        FollowingRequestFragment fragment = new FollowingRequestFragment();
        Bundle args = new Bundle();
        args.putSerializable("Follow Request", pending);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_following_request, container, false);



        return view;
    }
}