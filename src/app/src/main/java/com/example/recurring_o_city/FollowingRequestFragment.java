package com.example.recurring_o_city;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

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

    /**
     * Empty constructor required for instantiation of this class.
     */
    public FollowingRequestFragment() {
        // Required empty public constructor
    }

    /**
     * get pending follow requests, put it in a new fragment
     * Creates new instance of {@link FollowingRequestFragment} class.
     * @param pending
     * {@link ArrayList} of type {@link String} to add to the {@link FollowingRequestFragment}.
     * @return fragment
     * new fragment instantiated with {@link Bundle} containing {@link String} objects.
     */
    public static FollowingRequestFragment newInstance(ArrayList<String> pending) {
        FollowingRequestFragment fragment = new FollowingRequestFragment();
        Bundle args = new Bundle();
        args.putSerializable("Pending", pending);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Using the saved instance, gets the list of {@link String} pending follow requests.
     * @param savedInstanceState
     * New {@link Bundle} object instantiated from {@link #newInstance(ArrayList)} method.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pending = (ArrayList<String>) getArguments().getSerializable("Pending");
    }

    /**
     * Using {@link RecyclerView}, sets up the list of {@link String} to occur today.
     * Sets up {@link com.example.recurring_o_city.ItemAdapter.OnItemClickListener} for each {@link String} object.
     * @param inflater
     *  Layout .xml file instantiated into a {@link View}.
     * @param container
     *  Container for the {@link View} created by {@link LayoutInflater}.
     * @param savedInstanceState
     *  {@link Bundle} instantiated from {@link #newInstance(ArrayList)}.
     * @return view
     * Contains the list of {@link String} objects.
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