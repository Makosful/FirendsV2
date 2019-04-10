package com.github.makosful.friendsv2.gui.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
        Log.d(TAG, "onCreate: Begin");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_add);

        Log.d(TAG, "onCreate: Creates a new MainModel class");
        
        this.model = new MainModel(this);
        Log.d(TAG, "onCreate: Finish");
    }

    /**
     * Callback method from FriendMeta.OnFragmentInteractionListener
     * Updates the local instance of Friend with the one created by Friend Meta Fragment
     * @param friend The new Friend object
     */
    @Override
    public void updateFriend(Friend friend) {
        Log.d(TAG, "updateFriend: Updates Friend from FriendMeta");
        this.friend = friend;
    }

    /**
     * Method should be called from View (XML)
     * This attempts to create a new Friend based on the input from the Friend Meta Fragment.
     * @param view The View that calls this method
     */
    public void addFriend(View view) {
        Log.d(TAG, "addFriend: Checks if FriendMeta has created a Friend");
        if (this.friend == null) {

            Toast.makeText(this, "No Friend have been created", Toast.LENGTH_SHORT).show();
            return;
        }

        if (this.model.createFriend(this.friend))
            notification("Friend (" + this.friend.getName() + ") added successfully");
        else
            notification("Failed to add friend");
    }

    /**
     * A small hack to reduce repetitive method calls.
     * Prints a small message to the bottom of the screen.
     * @param message The message to print
     */
    private void notification(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
