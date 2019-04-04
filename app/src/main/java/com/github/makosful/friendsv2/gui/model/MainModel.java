package com.github.makosful.friendsv2.gui.model;

import android.content.Context;

import com.github.makosful.friendsv2.be.Friend;
import com.github.makosful.friendsv2.dal.IStorage;
import com.github.makosful.friendsv2.dal.MemoryStorageFriends;
import com.github.makosful.friendsv2.dal.SQLiteFriends;

import java.util.List;

public class MainModel
{
    private IStorage<Friend> friendStorage;

    public MainModel(Context context)
    {
        friendStorage = new SQLiteFriends(context);
        // friendStorage = MemoryStorageFriends.getInstance();
    }

    public List<Friend> getFriendList()
    {
        return friendStorage.readAll();
    }

    public boolean save(Friend friend) {
        return friendStorage.update(friend);
    }

    public Friend getFriend(int id) {
        return friendStorage.readById(id);
    }
}
