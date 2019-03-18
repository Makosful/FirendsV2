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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_detail);

        friend = (Friend) getIntent().getSerializableExtra(Common.INTENT_FRIEND_DETAIL);
        name = findViewById(R.id.tv_name);
        name.setText(friend.getName());
        phone = findViewById(R.id.txt_number);
        final String numberText = friend.getNumber() + "";
        phone.setText(numberText);
        email = findViewById(R.id.txt_number);
        email.setText(friend.getEmail());
    }

    public void editFriend(View view)
    {
        Intent i = new Intent(this, FriendEdit.class);
        i.putExtra(Common.INTENT_FRIEND_EDIT, friend);
        startActivityForResult(i, Common.FRIEND_EDIT_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        Friend friend = (Friend)data.getExtras().get(Common.INTENT_FRIEND_EDIT_RESULT);
        switch (resultCode)
        {
            case Activity.RESULT_OK: saveResult(friend); break;
            default: Toast.makeText(this, "", Toast.LENGTH_SHORT).show(); break;
        }
    }

    private void saveResult(Friend friend)
    {
        log("Logging changes to Friend");
        this.friend = friend;
        this.name.setText(friend.getName());
        this.phone.setText(friend.getNumber());
        this.email.setText(friend.getEmail());
    }

    private void log(String message)
    {
        Log.d(TAG, message);
    }
}
