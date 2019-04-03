package com.github.makosful.friendsv2.gui.model;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.makosful.friendsv2.Common;
import com.github.makosful.friendsv2.R;
import com.github.makosful.friendsv2.be.Friend;
import com.github.makosful.friendsv2.gui.controller.FriendDetail;

import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder>
{
    private static final String TAG = "Friend Adapter";

    private Context context;
    private List<Friend> friendList;

    public FriendAdapter(Context context, List<Friend> friendList)
    {
        this.context = context;
        this.friendList = friendList;

        Log.d(TAG, "Adapter has been created");
    }



    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        Log.d(TAG, "Creating ViewHolder");
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.adapter_layout_friends, viewGroup, false);
        FriendViewHolder viewHolder = new FriendViewHolder(view);
        Log.d(TAG, "ViewHolder created");
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder viewHolder, int position)
    {
        Log.d(TAG, "Binding ViewHolder");

        final Friend friend = friendList.get(position);
        Log.d(TAG, "Retrieved friend: " + friend.getName());

        // Log.d(TAG, "Setting friend image");
        // viewHolder.image.setImageBitmap(friend.getPicture());

        Log.d(TAG, "Setting friend name");
        viewHolder.name.setText(friend.getName());

        viewHolder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDetailView(friend);
            }
        });
    }

    private void openDetailView(Friend friend) {
        Intent i = new Intent(context, FriendDetail.class);
        i.putExtra(Common.DATA_FRIEND_DETAIL, friend);
        context.startActivity(i);
        // Toast.makeText(context, "Showing Friend: " + friend.getName(), Toast.LENGTH_SHORT).show();
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

            // image = itemView.findViewById(R.id.iv_friend_list_image);
            name = itemView.findViewById(R.id.tv_friend_list_name);

            parent = itemView.findViewById(R.id.parent_layout);
        }
    }
}
