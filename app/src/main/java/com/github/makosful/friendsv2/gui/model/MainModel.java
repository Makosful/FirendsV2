package com.github.makosful.friendsv2.gui.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.github.makosful.friendsv2.be.Friend;
import com.github.makosful.friendsv2.dal.IStorage;
import com.github.makosful.friendsv2.dal.MemoryStorageFriends;
import com.github.makosful.friendsv2.dal.SQLiteFriends;

import java.io.IOException;
import java.util.List;

public class MainModel {

    private IStorage<Friend> friendStorage;

    public MainModel(Context context) {
        friendStorage = new SQLiteFriends(context);
        friendStorage.seed();
    }

    /**
     * Get a full list of all Friends
      * @return A list of Friends
     */
    public List<Friend> getFriendList() {
        return friendStorage.readAll();
    }

    /**
     * Update an existing Friend with a matching ID.
     * @param friend The Friend to update
     * @return Returns true if the update was successful, otherwise false
     */
    public boolean saveFriend(Friend friend) {
        return friendStorage.update(friend);
    }

    /**
     * Returns a single friend based on the ID
     * @param id The ID of the Friend to retrieve
     * @return Returns the friend requested, if they exist. Otherwise returns null
     */
    public Friend getFriend(int id) {
        return friendStorage.readById(id);
    }

    /**
     * Creates a new Friend in the database
     * @param friend The friend to create
     * @return Returns true if the created was successful, otherwise false
     */
    public boolean createFriend(Friend friend) {
        return friendStorage.create(friend);
    }

    /**
     * Deletes a Friend from the database
     * @param friend The friend to delete
     * @return Returns true if the Friend was successfully deleted, otherwise false
     */
    public boolean deleteFriend(Friend friend) {
        return friendStorage.deleteById(friend.getId());
    }
}
