package com.github.makosful.friendsv2.gui.model;

import com.github.makosful.friendsv2.be.Friend;

import java.util.ArrayList;
import java.util.List;

public class MainModel
{
    private List<Friend> friendList;

    public MainModel()
    {
        friendList = new ArrayList<>();
        Friend friend = new Friend("Bob Ross");
        friend.setNumber("12983476");
        friend.setEmail("bob@ross.com");
        friendList.add(friend);
    }

    public List<Friend> getFriendList()
    {
        return friendList;
    }
}
