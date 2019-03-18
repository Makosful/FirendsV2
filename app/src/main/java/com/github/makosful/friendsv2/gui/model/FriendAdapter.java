package com.github.makosful.friendsv2.gui.model;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.makosful.friendsv2.Common;
import com.github.makosful.friendsv2.R;
import com.github.makosful.friendsv2.be.Friend;
import com.github.makosful.friendsv2.gui.controller.FriendDetail;

import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder>
{
    private static final String TAG = "Friend Adapter";

    private List<Friend> friendList;
    private Context context;

    public FriendAdapter(List<Friend> friendList, Context context)
    {
        this.friendList = friendList;
        this.context = context;

        Log.d(TAG, "Adapter has been created");
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        Log.d(TAG, "Creating ViewHolder");
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.adapter_layout_friends, null);
        FriendViewHolder viewHolder = new FriendViewHolder(view);
        Log.d(TAG, "ViewHolder created");
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder viewHolder, int position)
    {
        Log.d(TAG, "Binding ViewHolder");

        final Friend friend = friendList.get(position);
        Log.d("friend", "Retrieved friend: " + friend.getName());

        Log.d(TAG, "Setting friend name");
        viewHolder.name.setText(friend.getName());

        Log.d(TAG, "Creating onclickListener for detail view");
        viewHolder.details.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                openDetailsWindow(friend);
            }
        });

        Log.d(TAG, "Setting onClickListener for SMS service");
        viewHolder.text.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                openTextService(friend);
            }
        });

        Log.d(TAG, "Setting onClickListener for phone service");
        viewHolder.call.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                openPhoneService(friend);
            }
        });

        Log.d(TAG, "Setting onClickListener for mail service");
        viewHolder.mail.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                openMailService(friend);
            }
        });
    }

    private void openDetailsWindow(Friend friend)
    {
        Log.d(TAG, "Opening detailed friend view");
        Intent intent = new Intent(context, FriendDetail.class);

        Log.d(TAG, "Adding friend to intent extra");
        intent.putExtra(Common.INTENT_FRIEND_DETAIL, friend);

        Log.d(TAG, "Starting new intent");
        context.startActivity(intent);
    }

    private void openTextService(Friend friend)
    {
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("sms:" + friend.getNumber()));
        sendIntent.putExtra("sms_body", "Hi, it goes well on the android course...");
        context.startActivity(sendIntent);
    }

    private void openPhoneService(Friend friend)
    {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + friend.getNumber()));
        context.startActivity(intent);
    }

    private void openMailService(Friend friend)
    {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        String[] receivers = { friend.getEmail() };
        emailIntent.putExtra(Intent.EXTRA_EMAIL, receivers);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Test");
        emailIntent.putExtra(Intent.EXTRA_TEXT,
                             "Hej, Hope that it is ok, Best Regards android...;-)");
        context.startActivity(emailIntent);
    }

    @Override
    public int getItemCount()
    {
        return friendList.size();
    }

    class FriendViewHolder extends RecyclerView.ViewHolder
    {
        private TextView name;
        private ImageButton details;
        private ImageButton text;
        private ImageButton call;
        private ImageButton mail;

        public FriendViewHolder(@NonNull View itemView)
        {
            super(itemView);

            name = itemView.findViewById(R.id.tv_name);
            details = itemView.findViewById(R.id.btn_detail);
            text = itemView.findViewById(R.id.btn_text);
            call = itemView.findViewById(R.id.btn_call);
            mail = itemView.findViewById(R.id.btn_mail);
        }
    }
}
