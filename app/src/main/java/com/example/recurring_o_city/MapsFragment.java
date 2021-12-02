package com.example.recurring_o_city;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * this fragment deals with displaying the map in the app
 */
public class MapsFragment extends DialogFragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    private boolean locationPermissionGranted;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location lastKnownLocation;
    private Geocoder geocoder;
    private EditText addressText;
    private FloatingActionButton fab;
    private Button saveButton;
    private GeoPoint coordinate;
    private List<Address> addresses;

    /**
     * Try to attach map fragment to screen
     * @param context
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        // Test whether permission has been granted.
        ActivityResultLauncher<String> mPermissionResult = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {
                        if (result) {
                            Log.e("TESTING", "PERMISSION GRANTED");
                            locationPermissionGranted = true;

                            // Immediately load the user position once permission is granted.
                            getLocationUi();
                        } else {
                            Log.e("TESTING", "PERMISSION DENIED");
                        }
                    }
                }
         );

        // Request Permission.
        mPermissionResult.launch(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    /**
     * instantiate class
     * @param userId
     * @param title
     * @param date
     * @param address
     * @return fragment
     */
    public static MapsFragment newInstance(String userId, String title, Date date, GeoPoint address) {
        Bundle args = new Bundle();

        args.putString("user_id", userId);
        args.putString("event_title", title);
        args.putSerializable("event_date", date);
        args.putDouble("event_lat", address.getLatitude());
        args.putDouble("event_lon", address.getLongitude());

        MapsFragment fragment = new MapsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * create UI to interact with user
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_maps, container, false);

        double latitude = getArguments().getDouble("event_lat");
        double longitude = getArguments().getDouble("event_lon");

        addressText = view.findViewById(R.id.map_address_field);
        fab = view.findViewById(R.id.search_map_fab);
        saveButton = view.findViewById(R.id.save_location_button);

        // If latitude and longitude are both zero, then location is null.
        if (latitude == 0.0 && longitude == 0.0) {
            coordinate = null;
        }
        else {
            coordinate = new GeoPoint(latitude, longitude);
        }

        // Construct a FusedLocationProviderClient and Geocoder.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }

    /**
     * when fragment is loaded, load the google map API
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        // Enable UI to track current device location.
        getLocationUi();

        // Track the current device location.
        getDeviceLocation();

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDrag(@NonNull Marker marker) {
                // Empty method for OnMarkerDragListener.
            }
            @Override
            public void onMarkerDragEnd(@NonNull Marker marker) {
                // Enter marker destination to the address text field.
                lastKnownLocation.setLatitude(marker.getPosition().latitude);
                lastKnownLocation.setLongitude(marker.getPosition().longitude);
                setLocation(lastKnownLocation);
            }
            @Override
            public void onMarkerDragStart(@NonNull Marker marker) {
                // Empty method for OnMarkerDragListener.
            }
        });

        // On save button click, return the current coordinate.
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!addressText.getText().toString().equals("")) {
                    Bundle bundle = new Bundle();

                    bundle.putString("newAddress", addressText.getText().toString());
                    bundle.putDouble("latitude", lastKnownLocation.getLatitude());
                    bundle.putDouble("longitude", lastKnownLocation.getLongitude());

                    // Send the bundle of information back to the editHabitEventFragment.
                    getActivity().getSupportFragmentManager()
                            .setFragmentResult("MapRequest", bundle);

                    // Pop back out of the backstack.
                    getActivity().getSupportFragmentManager().popBackStack();
                }
                else {
                    Toast.makeText(
                            getActivity(),
                            "Cannot save empty address",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        // On floating button action click, get the address from textbox and update marker.
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // If location permission is granted, zoom to current location of marker.
                if (addressText.getText().toString().equals("")) {
                    Toast.makeText(
                            getActivity(),
                            "Cannot search empty address",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                mMap.clear();

                try {
                    addresses = geocoder.getFromLocationName(addressText.getText().toString(), 1);
                    Address address = addresses.get(0);

                    lastKnownLocation.setLatitude(address.getLatitude());
                    lastKnownLocation.setLongitude(address.getLongitude());

                    moveCamera(lastKnownLocation);
                    createMarker(lastKnownLocation);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // On map click, create marker on that position and set coordinate.
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                if (lastKnownLocation != null) {
                    mMap.clear();
                    lastKnownLocation.setLatitude(latLng.latitude);
                    lastKnownLocation.setLongitude(latLng.longitude);
                    createMarker(lastKnownLocation);
                    setLocation(lastKnownLocation);
                }
            }
        });
    }

    /**
     * get location if permission is granted
     */
    public void getLocationUi() {
        if (mMap == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * if permission is granted, get user location
     */
    public void getDeviceLocation() {
        try{
            // If location permission is granted, build coordinate from getLastLocation().
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            lastKnownLocation = task.getResult();

                            // If previous coordinate exist, center on that.
                            if (coordinate != null) {
                                try {
                                    addresses = geocoder
                                            .getFromLocation(coordinate.getLatitude(), coordinate.getLongitude(), 1);
                                    Address location = addresses.get(0);
                                    lastKnownLocation.setLatitude(location.getLatitude());
                                    lastKnownLocation.setLongitude(location.getLongitude());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            // If not record of previous coordinate, use device location.
                            else if (lastKnownLocation != null) {
                                try {
                                    addresses = geocoder
                                            .getFromLocation(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), 1);
                                    Address location = addresses.get(0);
                                    lastKnownLocation.setLatitude(location.getLatitude());
                                    lastKnownLocation.setLongitude(location.getLongitude());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            moveCamera(lastKnownLocation);
                            createMarker(lastKnownLocation);
                            setLocation(lastKnownLocation);
                        }
                    }
                });
            }
            // If no permission is granted, create default coordinate and build from there.
            else {
                lastKnownLocation = new Location("Default coordinate");

                if (coordinate != null) {
                    double newLatitude = coordinate.getLatitude();
                    double newLongitude = coordinate.getLongitude();

                    lastKnownLocation.setLatitude(newLatitude);
                    lastKnownLocation.setLongitude(newLongitude);

                    moveCamera(lastKnownLocation);
                    createMarker(lastKnownLocation);
                    setLocation(lastKnownLocation);
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    /**
     * move the top down view into the coordinates
     * @param location
     */
    public void moveCamera(Location location) {
        LatLng cameraPos = new LatLng(
                location.getLatitude(),
                location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cameraPos, 15));
    }

    /**
     * send location to google map
     * @param location
     */
    public void setLocation(Location location) {
        String newAddress;

        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String street = addresses.get(0).getThoroughfare();
            String streetNum = addresses.get(0).getFeatureName();

            if (street == null) {
                newAddress = streetNum + " " + city + " " + state;
            } else {
                newAddress = street + " " + city + " " + state;
            }
            addressText.setText(newAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * put the marker on the map
     * @param location
     */
    public void createMarker(Location location) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(
                location.getLatitude(),
                location.getLongitude()));
        markerOptions.draggable(true);
        mMap.addMarker(markerOptions);
    }
}