package com.example.recurring_o_city;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * the class that sets the repeat settings of a habit
 */
public class RepeatDialog extends DialogFragment {
    private EditText repeat_num, occur_num;
    private Spinner spinner;
    private CheckBox Mon, Tue, Wed, Thur, Fri, Sat, Sun;
    private String freq;
    private RepeatDialogListener listener;
    private LinearLayout days_repeat;
    private RadioGroup radiogroup;
    private TextView title_repeat;
    List<String> repeat = new ArrayList<>();

    /**
     * the list of repeat settings
     */
    public interface RepeatDialogListener {
        void onRepeatSavePressed(List<String> repeat);
    }

    /**
     * Override the Fragment.onAttach() method to instantiate RepeatDialog
     * @param context
     */
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

    /**
     * create the UI the user interacts with
     * @param savedInstanceState
     * @return dialog
     */
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
        days_repeat = view.findViewById(R.id.days_repeat);
        occur_num = view.findViewById(R.id.num_occur);
        radiogroup = view.findViewById(R.id.radio_group);
        title_repeat = view.findViewById(R.id.title_repeat);

        title_repeat.setVisibility(View.VISIBLE);
        days_repeat.setVisibility(View.VISIBLE);
        // Create spinner with drop down
        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.freq));
        myAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(myAdapter);

        // Check whether day or week is clicked
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String selected_item = adapterView.getItemAtPosition(position).toString();
                if (selected_item.equals("week")) {
                    repeat_num.setVisibility(View.GONE);
                } else if (selected_item.equals("day")) {
                    title_repeat.setVisibility(View.GONE);
                    days_repeat.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        // By default, set never is checked
        radiogroup.check(R.id.never_end);

        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            /**
             * toggle if repeat does occur
             * @param radioGroup
             * @param i
             */
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i) {
                    case R.id.never_end:
                        // Never is checked
                        occur_num.setEnabled(false);
                        break;
                    case R.id.after_end:
                        // After is checked
                        occur_num.setEnabled(true);
                        break;
                }
            }
        });

        // Set default value for custom_num
        repeat_num.setText("1");

        // Create builder
        /**
         * dialog builder
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Repeat")
                .setNegativeButton("Cancel", null)
                .setNeutralButton("NO REPEAT", (dialogInterface, i) -> {
                    repeat.add("NO_REPEAT");
                    listener.onRepeatSavePressed(repeat);
                })
                .setPositiveButton("Save", (dialogInterface, i) -> {
                    // Get and validate input
                    String custom_num = repeat_num.getText().toString();
                    freq = spinner.getSelectedItem().toString();

                    // Check what custom day was entered
                    if (!custom_num.equals("") && !custom_num.equals("0")) {
                        repeat.add(custom_num);
                        repeat.add(freq);
                    }

                    // Check if day or week is selected
                    if (freq.equals("week")) {
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
                            repeat.add("Thu");
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
                    }
                    // Check if none checkbox is chosen, set day repeat to today
                    Utility util = new Utility();
                    SimpleDateFormat name_format = new SimpleDateFormat("EEE");
                    String date_name = name_format.format(util.getCurrentDate());

                    if (!Mon.isChecked() && !Tue.isChecked() && !Wed.isChecked() && !Thur.isChecked()
                    &&!Fri.isChecked() && !Sat.isChecked() && !Sun.isChecked()) {
                        repeat.add(date_name);
                    }

                    // Check if user chooses end date or not
                    int radioID = radiogroup.getCheckedRadioButtonId();
                    RadioButton radiobutton = radiogroup.findViewById(radioID);
                    String num = occur_num.getText().toString();

                    if (radiobutton.getText().equals("Never")) {
                        repeat.add("never");
                    } else {
                        if (!num.equals("") && !num.equals("0")) {
                            repeat.add(num);
                        }
                    }

                    if (!custom_num.equals("") && !custom_num.equals("0")) {
                        if (!radiobutton.getText().equals("Never") && !num.equals("") && !num.equals("0")) {
                            listener.onRepeatSavePressed(repeat);
                        } else if (radiobutton.getText().equals("Never")) {
                            listener.onRepeatSavePressed(repeat);
                        }
                    }

                })
                .create();
    }

}
