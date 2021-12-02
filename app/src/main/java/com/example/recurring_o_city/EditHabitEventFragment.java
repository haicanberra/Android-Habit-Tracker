package com.example.recurring_o_city;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Creates new fragment that prompts user to edit the {@link HabitEvent}.
 */
public class EditHabitEventFragment extends DialogFragment {

    private TextView eventTitle;
    private EditText eventComment;
    private EditText eventLocation;
    private ImageButton mapButton;
    private ImageButton photoButton;
    private ImageView imageView;
    private byte[] bytes;
    private String event_comment, event_image;
    private EditHabitEventFragment.EditHabitEventFragmentListener listener;
    private FirebaseFirestore db;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private GeoPoint oldLocation;
    CollectionReference collectionReference;
    DocumentReference editHabitEvent;

    private GeoPoint newCoordinate;

    /**
     * Creates new instance of {@link EditHabitEventFragment} class.
     * instantiates the class
     * @param event_title
     * @param event_datedone
     * @param UserId
     * @param date
     * @return fragment
     *  New fragment instantiated with {@link Bundle}.
     */
    public static EditHabitEventFragment newInstance(String event_title, String event_datedone, String UserId, Date date) {
        Bundle args = new Bundle();

        args.putString("event_datedone", event_datedone);
        // String acts as key for retrieving Firebase document
        args.putString("event_title", event_title);

        // Get location to show in edit box?
//        args.putString("event_location", newHabitEvent.getEventLoc());

        args.putString("User_Id", UserId);

        args.putSerializable("event_date_simple", date);

        EditHabitEventFragment fragment = new EditHabitEventFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * save the data, when the user presses save
     */
    public interface EditHabitEventFragmentListener{
        void onEditEventSavePressed(String newComment, String address, String img);
    }

    /**
     * @param context
     * try to attach to environment
     * Sets up interface between this class and {@link ViewHabitEventFragment} class.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (EditHabitEventFragment.EditHabitEventFragmentListener) getParentFragment();
        } catch (RuntimeException e) {
            // The activity doesn't implement the interface, throw exception
            throw new RuntimeException(context.toString()
                    + " must implement EditHabitEventFragmentListener");
        }
    }

    /**
     * get current app state
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * creates the dialog popup
     * Creates {@link AlertDialog} for creating {@link HabitEvent} object.
     * @param savedInstanceState
     *  Required {@link Bundle} object for instantiating onCreateDialog method.
     * @return Dialog
     *  Dialog fragment for creation of {@link HabitEvent} object.
     */
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.edit_habit_event_fragment, null);

        eventTitle = view.findViewById(R.id.editevent_title);
        eventComment = view.findViewById(R.id.editevent_comment);
        eventLocation = view.findViewById(R.id.editevent_location);
        mapButton = view.findViewById(R.id.editevent_map);
        photoButton = view.findViewById(R.id.editevent_photo);
        imageView = view.findViewById(R.id.editevent_image);

        // Set the collection reference from the Firebase.
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Habit Events");


