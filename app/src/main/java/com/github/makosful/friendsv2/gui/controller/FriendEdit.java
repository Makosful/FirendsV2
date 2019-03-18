package com.github.makosful.friendsv2.gui.controller;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.github.makosful.friendsv2.Common;
import com.github.makosful.friendsv2.R;
import com.github.makosful.friendsv2.be.Friend;

public class FriendEdit extends AppCompatActivity
{

    private static final String TAG = "FriendEdit";

    private Friend friend;

    private EditText txtName;
    private EditText txtPhone;
    private EditText txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        log("Creating Friend Edit");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_friend);

        log("Retrieving Friend from extras");
        friend = (Friend) getIntent().getExtras().get(Common.INTENT_FRIEND_EDIT);

        log("Setting default values from Friend");
        txtName = findViewById(R.id.txt_friend_edit_name);
        txtName.setText(friend.getName());
        txtPhone = findViewById(R.id.txt_friend_edit_phone);
        txtPhone.setText(friend.getNumber());
        txtEmail = findViewById(R.id.txt_friend_edit_email);
        txtEmail.setText(friend.getEmail());

        log("Creation finished successfully");
    }

    @Override
    public void onBackPressed()
    {
        cancel();
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
