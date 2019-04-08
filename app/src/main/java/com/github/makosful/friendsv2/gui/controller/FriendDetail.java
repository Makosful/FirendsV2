package com.github.makosful.friendsv2.gui.controller;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.makosful.friendsv2.BuildConfig;
import com.github.makosful.friendsv2.Common;
import com.github.makosful.friendsv2.R;
import com.github.makosful.friendsv2.be.Friend;
import com.github.makosful.friendsv2.gui.model.MainModel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class FriendDetail extends AppCompatActivity {
    private static final String TAG = "FriendDetail";

    private MainModel model;

    private Friend friend;
    private Uri path;

    private ImageView image;
    private TextView tv_uri;
    private TextView name;
    private TextView phone;
    private TextView email;
    private TextView website;

    private static void log(String message) {
        Log.d(TAG, message);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        log("Creating Friend Detail");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_detail);

        log("Getting an instance of the main model");
        this.model = new MainModel(this);

        log("Finds and sets all the reused views");
        this.name = findViewById(R.id.tv_friend_list_name);
        this.phone = findViewById(R.id.tv_friend_detail_phone);
        this.email = findViewById(R.id.tv_friend_detail_email);
        this.website = findViewById(R.id.tv_friend_detail_website);
        this.image = findViewById(R.id.iv_friend_detail_image);
        this.tv_uri = findViewById(R.id.tv_friend_detail_uri);

        log("Reading the Friend ID from the Extras");
        int id = (int) Objects.requireNonNull(getIntent().getExtras()).get(Common.DATA_FRIEND_DETAIL);
        log("Fetching the friend from the main model");
        friend = model.getFriend(id);

        // Populate all the fields
        updateAllFields();

        log("Finished creating Friend Detail");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        log("Parsing result code");

        if (resultCode == Activity.RESULT_CANCELED)
            return;

        switch (requestCode) {
            case Common.ACTIVITY_REQUEST_CODE_CAMERA:
                assert data != null;
                handleCameraResult(data);
                break;
            case Common.ACTIVITY_REQUEST_CODE_FRIEND_EDIT:
                assert data != null;
                handleEditResult(data);
                break;
            default:
                Toast.makeText(this, "Unknown result code.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void showFriendOnMap(View view) {
        log("Creating Map Activity");
        Intent i = new Intent(this, MapsActivity.class);
        i.putExtra(Common.INTENT_MAP_ACTIVITY, friend);

        log("Starting Map activity");
        startActivity(i);
    }


    /**
     * Launches the default browser and loads the friend's website
     * @param view unused
     */
    public void openWebsite(View view) {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(friend.getWebsite()));
        startActivity(i);
    }

    /**
     * Launches the default Mail application
     * @param view Unused
     */
    public void openMail(View view) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        String[] receivers = { friend.getEmail() };
        emailIntent.putExtra(Intent.EXTRA_EMAIL, receivers);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Test");
        startActivity(emailIntent);
    }

    /**
     * Launches the default phone application
     * @param view Unused
     */
    public void openCall(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + friend.getPhone()));
        startActivity(intent);
    }

    /**
     * Launches the default SMS application
     * @param view Unused
     */
    public void openText(View view) {
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("sms:" + friend.getPhone()));
        startActivity(sendIntent);
    }


    /**
     * Launches the Edit Friend activity
     * @param view Unused
     */
    public void openFriendEditActivity(View view) {
        log("Preparing to edit Friend");

        Intent i = new Intent(this, FriendEdit.class);
        i.putExtra(Common.INTENT_FRIEND_EDIT, friend);

        log("Starting Friend Edit activity");

        startActivityForResult(i, Common.ACTIVITY_REQUEST_CODE_FRIEND_EDIT);
    }

    /**
     * Saves the edit of the Friend to the storage
     * @param data The intent returned from the EditFriend activity
     */
    private void handleEditResult(Intent data) {
        log("Returning from Friend Edit");
        assert data != null;
        Friend friend = (Friend) Objects.requireNonNull(data.getExtras()).get(Common.INTENT_FRIEND_EDIT_RESULT);
        assert friend != null;
        log("Results came back as OK");

        if (model.saveFriend(friend)){
            this.friend = friend;
            updateAllFields();
        } else {
            Toast.makeText(this, "Failed to saveFriend the changes", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Launches the default camera app
     * @param view Unused
     */
    public void openCameraActivity(View view) {
        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
            requestPermissions( new String[]{Manifest.permission.CAMERA},
                    Common.PERMISSION_REQUEST_CODE_CAMERA );

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            log("Permissions have been denied.");
            return;
        }

        try {
            path = FileProvider.getUriForFile(
                    FriendDetail.this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    getNewFilePath());
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            i.putExtra(MediaStore.EXTRA_OUTPUT, path);
            if (i.resolveActivity(getPackageManager()) == null) return;

            startActivityForResult(i, Common.ACTIVITY_REQUEST_CODE_CAMERA);

        } catch (IOException e) {
            log(e.getMessage());
        }
    }

    /**
     * Saves the image taken to the friend.
     * @param data The Intent returned from the camera app.
     */
    private void handleCameraResult(Intent data) {
        log("Handling image from camera");
        friend.setImageUrl(this.path);
        if (model.saveFriend(friend)) {
            log("Friend saved");
            updatePicture();
        } else {
            log("Could not save image");
        }
    }


    /**
     * Updates all fields in the activity with the information from the current instance of Friend
     */
    private void updateAllFields() {
        log("Updating all fields for this friend");

        updateName();

        updatePhone();

        updateEmail();

        updateWebsite();

        updatePicture();

        log("Finished updating all fields");
    }

    /**
     * Update the name view based on the current instance of the Friend
     */
    private void updateName(){
        log("Updating the name");
        name.setText(friend.getName());
        // No checks since name is mandatory
    }

    /**
     * Update the Views related to the phone number based on the instance of Friend
     */
    private void updatePhone() {
        log("Updating phone number");
        String phone = friend.getPhone();
        if (phone == null || phone.isEmpty()) {
            log("No phone number was saved");

            log("Disabling SMS button");
            findViewById(R.id.ib_friend_detail_sms).setEnabled(false);
            log("Disabling Call button");
            findViewById(R.id.ib_friend_detail_call).setEnabled(false);
        } else {
            log("Setting phone number");
            this.phone.setText(phone);

            log("Enabling SMS button");
            findViewById(R.id.ib_friend_detail_sms).setEnabled(true);
            log("Enabling Call button");
            findViewById(R.id.ib_friend_detail_call).setEnabled(true);
        }
    }

    /**
     * Updates the Views related to email, based on the instance of Friend
     */
    private void updateEmail() {
        log("Updating email");
        String email = friend.getEmail();
        if (email == null || email.isEmpty()) {
            log("No email was saved");
            log("Disabling email button");
            findViewById(R.id.ib_friend_detail_mail).setEnabled(false);
        } else {
            log("Setting email");
            this.email.setText(email);

            log("Enabling email button");
            findViewById(R.id.ib_friend_detail_mail).setEnabled(true);
        }
    }

    /**
     * Updates the Views related to website , based on the instance of Friend
     */
    private void updateWebsite() {
        log("Updating website");
        String website = friend.getWebsite();
        if (website == null || website.isEmpty()) {
            log("No website was saved");

            log("Disabling website button");
            findViewById(R.id.ib_friend_detail_website).setEnabled(false);
        } else {
            log("Setting website");
            this.website.setText(website);

            log("Disabling website button");
            findViewById(R.id.ib_friend_detail_website).setEnabled(true);
        }
    }

    /**
     * Updates the ImageView holding the Friend's picture, based on the current instance of Friend
     */
    private void updatePicture() {
        log("Updating picture");
        Uri uri = friend.getImageUrl();
        if (uri == null) return;

        this.tv_uri.setText(uri.toString());
        this.image.setImageURI(uri);
    }


    private File getNewFilePath() throws IOException {
        log("Gets the file path");

        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Common.APPLICATION_NAME);
        log("Picture directory: " + dir.getPath());

        log("Checking if image directory exists");
        if (!dir.exists()) {
            log("Image directory doesn't exist. Attempts to create directory");
            if (!dir.mkdir()) {
                log("Failed to create directory");
                throw new IOException("Failed to create output directory");
            } else {
                log("Directory (" + dir + ") created.");
            }
        } else {
            log("Picture directory exists");
        }

        log("Creating filename from timestamp");
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String postfix = "jpg";
        String prefix = "IMG";
        String filename = prefix + "_" + timeStamp + "." + postfix;
        log("Filename generated: " + filename);

        return new File(dir.getPath() + File.separator + filename);
    }
}