        // Get reference to the selected document
        collectionReference
                .whereEqualTo("User Id", getArguments().getString("User_Id"))
                .whereEqualTo("Title", getArguments().getString("event_title"))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    /**
                     * check the validity of the firebase snapshot and user inputs
                     * @param task
                     */
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document: task.getResult()) {
                                // String to date and back to string to extract yyyy/MM/dd/HH/mm
                                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd/HH/mm");
                                String date1_string = getArguments().getString("event_datedone");
                                Date date1;
                                try {
                                    date1 = format.parse(date1_string);
                                    date1_string = format.format(date1);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                // Date to string
                                Date date2 = document.getTimestamp("DateCreated").toDate();
                                String date2_string = format.format(date2);

                                // Fetch data if date created is the same (2 string above are the same)
                                if (date1_string.equals(date2_string)) {
                                    // Set the habit event details
                                    eventTitle.setText(document.getString("Title"));
                                    eventComment.setText(document.getString("Comment"));

                                    oldLocation = document.getGeoPoint("Location");

                                    if (oldLocation != null && eventLocation.getText().toString().isEmpty()) {
                                        eventLocation.setText(fetchAddress(oldLocation));
                                    }

                                    if (oldLocation == null) {
                                        oldLocation = new GeoPoint(0.0,0.0);
                                    }

                                    event_image = document.getString("Photograph");
                                    if (event_image != null) {
                                        byte[] bytes = Base64.getDecoder().decode(event_image);
                                        imageView.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                                    }
                                    // Get reference to the selected document
                                    editHabitEvent = document.getReference();
                                    break;
                                }
                            }
                        }
                    }
                });

        /**
         * Allows app to display photo
         */
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            /**
             * returns bytes or photo
             * @param result
             */
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && result.getData() != null){
                    Bitmap bitmap = (Bitmap) result.getData().getExtras().get("data");
                    imageView.setImageBitmap(bitmap);

                    // Compress into array of bytes.
                    int size = bitmap.getWidth() * bitmap.getHeight();
                    ByteArrayOutputStream out = new ByteArrayOutputStream(size);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    bytes = out.toByteArray();
                }
            }
        });

        /**
         * take photo
         */
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            /**
             * @param View
             */
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    activityResultLauncher.launch(intent);
                }
            }
        });

        /**
         * opens map activity component of the app
         */
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            /**
             * @param View
             * request map fragment
             */
            public void onClick(View view) {
                String mUserId = getArguments().getString("User_Id");
                String mTitle = eventTitle.getText().toString();
                Date mCreated = (Date) getArguments().getSerializable("event_date_simple");
                GeoPoint mAddress = new GeoPoint(oldLocation.getLatitude(), oldLocation.getLongitude());

                MapsFragment fragment =
                        MapsFragment.newInstance(mUserId, mTitle, mCreated, mAddress);

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.drawer_layout, fragment)
                        .addToBackStack(null).commit();
            }
        });

        /**
         * retrieves map fragment, after request has been sent
         */
        getActivity().getSupportFragmentManager().setFragmentResultListener(
                "MapRequest", this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        String newAddress = result.getString("newAddress");
                        double latitude = result.getDouble("latitude");
                        double longitude = result.getDouble("longitude");

                        newCoordinate = new GeoPoint(latitude, longitude);
                        eventLocation.setText(newAddress);
                    }
                });

        /**
         * @param context
         * edit habit fragment builder
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Edit Habit Event")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Save", (dialogInterface, i) -> {
                    // Get input from user, can be null as optional
                    String comment = eventComment.getText().toString();
                    String address = eventLocation.getText().toString();

                    // Get the image (byte array) and convert to list to store on firebase
                    String img = "";
                    if (bytes != null){
                        img = Base64.getEncoder().encodeToString(bytes);
                    }
                    else {
                        if (event_image != null) {
                            img = event_image;
                        }
                    }

                    // Get the location

                    // Update to database
                    editHabitEvent.update("Comment", comment);
                    editHabitEvent.update("Photograph", img);
                    editHabitEvent.update("Location", newCoordinate);

                    // When user clicks save button, add these details to habit event
                    listener.onEditEventSavePressed(comment, address, img);
                }).create();

    }

    /**
     * Interface for {@link GeoPoint} class
     * goes get the address
     * @param geoPoint
     * @return address
     */
    public String fetchAddress(GeoPoint geoPoint) {
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        List<Address> addresses;
        String address = null;

        try {
            addresses = geocoder.getFromLocation(geoPoint.getLatitude(), geoPoint.getLongitude(), 1);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String street = addresses.get(0).getThoroughfare();

            if (street == null) {
                street = addresses.get(0).getFeatureName() + " ";
            } else {
                street = street + " ";
            }

            if (state == null) {
                state = "";
            } else {
                state = state + " ";
            }

            if (city == null) {
                city = "";
            } else {
                city = city + " ";
            }

            address = street + city + state;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }
}
