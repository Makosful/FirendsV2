package com.github.makosful.friendsv2.gui.controller;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.github.makosful.friendsv2.R;
import com.github.makosful.friendsv2.be.Friend;
import com.github.makosful.friendsv2.gui.model.MainModel;
import com.github.makosful.friendsv2.gui.model.FriendAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity
{

    private MainModel model;

    private RecyclerView recyclerView;
    private FriendAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        model = new MainModel();
        List<Friend> friendList = model.getFriendList();

        adapter = new FriendAdapter(friendList, this);
        recyclerView = findViewById(R.id.rv_friendList);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
