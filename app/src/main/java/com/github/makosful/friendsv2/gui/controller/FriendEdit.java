package com.github.makosful.friendsv2.gui.controller;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.github.makosful.friendsv2.Common;
import com.github.makosful.friendsv2.R;
import com.github.makosful.friendsv2.be.Friend;

public class FriendEdit extends AppCompatActivity
{

    private Friend friend;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_friend);

        friend = (Friend) getIntent().getExtras().get(Common.intentFriendEdit);
    }

    public void saveEdits(View view)
    {
    }

    public void cancel(View view)
    {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
}
