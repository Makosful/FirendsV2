package com.github.makosful.friendsv2.gui.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.github.makosful.friendsv2.Common;
import com.github.makosful.friendsv2.R;
import com.github.makosful.friendsv2.be.Friend;

public class FriendDetail extends AppCompatActivity
{
    private static final String TAG = "FriendDetail";
    private Friend friend;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_detail);

        friend = (Friend) getIntent().getSerializableExtra(Common.intentFriendDetail);
        final TextView name = findViewById(R.id.tv_name);
        name.setText(friend.getName());
        final TextView number = findViewById(R.id.txt_number);
        final String numberText = friend.getNumber() + "";
        number.setText(numberText);
        final TextView email = findViewById(R.id.txt_number);
        email.setText(friend.getEmail());
    }

    public void editFriend(View view)
    {
        Intent i = new Intent(this, FriendEdit.class);
        i.putExtra(Common.intentFriendEdit, friend);
        startActivityForResult(i, Common.FRIEND_EDIT_REQUEST_CODE);
    }
}
