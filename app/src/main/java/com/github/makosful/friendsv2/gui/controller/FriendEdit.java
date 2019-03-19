package com.github.makosful.friendsv2.gui.controller;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.makosful.friendsv2.Common;
import com.github.makosful.friendsv2.R;
import com.github.makosful.friendsv2.be.Friend;

import java.util.ArrayList;

public class FriendEdit extends AppCompatActivity
{

    private static final String TAG = "FriendEdit";

    private Friend friend;

    private EditText txtName;
    private EditText txtPhone;
    private EditText txtEmail;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        log("Creating Friend Edit");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_edit);

        log("Retrieving Friend from extras");
        friend = (Friend) getIntent().getExtras().get(Common.INTENT_FRIEND_EDIT);

        log("Setting default values from Friend");
        txtName = findViewById(R.id.txt_friend_edit_name);
        txtName.setText(friend.getName());
        txtPhone = findViewById(R.id.txt_friend_edit_phone);
        txtPhone.setText(friend.getNumber());
        txtEmail = findViewById(R.id.txt_friend_edit_email);
        txtEmail.setText(friend.getEmail());
        imageView = findViewById(R.id.iv_friend_edit_image);

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
            case Common.CAMERA_REQUEST_CODE:
                handleImageTaken(data);
                break;
            default:
                Toast.makeText(this, "Unknown result code. Breaking process.", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void handleImageTaken(Intent data)
    {
        // "data" is the Android default for images
        Bitmap img = (Bitmap) data.getExtras().get("data");
        imageView.setImageBitmap(img);
    }

    /**
     * Called on Button press Save
     * @param view
     */
    public void saveEdits(View view)
    {
        log("Preparing to save edits");

        log("Overriding values in Friend");
        friend.setName(txtName.getText().toString());
        friend.setNumber(txtPhone.getText().toString());
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
     * Called on Button press Cancel.
     * @param view
     */
    public void cancel(View view)
    {
        cancel();
    }


    public void takePicture(View view)
    {
        handlePermissions();

        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (i.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(i, Common.CAMERA_REQUEST_CODE);
            //Toast.makeText(this, "Opening camera", Toast.LENGTH_SHORT).show();
        }
    }

    private void handlePermissions()
    {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},Common.PERMISSION_REQUEST_CODE);
        } else {
            // Permission has already been granted
        }

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

    private void log(String message)
    {
        Log.d(TAG, message);
    }
}
