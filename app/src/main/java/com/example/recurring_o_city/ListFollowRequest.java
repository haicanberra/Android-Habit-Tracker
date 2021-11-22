package com.example.recurring_o_city;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class ListFollowRequest extends ArrayAdapter<FollowRequest> {
    private Context context;
    private ArrayAdapter<FollowRequest> requestAdaptor;
    private ArrayList<FollowRequest> requests;

    public ListFollowRequest(Context context, ArrayList<FollowRequest> requests){
        super(context,0, requests);
        this.requests = requests;
        this.context = context;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.follow_request_list, parent,false);
        }

        FollowRequest request = requests.get(position);

        TextView person = view.findViewById(R.id.accept_button);
        TextView accept = view.findViewById(R.id.decline_button);

        person.setText(request.getPerson());
        String temp = Boolean.toString(request.getAccept());
        accept.setText(temp);

        return view;
    }
}
