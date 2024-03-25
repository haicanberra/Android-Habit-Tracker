package com.example.recurring_o_city;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Class that creates new fragment for editing {@link Habit} type object.
 */
public class EditHabitFragment extends DialogFragment
        implements RepeatDialog.RepeatDialogListener {

    private EditText habitTitle;
    private EditText habitReason;
    private EditText habitDate;
    private EditText habitRepeat;
    private ImageButton button;
    private ImageButton repeat;
    private Switch habitPrivacy;
    private int privacy;
    private Date nextDate, startDate, oldDate;
    private EditHabitFragmentListener listener;
    private DatePickerDialog calDialog;
    private List<String> repeats, oldRepeat;
    private boolean duplicate;

    private FirebaseFirestore db;
    CollectionReference collectionReference;
    DocumentReference editHabit;

    /**
     * Empty constructor required for instantiation of this class.
     */
    public EditHabitFragment () {
        // Required empty public constructor
    }

    /**
     * Creates new instance of {@link EditHabitFragment} class.
     * @param oldHabitTitle
     *  {@link String} type parameter containing title of {@link Habit} to edit.
     * @param UserId
     *  The user ID of the {@link Habit} object's owner, in type {@link String}.
     * @return fragment
     *  New fragment instantiated with {@link Bundle}.
     */
    public static EditHabitFragment newInstance(String oldHabitTitle, String UserId){
        Bundle args = new Bundle();
        args.putString("habit_title", oldHabitTitle);
        args.putString("User_Id", UserId);

        EditHabitFragment fragment = new EditHabitFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Creates interface between this class and {@link ViewHabitFragment} class.
     */
    public interface EditHabitFragmentListener{
        void onEditSavePressed(Habit newHabit);
    }

    /**
     * Sets up the listener for the interfacing between {@link ViewHabitFragment} and this class.
     * Throws {@link RuntimeException} if the interfacing was unsuccessful.
     * @param context
     *  Environment in which {@link EditHabitFragment} class is launched from.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (EditHabitFragment.EditHabitFragmentListener) getParentFragment();
        } catch (RuntimeException e) {
            // The activity doesn't implement the interface, throw exception
            throw new RuntimeException(context.toString()
                    + " must implement EditHabitFragmentListener");
        }
    }

    /**
     * Required onCreate method for creating the fragment.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Creates the builder for creating new {@link Habit} object.
     * @param savedInstanceState
     *  {@link #newInstance(String, String)} of {@link EditHabitFragment} containing values from {@link Bundle}.
     * @return builder
     *  Dialog fragment for creation of {@link Habit} object.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        // The layout of the edit habit fragment will be same as add habit fragment.
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_habit, null);

        habitTitle = view.findViewById(R.id.habit_name);
        habitReason = view.findViewById(R.id.habit_reason);
        habitDate = view.findViewById(R.id.habit_date);
        button = view.findViewById(R.id.button);
        repeat = view.findViewById(R.id.repeat_button);
        habitPrivacy = view.findViewById(R.id.privacy);
        habitRepeat = view.findViewById(R.id.habit_frequency);

        // Setup DatePickerDialog to pops up when "EDIT" button is clicked.
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        button.setOnClickListener(view1 -> {
            calDialog = new DatePickerDialog(getContext(), (datePicker, mYear, mMonth, mDay)
                    -> habitDate.setText(mYear + "/" + (mMonth + 1) + "/" + mDay), year, month, day);
            calDialog.show();
        });


        // Set the collection reference from the Firebase.
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Habits");

        // Set the document editHabit, and pull the old data from that document.
        // Use .whereEqualTo() to get the query...
        collectionReference
                .whereEqualTo("User Id", getArguments().getString("User_Id"))
                .whereEqualTo("Title", getArguments().getString("habit_title"))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Use the task.getResult() to get the querySnapshot...
                            QuerySnapshot querySnapshot = task.getResult();

                            // Get the querySnapshot to get document reference.
                            DocumentSnapshot docSnapshot = querySnapshot.getDocuments().get(0);

                            // Update values using the documentSnapshot.
                            editHabit = docSnapshot.getReference();
                            habitTitle.setText(docSnapshot.getString("Title"));
                            habitReason.setText(docSnapshot.getString("Reason"));
                            if (docSnapshot.getLong("Privacy") == 0) {
                                habitPrivacy.setChecked(false);
                            }
                            repeats = (List<String>)docSnapshot.get("Repeat");
                            oldRepeat = repeats;
                            if (repeats.contains("NO_REPEAT") || repeats.size() <= 1) {
                                habitRepeat.setText("Does not repeat");
                            } else {
                                Utility util = new Utility();
                                habitRepeat.setText(util.convertRepeat(repeats));
                            }

                            oldDate = docSnapshot.getDate("Date");
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                            String date = dateFormat.format(oldDate);
                            habitDate.setText(date);

                            nextDate = docSnapshot.getDate("Next Date");

                        }
                    }
                });

        // Set up the repeat fragment to pop up when Edit calender is clicked
        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RepeatDialog repeatDialog = new RepeatDialog();
                repeatDialog.show(getChildFragmentManager(), "Repeat");
            }
        });

        habitPrivacy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    privacy = 1;
                } else {
                    privacy = 0;
                }
            }
        });

        habitTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Empty method for TextWatcher.
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Empty method for TextWatcher.
            }
            @Override
            public void afterTextChanged(Editable editable) {
                duplicateTitle(editable.toString());
            }
        });

        // Create builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Edit Habit")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Save", (dialogInterface, i) -> {
                    Date newDate = null;
                    SimpleDateFormat d = new SimpleDateFormat("yyyy/MM/dd");

                    // Get and validate new input from user
                    String title = habitTitle.getText().toString();
                    String reason = habitReason.getText().toString();

                    if (duplicate) {
                        habitTitle.setError("Title must be unique");
                        habitTitle.requestFocus();
                    }

                    try {
                        newDate = d.parse(String.valueOf(habitDate.getText()));

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (habitPrivacy.isChecked()) {
                        privacy = 1;
                    } else {
                        privacy = 0;
                    }

                    startDate = newDate;
                    if (repeats != null && repeats.size() ==3) {
                        Utility util = new Utility();
                        nextDate = util.getNextDate(startDate, null, repeats);
                    } else {
                        nextDate = null;
                    }
                    //Check if new date or reminder is selected
                    if (repeats != oldRepeat || !startDate.equals(oldDate)) {
                        // Reset the complete
                        editHabit.update("Complete", 0);
                    }

                    // Check if input is valid and proceed
                    if (!title.equals("") && newDate != null) {
                        editHabit.update("Title", title);
                        editHabit.update("Reason", reason);
                        editHabit.update("Date", newDate);
                        editHabit.update("Repeat", repeats);
                        editHabit.update("Privacy", privacy);
                        editHabit.update("Next Date", nextDate);

                    }
                    if (!title.equals("") && !reason.equals("") && newDate != null) {
                        //When user clicks save button, add new medicine
                        listener.onEditSavePressed(new Habit(title, reason, newDate, repeats, privacy));
                    }
                }).create();
    }

    /**
     * Interface for the {@link RepeatDialog} class. Receives repeat frequency of the {@link Habit}.
     * @param repeat_list
     *  {@link List} of type {@link String} that stores repeat frequency information.
     */
    @Override
    public void onRepeatSavePressed(List<String> repeat_list) {
        // Get the List<String> of repeats, and set it to habitRepeat text field.
        String repeat_display ="";
        if (repeat_list.size() <= 1) {
            repeat_display = "Does not repeat";
        } else {
            Utility util = new Utility();
            repeat_display = util.convertRepeat(repeat_list);
        }
        habitRepeat.setText(repeat_display);
        repeats = repeat_list;
    }

    /**
     * Method for checking whether title of edited {@link Habit} object is a duplicate.
     * @param title
     * {@link String} type object that serves as title for {@link Habit} object.
     */
    private void duplicateTitle(String title) {
        String userId = getActivity().getIntent().getStringExtra("User Id");

        Query query = collectionReference
                .whereEqualTo("Title", title)
                .whereEqualTo("User Id", userId);

        // This query modifies global boolean variable "duplicate".
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    duplicate = task.getResult().size() != 0;
                }
            }
        });
    }
}
