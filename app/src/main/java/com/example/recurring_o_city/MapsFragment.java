package com.example.recurring_o_city;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.recurring_o_city.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    private boolean locationPermissionGranted;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location lastKnownLocation;
    private Geocoder geocoder;
    private EditText addressText;
    private FloatingActionButton fab;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    public static MapsFragment newInstance(String userId, String title, Date date, String address) {
        Bundle args = new Bundle();

        args.putString("user_id", userId);
        args.putString("event_title", title);
        args.putSerializable("event_date", date);
        args.putString("event_loc", address);

        MapsFragment fragment = new MapsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_maps, container, false);

        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        addressText = view.findViewById(R.id.map_address_field);
        fab = view.findViewById(R.id.map_edit_fab);

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        List<Address> addresses;
        String address = getArguments().getString("event_loc");
        // Request permission on runtime.
        getLocationPermission();
        getLocationUi();

        // Get the address and marker. Use device location or saved location.
        if (address.isEmpty()) {
            getDeviceLocation();
        }
        else {
            try {
                addresses = geocoder.getFromLocationName(address, 1);
                Address location = addresses.get(0);
                lastKnownLocation.setLatitude(location.getLatitude());
                lastKnownLocation.setLongitude(location.getLongitude());
                createMarker();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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

        // On floating action button click, return the current coordinate.
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putDouble("latitude", lastKnownLocation.getLatitude());
                bundle.putDouble("longitude", lastKnownLocation.getLongitude());

                getActivity().getSupportFragmentManager()
                        .setFragmentResult("RequestKey", bundle);

                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    public void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        }
        else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        locationPermissionGranted = false;
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    locationPermissionGranted = true;
                }
            }
        }
        getLocationUi();
    }

    public void getLocationUi() {
        if (mMap == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            }
            else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }

    }

    public void getDeviceLocation() {
        try{
            if (locationPermissionGranted) {

                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();

                locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            lastKnownLocation = task.getResult();

                            // Update last known location from the current device location.
                            if (lastKnownLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), 15));

                                // Set the marker on current location.
                                createMarker();
                            }

                            else {
                                // Placeholder coordinate, if current location is null.
                                lastKnownLocation.setLatitude(-34);
                                lastKnownLocation.setLongitude(151);
                                mMap.moveCamera(CameraUpdateFactory
                                        .newLatLngZoom(new LatLng(-34, 151), 5));
                            }
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void setLocation(Location lastKnownLocation) {
        List<Address> addresses;
        String address = null;

        try {
            addresses = geocoder.getFromLocation(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), 1);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String street = addresses.get(0).getThoroughfare();
            String streetNum = addresses.get(0).getFeatureName();

            if (street == null) {
                address = streetNum + " " + city + " " + state;
            } else {
                address = street + " " + city + " " + state;
            }
            addressText.setText(address);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createMarker() {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(
                lastKnownLocation.getLatitude(),
                lastKnownLocation.getLongitude()));
        markerOptions.draggable(true);
        markerOptions.title(lastKnownLocation.getLatitude() + " : " + lastKnownLocation.getLongitude());
        mMap.addMarker(markerOptions);

        // Set the location, after getting the marker.
        setLocation(lastKnownLocation);
    }
}