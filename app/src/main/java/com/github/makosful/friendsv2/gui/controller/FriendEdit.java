package com.github.makosful.friendsv2.gui.controller;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.makosful.friendsv2.Common;
import com.github.makosful.friendsv2.IMapCallBack;
import com.github.makosful.friendsv2.R;
import com.github.makosful.friendsv2.be.Friend;
import com.github.makosful.friendsv2.gui.model.MyLocationListener;

import java.util.Objects;

public class FriendEdit extends AppCompatActivity implements IMapCallBack {

    private static final String TAG = "FriendEdit";

    private Friend friend;
    private EditText txtName;
    private EditText txtPhone;
    private EditText txtEmail;
    private ImageView imageView;

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

    // AppCompatActivity Overrides

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        log("Creating Friend Edit");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_edit);

        log("Retrieving Friend from extras");
        friend = (Friend) Objects.requireNonNull(getIntent().getExtras())
                                 .get(Common.INTENT_FRIEND_EDIT);

        log("Setting default values from Friend");
        txtName = findViewById(R.id.txt_friend_edit_name);
        txtName.setText(friend.getName());
        txtPhone = findViewById(R.id.txt_friend_edit_phone);
        txtPhone.setText(friend.getPhone());
        txtEmail = findViewById(R.id.txt_friend_edit_email);
        txtEmail.setText(friend.getEmail());
        imageView = findViewById(R.id.iv_friend_edit_image);

        log("Gets LocationManager");
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        log("Creation finished successfully");
    }

    @Override
    public void onBackPressed()
    {
        cancel();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if (resultCode == Activity.RESULT_CANCELED)
            return;

        switch (requestCode)
        {
            case Common.ACTIVITY_REQUEST_CODE_CAMERA:
                assert data != null;
                handleImageTaken(data);
                break;
            default:
                Toast.makeText(this, "Unknown result code. Breaking process.", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        log("Permission: " + permissions[0] + " - grantResult: " + grantResults[0]);
    }

    // View Calls

    /**
     * Called on Button press Save
     * @param view The view that calls this method
     */
    public void saveEdits(View view)
    {
        log("Preparing to save edits");

        log("Overriding values in Friend");
        friend.setName(txtName.getText().toString());
        friend.setPhone(txtPhone.getText().toString());
        friend.setEmail(txtEmail.getText().toString());
        log("Override complete");

        log("Creating return Intent");
        Intent returnIntent = new Intent();

        log("Adding modified Friend as extra");
        returnIntent.putExtra(Common.INTENT_FRIEND_EDIT_RESULT, friend);
        setResult(Activity.RESULT_OK, returnIntent);

        log("Returning result to previous activity");
        finish();
    }

    /**
     * Launches the default camera
     * @param view The view that calls this method
     */
    public void takePicture(View view)
    {
        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
            requestPermissions( new String[]{Manifest.permission.CAMERA},
                                Common.PERMISSION_REQUEST_CODE_CAMERA );

        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
        {
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (i.resolveActivity(getPackageManager()) != null)
            {
                startActivityForResult(i, Common.ACTIVITY_REQUEST_CODE_CAMERA);
            }
        }
        else
        {
            log("Permissions have been denied.");
        }
    }

    /**
     * Called on Button press Cancel.
     * @param view The view that calls this method
     */
    public void cancel(View view)
    {
        cancel();
    }

    public void setHome(View view)
    {
        log("Getting last known location");
        Location location = getLastKnownLocation();


        String s = "(" + location.getLatitude() + ";" + location.getLongitude() + ")";

        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    // Internal methods

    public Location getLastKnownLocation()
    {
        boolean GPSPermissionGiven;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            GPSPermissionGiven = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
                                 PackageManager.PERMISSION_GRANTED;
        }
        else {
            GPSPermissionGiven = true;

        }

        if (GPSPermissionGiven)
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000, 1, locationListener);
            locationManager.removeUpdates(locationListener);

            return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        else
            return null;
    }

    /**
     * Handles the image that's returned by the camera. Called from onActivityResult
     * @param data The Intent sent back from the camera
     */
    private void handleImageTaken(Intent data)
    {
        // "data" is the Android default for images
        Bitmap img = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
        imageView.setImageBitmap(img);
    }

    /**
     * Backs out of the activity without saving any data
     */
    private void cancel()
    {
        log("Canceling changes");
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    @Override
    public void setCurrentLocation(Location location) {
        String lat = String.valueOf(location.getLatitude());
        String lng = String.valueOf(location.getLongitude());
        log("latitude: " + lat + " & longitude: " + lng);
    }
}
