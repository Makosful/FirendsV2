package com.github.makosful.friendsv2.gui.controller;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.makosful.friendsv2.Common;
import com.github.makosful.friendsv2.R;
import com.github.makosful.friendsv2.be.Friend;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "MapActivity";

    private GoogleMap mMap;
    private Friend friend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        log("Retrieving Friend from extras");
        friend = (Friend) getIntent().getExtras().get(Common.INTENT_MAP_ACTIVITY);
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

        // Add a marker in Sydney and move the camera
        LatLng friend = new LatLng(this.friend.getLatitude(), this.friend.getLongitude());

        //TODO finish by re-adding custom user image icon instead of standard red pointer
        mMap.addMarker(new MarkerOptions().position(friend).title(this.friend.getName())/*.icon(BitmapDescriptorFactory.fromBitmap(this.friend.getPicture()))*/);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(friend));
    }

    private void log(String message)
    {
        Log.d(TAG, message);
    }
}
