package com.github.makosful.friendsv2.gui.controller;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.github.makosful.friendsv2.R;
import com.github.makosful.friendsv2.be.Friend;
import com.github.makosful.friendsv2.gui.model.MainModel;
import com.github.makosful.friendsv2.gui.model.FriendAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity
{
        private MainModel model;
        FriendAdapter adapter;
        List<Friend> friendList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        model = new MainModel(this);
        friendList = model.getFriendList();

        adapter = new FriendAdapter(this, friendList);
        RecyclerView recyclerView = findViewById(R.id.rv_friendList);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        friendList = model.getFriendList();
        adapter.notifyDataSetChanged();
    }


}
