package com.github.makosful.friendsv2.gui.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.makosful.friendsv2.Common;
import com.github.makosful.friendsv2.R;
import com.github.makosful.friendsv2.be.Friend;
import com.github.makosful.friendsv2.gui.controller.FriendDetail;

import java.io.IOException;
import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder>
{
    private static final String TAG = "Friend Adapter";

    private Context context;
    private List<Friend> friendList;

    public FriendAdapter(Context context, List<Friend> friendList)
    {
        log("Creating adapter");
        this.context = context;
        this.friendList = friendList;
    }

    private static void log(String message){
        Log.d(TAG, message);
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        log("Creating ViewHolder");

        log("Inflating using custom layout");
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.adapter_layout_friends, viewGroup, false);

        log("Created ViewHolder");
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder viewHolder, int position)
    {
        log("Binding ViewHolder in position: " + position);

        log("Reading friend from position " + position);
        final Friend friend = friendList.get(position);

        try {
            if (friend.getImageUrl() != null) {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), friend.getImageUrl());
                viewHolder.image.setImageBitmap(bitmap);
            }
        } catch (IOException e) {
            log(e.getMessage());
        }

        String name = friend.getName();
        if (name == null || name.isEmpty()) {
            log("Friend in position (" + position + ") appears to have no name");
            viewHolder.name.setText("");
        } else {
            log("Setting name");
            viewHolder.name.setText(name);
        }

        log("Setting the onClickListener for this item to open the friend detail view");
        viewHolder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDetailView(friend.getId());
            }
        });
    }

    private void openDetailView(int id) {
        Intent i = new Intent(context, FriendDetail.class);
        i.putExtra(Common.DATA_FRIEND_DETAIL, id);
        ((Activity)context).startActivityForResult(i, Common.ACTIVITY_REQUEST_CODE_FRIEND_DETAIL);
    }

    @Override
    public int getItemCount()
    {
        return friendList.size();
    }


    class FriendViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView image;
        private TextView name;
        private LinearLayout parent;

        FriendViewHolder(@NonNull View itemView)
        {
            super(itemView);

            image = itemView.findViewById(R.id.iv_friend_list_image);
            name = itemView.findViewById(R.id.tv_friend_list_name);

            parent = itemView.findViewById(R.id.parent_layout);
        }
    }
}
