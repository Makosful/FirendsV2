package com.github.makosful.friendsv2.gui.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.makosful.friendsv2.Common;
import com.github.makosful.friendsv2.R;
import com.github.makosful.friendsv2.be.Friend;

public class FriendDetail extends AppCompatActivity
{

    private TextView name;
    private TextView number;
    private TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_detail);

        name = findViewById(R.id.tv_name);
        number = findViewById(R.id.tv_number);
        email = findViewById(R.id.tv_email);

        Friend friend = (Friend) getIntent().getSerializableExtra(Common.intentFriendDetail);

        name.setText(friend.getName());
        final String numberText = friend.getNumber() + "";
        number.setText(numberText);
        email.setText(friend.getEmail());
    }

}
