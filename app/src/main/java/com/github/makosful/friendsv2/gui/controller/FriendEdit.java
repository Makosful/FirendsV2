package com.github.makosful.friendsv2.gui.controller;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
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
import com.github.makosful.friendsv2.gui.model.SingleShotLocationProvider;

import java.util.Objects;

public class FriendEdit extends AppCompatActivity implements IMapCallBack, FriendMeta.OnFragmentInteractionListener, SingleShotLocationProvider.LocationCallback {

    private static final String TAG = "FriendEdit";

    private Friend friend;

    /**
     * Small hack to minimize repetitive calls of the Log.d() method
     * @param message The message to log
     */
    private static void log(String message) {
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

        log("Creation finished successfully");
    }

    /**
     * Overrides the functionality of the back button, to properly end this Activity
     */
    @Override
    public void onBackPressed() {
        finishActivity();
    }

    /**
     * Callback from IMapCallBack interface.
     * Unused in the current context, but necessary
     * @param location Unused.
     */
    @Override
    public void setCurrentLocation(Location location) {
         // Does nothing in this context
    }

    /**
     * Callback method from FriendMeta.OnFragmentInteractionListener
     * This method updates the local instance of Friend with the instance created in FriendMeta
     * @param friend The new instance of Friend
     */
    @Override
    public void updateFriend(Friend friend) {
        // Location is managed in this class
        // The location of the Friend returned from FriendMeta is overridden by the location from the local instance
        Friend temp = friend;
        temp.setLatitude(this.friend.getLatitude());
        temp.setLongitude(this.friend.getLongitude());
        this.friend = temp;
    }

    /**
     * Callback method from SingleShotLocationProvider.LocationCallback
     * This method updates the local friend's lat/long.
     * @param location The location with the latest coordinates.
     */
    @Override
    public void onNewLocationAvailable(Location location) {
        this.friend.setLatitude(location.getLatitude());
        this.friend.setLongitude(location.getLongitude());

        Toast.makeText(this, "Latitude: " + friend.getLatitude() + ", Longitude: " + friend.getLongitude(), Toast.LENGTH_SHORT).show();
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

    /**
     * Method should be called from View (XML)
     * Checks for Fine Access Permission.
     * This method sets the current location as the Friend's Location.
     * @param view The View that calls this method.
     */
    public void setHome(View view) {
        int permissionDenied = PackageManager.PERMISSION_DENIED;
        String accessFineLocation = Manifest.permission.ACCESS_FINE_LOCATION;

        if (checkSelfPermission(accessFineLocation) == permissionDenied) {
            requestPermissions(new String[]{accessFineLocation}, Common.PERMISSION_REQUEST_CODE_LOCATION);
        }

        SingleShotLocationProvider.requestSingleUpdate(this, this);
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
