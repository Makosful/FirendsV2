package com.github.makosful.friendsv2.gui.controller;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, IMapCallBack {

    // used in  simplified Logging method
    public static String TAG = "mapActivity";

    // GPS Request code
    private int PERMISSION_CODE = 1337;

    // google map ref
    private GoogleMap mMap;

    // friend red
    private Friend friend;

    // google map markers
    MarkerOptions friendMarker;
    MarkerOptions deviceMarker;

    //location & location manager & location listener
    Location deviceLocation;
    LocationListener locationListener;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Button btnShowSelf = findViewById(R.id.btnShowSelf);
        Button btnShowFriend = findViewById(R.id.btnShowFriend);
        
        /**
         * Starts listening to the users location as well as initiate the
         */
        btnShowSelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //grab gps coordinate updates from user device to show directions.
                startListening();
                logCurrentLocation(v);
                zoomToDevicePosition(v);
            }
        });

        // zooms the map to the friends marker
        btnShowFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomToFriendPosition(v);
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        // asynchronous loads in the map
        mapFragment.getMapAsync(this);

        log("Retrieving Friend from extras");
        friend = (Friend) getIntent().getExtras().get(Common.INTENT_MAP_ACTIVITY);
        // FusedLocationProviderClient mfLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = null;

        // request user to manually provide gps permissions for this (FINE and or COARSE location)
        requestGPSPermissions();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add latlng + marker
        friendMarker = new MarkerOptions().position(new LatLng(this.friend.getLatitude(), this.friend.getLongitude()));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        // Already went through permission check. Added AZURE (blue) color to DEVICE marker, RED (default) on FRIEND marker until we can use picture icon
        deviceMarker = new MarkerOptions().position(new LatLng(getLastKnownLocation().getLatitude(), getLastKnownLocation().getLongitude())).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

        // TODO finish by re-adding custom user image icon instead of standard red pointer
        mMap.addMarker(friendMarker.title(this.friend.getName())/*.icon(BitmapDescriptorFactory.fromBitmap(this.friend.getPicture()))*/);
        // center map
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(this.friend.getLatitude(), this.friend.getLongitude())));
    }

    private void logCurrentLocation(View view) {
        deviceLocation = getLastKnownLocation();
        log("CURRENT KNOWN LOCATION: " + deviceLocation);
    }

    /**
     * Requests GPS Permissions to allow locationManager the devices last known location;
     * if not given then bool is null and we won't track the users most recently updated location (last known).
     * @return
     */
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

    /**
     * If given we will start updating last known position with a minimum time of 5000 ms or 1 meter change.
     */
    protected void startListening() {

        Boolean GPSPermissionGiven;
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
                    1, // the minimal distance (meter) between notifications
                    locationListener);
        else
            log("Could not register location listener..");
    }

    /**
     * Requests devices GPS permissions in order to allow map & location to function.
     * Checks automatic permissions (manifest) as well as manual user prompts.
     */
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

    /**
     * Quick little hack to simplify the Log.d command, to not bother using tag etc every time.
     * @param message
     */
    private void log(String message)
    {
        Log.d(TAG, message);
    }

    /**
     * Overriding method to setting the devices current location, purely logged.
     * @param location
     */
    @Override
    public void setCurrentLocation(Location location) {
        String lat = String.valueOf(location.getLatitude());
        String lng = String.valueOf(location.getLongitude());
        log("latitude: " + lat + " & longitude: " + lng);
    }

    /**
     * Button to zoom to friends marker on the map (RED MARKER)
     * @param view
     */
    public void zoomToFriendPosition(View view) {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(this.friend.getLatitude(), this.friend.getLongitude())));
    }

    /**
     * Button to zoom to devices own marker on the map (BLUE MARKER)
     * @param view
     */
    public void zoomToDevicePosition(View view) {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(getLastKnownLocation().getLatitude(), getLastKnownLocation().getLongitude())));
    }
}