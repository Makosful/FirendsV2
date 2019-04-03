package com.github.makosful.friendsv2.gui.controller;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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

import java.util.Objects;

public class FriendDetail extends AppCompatActivity {
    private static final String TAG = "FriendDetail";

    private Friend friend;

    private TextView name;
    private TextView phone;
    private TextView email;
    private TextView website;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        log("Creating Friend Detail");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_detail);

        log("Retrieving Friend from extras");
        friend = (Friend) Objects.requireNonNull(getIntent().getExtras()).get(Common.DATA_FRIEND_DETAIL);

        log("Setting default values from Friend");
        name = findViewById(R.id.tv_friend_list_name);
        name.setText(friend.getName());

        phone = findViewById(R.id.tv_friend_detail_phone);
        phone.setText(friend.getPhone());

        email = findViewById(R.id.tv_friend_detail_email);
        email.setText(friend.getEmail());

        website = findViewById(R.id.tv_friend_detail_website);
        website.setText(friend.getWebsite());

        log("Finished creating Friend Detail");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        log("Returning from Friend Edit");

        Friend friend = (Friend) data.getExtras().get(Common.INTENT_FRIEND_EDIT_RESULT);

        log("Parsing result code");
        switch (resultCode) {
            case Activity.RESULT_OK:
                log("Results came back as OK");
                saveResult(friend);
                break;
            default:
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void showFriendOnMap(View view) {
        log("Creating Map Activity");
        Intent i = new Intent(this, MapsActivity.class);
        i.putExtra(Common.INTENT_MAP_ACTIVITY, friend);

        log("Starting Map activity");
        startActivity(i);
    }

    public void editFriend(View view) {
        log("Preparing to edit Friend");

        Intent i = new Intent(this, FriendEdit.class);
        i.putExtra(Common.INTENT_FRIEND_EDIT, friend);

        log("Starting Friend Edit activity");

        startActivityForResult(i, Common.ACTIVITY_REQUEST_CODE_FRIEND_EDIT);
    }

    private void saveResult(Friend friend) {
        log("Logging changes to Friend");
        this.friend = friend;
        this.name.setText(friend.getName());
        this.phone.setText(friend.getPhone());
        this.email.setText(friend.getEmail());
        this.website.setText(friend.getWebsite());
    }

    private void log(String message) {
        Log.d(TAG, message);
    }

    public void sendFriendEmail(View view) {
        if (this.friend.getEmail() != null) {
            log("Sending email to " + this.friend.getName());
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("plain/text");
            String[] receivers = {this.friend.getEmail()};
            emailIntent.putExtra(Intent.EXTRA_EMAIL, receivers);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
            emailIntent.putExtra(Intent.EXTRA_TEXT,
                    "Email Text");
            startActivity(emailIntent);
        } else {
            log("Cannot send email to friend: Email is not set");
        }
    }

    public void visitFriendWebsite(View view) {
        String website = this.friend.getWebsite();
        if (website != null) {
            log("Visiting the website of " + this.friend.getName());
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(website));
            startActivity(i);
        } else {
            log("Cannot visit website to friend: Website is not set");
        }
    }

    public void openWebsite(View view) {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(friend.getWebsite()));
        startActivity(i);
    }

    public void openMail(View view) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        String[] receivers = { friend.getEmail() };
        emailIntent.putExtra(Intent.EXTRA_EMAIL, receivers);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Test");
        startActivity(emailIntent);
    }

    public void openCall(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + friend.getPhone()));
        startActivity(intent);
    }

    public void openText(View view) {
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("sms:" + friend.getPhone()));
        startActivity(sendIntent);
    }
}
