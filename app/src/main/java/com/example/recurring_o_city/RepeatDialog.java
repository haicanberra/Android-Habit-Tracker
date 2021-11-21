package com.example.recurring_o_city;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
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
    private RepeatDialogListener listener;
    List<String> repeat = new ArrayList<>();

    //
    public interface RepeatDialogListener {
        void onRepeatSavePressed(List<String> repeat);
    }


    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            listener = (RepeatDialogListener) getParentFragment();
        } catch (RuntimeException e) {
            // The activity doesn't implement the interface, throw exception
            throw new RuntimeException(context.toString()
                    + " must implement RepeatDialogListener");
        }
    }



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
//                    // Get and validate input
                    String custom_num = repeat_num.getText().toString();
                    //int num = Integer.valueOf(custom_num);
                    freq = spinner.getSelectedItem().toString();

                    // Check what custom day was entered
                    if (!custom_num.equals("")) {
                        repeat.add(custom_num);
                        repeat.add(freq);
                    }
                    // Check what day in week was selected
                    if (Mon.isChecked()) {
                        repeat.add("Mon");
                    }
                    if (Tue.isChecked()) {
                        repeat.add("Tue");
                    }
                    if (Wed.isChecked()) {
                        repeat.add("Wed");
                    }
                    if (Thur.isChecked()) {
                        repeat.add("Thur");
                    }
                    if (Fri.isChecked()) {
                        repeat.add("Fri");
                    }
                    if (Sat.isChecked()) {
                        repeat.add("Sat");
                    }
                    if (Sun.isChecked()) {
                        repeat.add("Sun");
                    }

                    listener.onRepeatSavePressed(repeat);

                })
                .create();
    }

}
