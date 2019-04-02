package com.github.makosful.friendsv2.gui.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.makosful.friendsv2.Common;
import com.github.makosful.friendsv2.R;
import com.github.makosful.friendsv2.be.Friend;

public class FriendDetail extends AppCompatActivity
{
    private static final String TAG = "FriendDetail";

    private Friend friend;

    private TextView name;
    private TextView phone;
    private TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        log("Creating Friend Detail");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_detail);

        log("Retrieving Friend from extras");
        friend = (Friend) getIntent().getExtras().get(Common.INTENT_FRIEND_DETAIL);

        log("Setting default values from Friend");
        name = findViewById(R.id.tv_friend_list_name);
        name.setText(friend.getName());
        phone = findViewById(R.id.tv_friend_detail_phone);
        phone.setText(friend.getPhone());
        email = findViewById(R.id.tv_friend_detail_email);
        email.setText(friend.getEmail());

        log("Finished creating Friend Detail");
    }

    public void showFriendOnMap(View view) {
        log("Creating Map Activity");
        Intent i = new Intent(this, MapsActivity.class);
        i.putExtra(Common.INTENT_MAP_ACTIVITY, friend);

        log("Starting Map activity");
        startActivity(i);
    }

    public void editFriend(View view)
    {
        log("Preparing to edit Friend");

        Intent i = new Intent(this, FriendEdit.class);
        i.putExtra(Common.INTENT_FRIEND_EDIT, friend);

        log("Starting Friend Edit activity");

        startActivityForResult(i, Common.ACTIVITY_REQUEST_CODE_FRIEND_EDIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        log("Returning from Friend Edit");

        Friend friend = (Friend)data.getExtras().get(Common.INTENT_FRIEND_EDIT_RESULT);

        log("Parsing result code");
        switch (resultCode)
        {
            case Activity.RESULT_OK:
                log("Results came back as OK");
                saveResult(friend);
                break;
            default: Toast.makeText(this, "", Toast.LENGTH_SHORT).show(); break;
        }
    }

    private void saveResult(Friend friend)
    {
        log("Logging changes to Friend");
        this.friend = friend;
        this.name.setText(friend.getName());
        this.phone.setText(friend.getPhone());
        this.email.setText(friend.getEmail());
    }

    private void log(String message)
    {
        Log.d(TAG, message);
    }
}
