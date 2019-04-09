package com.github.makosful.friendsv2.gui.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.github.makosful.friendsv2.R;
import com.github.makosful.friendsv2.be.Friend;
import com.github.makosful.friendsv2.gui.model.MainModel;

public class FriendAdd extends AppCompatActivity implements FriendMeta.OnFragmentInteractionListener{
    private static final String TAG = "FriendAdd";

    private MainModel model;
    private Friend friend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_add);

        this.model = new MainModel(this);
    }

    @Override
    public void updateFriend(Friend friend) {
        this.friend = friend;
    }

    public void addFriend(View view) {
        // this.model.createFriend(this.friend);
        if (this.friend == null) {
            Toast.makeText(this, "No Friend have been created", Toast.LENGTH_SHORT).show();
            return;
        }

        if (this.model.createFriend(this.friend))
            notification("Friend (" + this.friend.getName() + ") added successfully");
        else
            notification("Failed to add friend");
    }

    private void notification(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
