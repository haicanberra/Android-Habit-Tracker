package com.example.recurring_o_city;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SendRequestFragment extends DialogFragment {
    private EditText person;
    private SendRequestFragment.OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        void onOKPressed(FollowRequest newFollowRequest);
        void onOkPressed(com.example.recurring_o_city.FollowRequest followRequest);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (SendRequestFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstance) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.send_follow, null);
        person = view.findViewById(R.id.PersonNameEntry);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Send Request")
                .setNegativeButton("back", null)
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        String personName = person.getText().toString();
                        listener.onOkPressed(new FollowRequest(personName, Boolean.TRUE ));
                    }
                }).create();
    }
}
