package com.github.makosful.friendsv2.gui.controller;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.github.makosful.friendsv2.Common;
import com.github.makosful.friendsv2.IMapCallBack;
import com.github.makosful.friendsv2.R;
import com.github.makosful.friendsv2.be.Friend;
import com.github.makosful.friendsv2.gui.model.MyLocationListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, IMapCallBack {

    public static String TAG = "mapActivity";

    private int PERMISSION_CODE = 1337;
    private GoogleMap mMap;
    private Friend friend;
    MarkerOptions friendMarker;
    Location deviceLocation;
    LocationListener locationListener;
    LocationManager locationManager;
    private FusedLocationProviderClient mfLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Button btnShowDirections = findViewById(R.id.btnShowDirections);
        btnShowDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //grab gps coordinate updates from user device to show directions.
                startListening();
                showDirectionsToFriend(v);
            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        log("Retrieving Friend from extras");
        friend = (Friend) getIntent().getExtras().get(Common.INTENT_MAP_ACTIVITY);
        mfLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = null;
        requestGPSPermissions();
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

        // Add latlng + marker
        friendMarker = new MarkerOptions().position(new LatLng(this.friend.getLatitude(), this.friend.getLongitude()));
        //TODO finish by re-adding custom user image icon instead of standard red pointer
        mMap.addMarker(friendMarker.title(this.friend.getName())/*.icon(BitmapDescriptorFactory.fromBitmap(this.friend.getPicture()))*/);
        // center map
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(this.friend.getLatitude(), this.friend.getLongitude())));
    }

    private void showDirectionsToFriend(View view) {
        deviceLocation = getLastKnownLocation();
        log("CURRENT LIVE LOCATION: " + deviceLocation);
    }

    private Location getLastKnownLocation() {

        Boolean GPSPermissionGiven = false;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            GPSPermissionGiven = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        }
        else {
            GPSPermissionGiven = true;
        }
        return GPSPermissionGiven ? locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER): null;
    }

    protected void startListening() {

        Boolean GPSPermissionGiven = false;
        log("Start listening");
        locationListener = new MyLocationListener(this) {
        };

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            GPSPermissionGiven = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED;
        }
        else {
            GPSPermissionGiven = true;
        }
        if (GPSPermissionGiven)
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    5000, // the minimal time (ms) between notifications
                    0, // the minimal distance (meter) between notifications
                    locationListener);
        else
            log("Could not register location listener..");
    }

    private void requestGPSPermissions() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_DENIED) {

                log("Location permission denied");
                String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};

                requestPermissions(permissions, PERMISSION_CODE);
                return;
            } else
            {
                log("Location permission granted!");
            }
        }
        else
            log("Location permission only granted by manifest");
    }

    private void log(String message)
    {
        Log.d(TAG, message);
    }

    @Override
    public void setCurrentLocation(Location location) {
        String lat = String.valueOf(location.getLatitude());
        String lng = String.valueOf(location.getLongitude());
        log("latitude: " + lat + " & longitude: " + lng);
    }
}