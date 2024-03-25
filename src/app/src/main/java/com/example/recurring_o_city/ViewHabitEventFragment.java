package com.example.recurring_o_city;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Implements the fragment for viewing the habit event details.
 */
public class ViewHabitEventFragment extends Fragment
        implements EditHabitEventFragment.EditHabitEventFragmentListener{
    /*
    Can be called using:

    ViewHabitEventFragment habitEventFrag = new ViewHabitEventFragment();
    getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.[Fragment Container], habitEventFrag.newInstance([Habit Event Object]))
                .addToBackStack(null).commit();
     */

    private String event_title;
    private String event_reason;
    private String event_date;
    private String event_repeat;
    private String event_privacy;
    private String event_comment;
    private String event_location;
    private String event_image;
    private String event_datedone;
    private String event_datedoneraw;
    private TextView titleText, reasonText, dateText, repeatText, privacyText, commentText, datedoneText, locationText;
    private ImageView eventImage;
    private FirebaseAuth mAuth;
    private double event_latitude, event_longitude;

    /**
     * Creates new instance of {@link ViewHabitEventFragment} class.
     * @param newHabitEvent
     * type {@link HabitEvent} to add to the {@link ViewHabitEventFragment}.
     * @return Fragment
     * New fragment instantiated with {@link Bundle} containing {@link HabitEvent} object.
     */
    // Get the attributes from the Habit object.
    public ViewHabitEventFragment newInstance(HabitEvent newHabitEvent) {
        Bundle args = new Bundle();

        args.putString("event_title", newHabitEvent.getEventHabit().getTitle());
        args.putString("event_reason", newHabitEvent.getEventHabit().getReason());
        args.putString("event_privacy", newHabitEvent.getEventHabit().getPrivacy().toString());
        args.putString("event_image", newHabitEvent.getEventPic());

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        Date date = newHabitEvent.getEventHabit().getDate();
        String date_string = format.format(date);
        args.putString("event_date", date_string);

        SimpleDateFormat format2 = new SimpleDateFormat("yyyy/MM/dd 'at' h:mm a");
        Date date2 = newHabitEvent.getDateCreated();
        String date_string2 = format2.format(date2);
        args.putString("event_datedone", date_string2);

        SimpleDateFormat format3 = new SimpleDateFormat("yyyy/MM/dd/HH/mm");
        Date date3 = newHabitEvent.getDateCreated();
        String date_string3 = format3.format(date3);
        args.putString("event_datedoneraw", date_string3);

        Date simpleDate = newHabitEvent.getDateCreated();
        args.putSerializable("event_date_simple", simpleDate);

        double latitude;
        double longitude;

        if (newHabitEvent.getEventLoc() != null) {
            latitude = newHabitEvent.getEventLoc().getLatitude();
            longitude = newHabitEvent.getEventLoc().getLongitude();
        }
        else {
            latitude = 0.0;
            longitude = 0.0;
        }

        args.putDouble("event_latitude", latitude);
        args.putDouble("event_longitude", longitude);

        String repeat;
        if (newHabitEvent.getEventHabit().getRepeat().size() <= 1){
            repeat = "Does not repeat";
        } else {
            Utility util = new Utility();
            repeat = util.convertRepeat(newHabitEvent.getEventHabit().getRepeat());
        }
        args.putString("event_repeat", repeat);

        ViewHabitEventFragment fragment = new ViewHabitEventFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * sets up  {@link HabitEvent}
     * Sets up {@link com.example.recurring_o_city.ItemAdapter.OnItemClickListener} for {@link HabitEvent} object.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return View
     * contains {@link HabitEvent} object
     */
    // Show View Habit Event Fragment.
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_habit_event_fragment, null);

        titleText      = view.findViewById(R.id.habitevent_title);
        reasonText     = view.findViewById(R.id.habitevent_reason_content);
        dateText       = view.findViewById(R.id.habitevent_date_content);
        repeatText     = view.findViewById(R.id.habitevent_repeat_content);
        privacyText    = view.findViewById(R.id.habitevent_privacy_content);
        commentText    = view.findViewById(R.id.habitevent_comment_content);
        datedoneText   = view.findViewById(R.id.habitevent_datedone_content);
        locationText   = view.findViewById(R.id.habitevent_location_content);
        eventImage    = view.findViewById(R.id.habitevent_image);

        ImageButton editButton = view.findViewById(R.id.habitevent_edit_button);
        ImageButton backButton = view.findViewById(R.id.habitevent_back_button);
        mAuth = FirebaseAuth.getInstance();


        event_title = getArguments().getString("event_title");
        event_reason = getArguments().getString("event_reason");
        event_date = getArguments().getString("event_date");
        event_datedone = getArguments().getString("event_datedone");
        event_datedoneraw = getArguments().getString("event_datedoneraw");
        event_repeat = getArguments().getString("event_repeat");
        event_privacy = getArguments().getString("event_privacy");
        event_image = getArguments().getString("event_image");
        event_latitude = getArguments().getDouble("event_latitude");
        event_longitude = getArguments().getDouble("event_longitude");

        if (event_privacy.equals("0")) {
            event_privacy = "Public";
        } else if (event_privacy.equals("1")){
            event_privacy = "Private";
        }

        if (event_comment == null) {
            event_comment = "";
        }

        if (event_latitude == 0.0 && event_longitude == 0.0) {
            event_location = "";
        }
        else {
            Geocoder geocoder;
            List<Address> addresses;

            geocoder = new Geocoder(this.getContext(), Locale.getDefault());

            // Get the address from coordinate.
            try {
                addresses = geocoder.getFromLocation(event_latitude, event_longitude, 1);
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String street = addresses.get(0).getThoroughfare();
                String streetNum = addresses.get(0).getFeatureName();

                if (street == null) {
                    event_location = streetNum + " " + city + " " + state;
                } else {
                    event_location = street + " " + city + " " + state;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        titleText.setText(event_title);
        reasonText.setText(event_reason);
        dateText.setText(event_date);
        datedoneText.setText(event_datedone);
        repeatText.setText(event_repeat);
        privacyText.setText(event_privacy);
        commentText.setText(event_comment);
        locationText.setText(event_location);

        if (event_image != null) {
            byte[] bytes = Base64.getDecoder().decode(event_image);
            eventImage.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
        }

        // Calls the EditHabitEventFragment.

        editButton.setOnClickListener(new View.OnClickListener() {
            /**
             * @param view
             */
            @Override
            public void onClick(View view) {
                // Call the EditHabitEventFragment
                Date simpleDate = (Date) getArguments().getSerializable("event_date_simple");

                new EditHabitEventFragment()
                        .newInstance(event_title, event_datedoneraw, mAuth.getCurrentUser().getUid(), simpleDate).show(getChildFragmentManager(), "EDIT_HABITEVENT");
            }
        });

        // Pops out a stack, returning to previous fragment.
        backButton.setOnClickListener(new View.OnClickListener() {
            /**
             * @param view
             */
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }

    /**
     * of type {@link String} and {@link Byte} that stores image information.
     * set some info before posting
     * @param comment
     * @param address
     * @param img
     */
    @Override
    public void onEditEventSavePressed(String comment, String address, String img) {
        // Set the comment
        commentText.setText(comment);

        // Set the location
        locationText.setText(address);

        // Set the image
        byte[] bytes = Base64.getDecoder().decode(img);
        eventImage.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
    }
}