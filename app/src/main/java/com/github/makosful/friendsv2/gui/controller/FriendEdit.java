package com.github.makosful.friendsv2.gui.controller;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.makosful.friendsv2.Common;
import com.github.makosful.friendsv2.IMapCallBack;
import com.github.makosful.friendsv2.R;
import com.github.makosful.friendsv2.be.Friend;
import com.github.makosful.friendsv2.gui.model.MyLocationListener;

import java.util.Objects;

public class FriendEdit extends AppCompatActivity implements IMapCallBack, FriendMeta.OnFragmentInteractionListener {

    private static final String TAG = "FriendEdit";

    private Friend friend;

    private LocationManager locationManager;
    private LocationListener locationListener;

    public FriendEdit()
    {
        locationListener = new MyLocationListener(this);
    }

    private static void log(String message)
    {
        Log.d(TAG, message);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        log("Creating Friend Edit");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_edit);

        this.friend = (Friend) Objects.requireNonNull(getIntent().getExtras()).get(Common.INTENT_FRIEND_EDIT);
        FriendMeta frag = (FriendMeta) getSupportFragmentManager().findFragmentById(R.id.frag_friend_edit);
        assert frag != null;
        frag.setFriend(friend);

        log("Gets LocationManager");
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        log("Creation finished successfully");
    }

    @Override
    public void onBackPressed() {
        finishActivity();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        log("Permission: " + permissions[0] + " - grantResult: " + grantResults[0]);
    }

    @Override
    public void setCurrentLocation(Location location) {
        String lat = String.valueOf(location.getLatitude());
        String lng = String.valueOf(location.getLongitude());
        log("latitude: " + lat + " & longitude: " + lng);
    }

    @Override
    public void updateFriend(Friend friend) {
        this.friend = friend;
    }

    /**
     * Called on Button press Save
     * @param view The view that calls this method
     */
    public void saveEdits(View view) {
        log("Preparing to save edits");

        log("Creating return Intent");
        Intent returnIntent = new Intent();

        log("Adding modified Friend as extra");
        returnIntent.putExtra(Common.INTENT_FRIEND_EDIT_RESULT, friend);
        setResult(Activity.RESULT_OK, returnIntent);

        log("Returning result to previous activity");
        finish();
    }

    /**
     * Called on Button press Cancel.
     * @param view The view that calls this method
     */
    public void cancel(View view) {
        finishActivity();
    }

    public void setHome(View view) {
        log("Getting last known location");
        Location location = getLastKnownLocation();


        String s = "(" + location.getLatitude() + ";" + location.getLongitude() + ")";

        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    public Location getLastKnownLocation() {
        boolean GPSPermissionGiven;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            GPSPermissionGiven = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
                                 PackageManager.PERMISSION_GRANTED;
        } else {
            GPSPermissionGiven = true;
        }

        if (GPSPermissionGiven) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000, 1, locationListener);
            locationManager.removeUpdates(locationListener);

            return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } else {
            return null;
        }
    }

    /**
     * Backs out of the activity without saving any data
     */
    private void finishActivity() {
        log("Canceling changes");
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
}
