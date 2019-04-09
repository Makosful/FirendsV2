package com.github.makosful.friendsv2.gui.controller;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.github.makosful.friendsv2.Common;
import com.github.makosful.friendsv2.R;
import com.github.makosful.friendsv2.be.Friend;

import java.util.Calendar;
import java.util.Date;

public class FriendMeta extends Fragment {
    private TextView txt_name;
    private TextView txt_phone;
    private TextView txt_email;
    private TextView tv_bday;
    private TextView txt_address;
    private TextView txt_website;

    private DatePickerDialog.OnDateSetListener dateSetListener;
    private OnFragmentInteractionListener mListener;

    private Date date;
    private Friend friend;

    /**
     * Required public constructor.
     * Leave empty and don't call.
     */
    public FriendMeta() { }

    public static FriendMeta newInstance() {
        FriendMeta friendMeta = new FriendMeta();
        return friendMeta;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.friend = (Friend)getArguments().get(Common.ARGUMENT_FRIEND);
        }
    }

    private void m() {
        if (this.friend != null && this.friend.getName() != null) txt_name.setText(this.friend.getName());
        if (this.friend != null && this.friend.getPhone() != null) txt_phone.setText(this.friend.getPhone());
        if (this.friend != null && this.friend.getEmail() != null) txt_email.setText(this.friend.getEmail());
        if (this.friend != null && this.friend.getBirthDate() != null) tv_bday.setText(this.friend.getBirthDate().toString());
        if (this.friend != null && this.friend.getAddress() != null) txt_address.setText(this.friend.getAddress());
        if (this.friend != null && this.friend.getWebsite() != null) txt_website.setText(this.friend.getWebsite());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_meta, container, false);

        txt_name = view.findViewById(R.id.txt_friend_meta_name);
        txt_name.addTextChangedListener(new MyTextWatcher());

        txt_phone = view.findViewById(R.id.txt_friend_meta_phone);
        txt_phone.addTextChangedListener(new MyTextWatcher());

        txt_email = view.findViewById(R.id.txt_friend_meta_email);
        txt_email.addTextChangedListener(new MyTextWatcher());

        tv_bday = view.findViewById(R.id.tv_friend_meta_bday);
        tv_bday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerDialog();
            }
        });
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                setDate(year, month, dayOfMonth);
            }
        };

        txt_address = view.findViewById(R.id.txt_friend_meta_address);
        txt_address.addTextChangedListener(new MyTextWatcher());

        txt_website = view.findViewById(R.id.txt_friend_meta_website);
        txt_website.addTextChangedListener(new MyTextWatcher());
        m();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private Friend assembleFriend() {
        Friend friend = new Friend();

        String name = txt_name.getText().toString();
        if (name.isEmpty())
            return null;
        else
            friend.setName(name);

        String phone = txt_phone.getText().toString();
        if (phone.isEmpty())
            friend.setPhone(null);
        else
            friend.setPhone(phone);

        String email = txt_email.getText().toString();
        if (email.isEmpty())
            friend.setEmail(null);
        else
            friend.setEmail(email);

        if (this.date == null)
            friend.setBirthDate(null);
        else
            friend.setBirthDate(this.date);

        String address = txt_address.getText().toString();
        if (address.isEmpty())
            friend.setAddress(null);
        else
            friend.setAddress(address);

        String website = txt_website.getText().toString();
        if (website.isEmpty())
            friend.setWebsite(null);
        else
            friend.setWebsite(website);

        friend.setImageUrl(null);

        return friend;
    }

    private void openDatePickerDialog() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog diag = new DatePickerDialog(getContext(), R.style.Theme_AppCompat_DayNight_Dialog_MinWidth, this.dateSetListener, year, month, day);
        // diag.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        diag.show();
    }

    private void setDate(int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, dayOfMonth, 0, 0, 0);
        date = c.getTime();
        tv_bday.setText(year + "/" + (month +1) + "/" + dayOfMonth);
    }

    public void setFriend(Friend friend) {
        this.friend = friend;
        m();
    }

    public interface OnFragmentInteractionListener {
        void updateFriend(Friend friend);
    }

    private class MyTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Do nothing
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // Do nothing
        }

        @Override
        public void afterTextChanged(Editable s) {
            Friend friend = assembleFriend();
            mListener.updateFriend(friend);
        }
    }
}
