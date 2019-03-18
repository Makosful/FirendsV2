package com.github.makosful.friendsv2.gui.controller;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.github.makosful.friendsv2.Common;
import com.github.makosful.friendsv2.R;
import com.github.makosful.friendsv2.be.Friend;

public class FriendEdit extends AppCompatActivity
{

    private Friend friend;

    private EditText txtName;
    private EditText txtPhone;
    private EditText txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_friend);

        friend = (Friend) getIntent().getExtras().get(Common.INTENT_FRIEND_EDIT);

        txtName = findViewById(R.id.txt_friend_edit_name);
        txtPhone = findViewById(R.id.txt_friend_edit_phone);
        txtEmail = findViewById(R.id.txt_friend_edit_email);
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
        friend.setName(txtName.getText().toString());
        friend.setNumber(txtPhone.getText().toString());
        friend.setEmail(txtEmail.getText().toString());

        Intent returnIntent = new Intent();
        returnIntent.putExtra(Common.INTENT_FRIEND_EDIT_RESULT, friend);
        setResult(Activity.RESULT_OK, returnIntent);
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
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
}
