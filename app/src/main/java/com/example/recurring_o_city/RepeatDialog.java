package com.example.recurring_o_city;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.List;

public class RepeatDialog extends DialogFragment {
    private EditText repeat_num;
    private Spinner spinner;
    private CheckBox Mon, Tue, Wed, Thur, Fri, Sat, Sun;
    private String freq;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_repeat_dialog, null);

        repeat_num = view.findViewById(R.id.repeat_num);
        spinner = view.findViewById(R.id.spinner);
        Mon = view.findViewById(R.id.Mon);
        Tue = view.findViewById(R.id.Tue);
        Wed = view.findViewById(R.id.Wed);
        Thur = view.findViewById(R.id.Thur);
        Fri = view.findViewById(R.id.Fri);
        Sat = view.findViewById(R.id.Sat);
        Sun = view.findViewById(R.id.Sun);

        // Create spinner with drop down
        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.freq));
        myAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(myAdapter);

        // Create builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Repeat")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Save", (dialogInterface, i) -> {
                    // Get and validate input
                    int num = Integer.valueOf(repeat_num.getText().toString());
                    freq = spinner.getSelectedItem().toString();

                    // Check what day in week was selected


                })
                .create();
    }
}
