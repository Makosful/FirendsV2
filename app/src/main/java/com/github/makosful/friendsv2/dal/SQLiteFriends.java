package com.github.makosful.friendsv2.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.github.makosful.friendsv2.Common;
import com.github.makosful.friendsv2.be.Friend;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SQLiteFriends implements IStorage<Friend>
{
    private static final String TAG = "SQLiteDatabase";

    private static final String TABLE_NAME = "friends";
    private static final String FIELD_ID = "ID";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_ADDRESS = "address";
    private static final String FIELD_LATITUDE = "latitude";
    private static final String FIELD_LONGITUDE = "longitude";
    private static final String FIELD_PHONE = "phone";
    private static final String FIELD_EMAIL = "email";
    private static final String FIELD_WEBSITE = "website";
    private static final String FIELD_BIRTHDAY = "birthday";
    private static final String FIELD_PICTURE = "picture";

    private static String INSERT = "INSERT INTO " + TABLE_NAME +
                                   "(" +
                                   FIELD_NAME + ", " +
                                   FIELD_ADDRESS + ", " +
                                   FIELD_LATITUDE + ", " +
                                   FIELD_LONGITUDE + ", " +
                                   FIELD_PHONE + ", " +
                                   FIELD_EMAIL + ", " +
                                   FIELD_WEBSITE + ", " +
                                   FIELD_BIRTHDAY + ", " +
                                   FIELD_PICTURE +
                                   ") " +
                                   "values (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private SQLiteDatabase database;
    private SQLiteStatement insertStatement;

    public SQLiteFriends(Context context)
    {
        OpenHelper openHelper = new OpenHelper(context);
        this.database = openHelper.getWritableDatabase();
        this.insertStatement = this.database.compileStatement(INSERT);
    }

    private static void log(String message)
    {
        Log.d(TAG, message);
    }

    @Override
    public boolean create(Friend friend)
    {
        log("Attempting to store Friend in database");

        try
        {
            byte[] bytes = new byte[0];

            if (friend.getPicture() != null)
            {
                log("Converts bitmap to byte array");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                friend.getPicture().compress(Bitmap.CompressFormat.PNG, 100, stream);
                bytes = stream.toByteArray();
                log("Conversion successfully");

                log("Attempts to close conversion stream");
                stream.close();
                log("stream closed");
            }

            int i = 0;

            this.insertStatement.bindString(++i, friend.getName()); // 1

            String address = friend.getAddress();
            if (address == null)
                this.insertStatement.bindNull(++i); // 2
            else
                this.insertStatement.bindString(++i, address); // 2

            this.insertStatement.bindDouble(++i, friend.getLatitude()); // 3
            this.insertStatement.bindDouble(++i, friend.getLongitude()); // 4

            String phone = friend.getPhone();
            if (phone == null)
                this.insertStatement.bindNull(++i); // 5
            else
                this.insertStatement.bindString(++i, phone); // 5

            String email = friend.getEmail();
            if (email == null)
                this.insertStatement.bindNull(++i); // 6
            else
                this.insertStatement.bindString(++i, email); // 6

            String website = friend.getWebsite();
            if (website == null)
                this.insertStatement.bindNull(++i); // 7
            else
                this.insertStatement.bindString(++i, website); // 7

            Date birthDate = friend.getBirthDate();
            if (birthDate == null)
                this.insertStatement.bindNull(++i); // 8
            else
                this.insertStatement.bindLong(++i, birthDate.getTime()); // 8

            this.insertStatement.bindBlob(++i, bytes); // 9

            log("Attempts to execute INSERT statement for Friend");
            long result = this.insertStatement.executeInsert();

            return result >= 0;
        }
        catch (IOException e)
        {
            log("Stream failed to close. IOException");
            log("Exception: " + e.getMessage());
            return false;
        }
        finally
        {
            log("Recycles the bitmap");
            if (friend.getPicture() != null)
            {
                friend.getPicture().recycle();
            }
        }
    }

    @Override
    public Friend readById(int id)
    {
        log("Attempts to read Friend with ID " + id);
        Cursor cursor = this.database.query(TABLE_NAME,
                                           new String[] {FIELD_ID, FIELD_NAME, FIELD_ADDRESS,
                                                         FIELD_LATITUDE, FIELD_LONGITUDE,
                                                         FIELD_PHONE, FIELD_EMAIL, FIELD_WEBSITE,
                                                         FIELD_BIRTHDAY, FIELD_PICTURE}, "id = ?",
                                           new String[] {"" + id}, null, null, FIELD_NAME);

        log("Query sent. Result received");

        if (cursor.moveToFirst())
        {
            log("Assembles Friend from the database");
            int i = -1;
            Friend friend = new Friend();
            friend.setId(cursor.getInt(++i));
            friend.setName(cursor.getString(++i));
            friend.setAddress(cursor.getString(++i));
            friend.setLatitude(cursor.getLong(++i));
            friend.setLongitude(cursor.getLong(++i));
            friend.setPhone(cursor.getString(++i));
            friend.setEmail(cursor.getString(++i));
            friend.setWebsite(cursor.getString(++i));
            friend.setBirthDate(new Date(cursor.getLong(++i)));

            log("Attempts to convert the byte array into a bitmap");
            byte[] blob = cursor.getBlob(++i);
            Bitmap bitmap = BitmapFactory
                    .decodeByteArray(blob, 0, blob.length, new BitmapFactory.Options());
            friend.setPicture(bitmap);

            cursor.close();
            return friend;
        }

        log("No friend with the ID " + id + " was found");
        cursor.close();
        return null;
    }

    @Override
    public List<Friend> readAll()
    {
        log("Attempts to read all Friends from the database");

        List<Friend> friends = new ArrayList<>();

        Cursor cursor = this.database.query(TABLE_NAME,
                                          new String[] {FIELD_ID, FIELD_NAME, FIELD_ADDRESS,
                                                        FIELD_LATITUDE, FIELD_LONGITUDE,
                                                        FIELD_PHONE, FIELD_EMAIL, FIELD_WEBSITE,
                                                        FIELD_BIRTHDAY, FIELD_PICTURE}, null, null,
                                          null, null, FIELD_NAME);

        log("Query sent. Results received");

        if (cursor.moveToFirst())
        {
            log("Starts looping through all friends from the database");
            do
            {
                int i = -1;
                Friend friend = new Friend();
                log("Gets the Friend's ID");
                friend.setId(cursor.getInt(++i));

                log("Gets the name of Friend(" + friend.getId() + ")");
                friend.setName(cursor.getString(++i));

                log("Gets the address of Friend(" + friend.getId() + ")");
                friend.setAddress(cursor.getString(++i));

                log("Gets the location of Friend(" + friend.getId() + ")");
                friend.setLatitude(cursor.getLong(++i));
                friend.setLongitude(cursor.getLong(++i));

                log("Gets the phone number of Friend(" + friend.getId() + ")");
                friend.setPhone(cursor.getString(++i));

                log("Gets the email of Friend(" + friend.getId() + ")");
                friend.setEmail(cursor.getString(++i));

                log("Gets the website of Friend(" + friend.getId() + ")");
                friend.setWebsite(cursor.getString(++i));

                log("Gets the birthday of Friend(" + friend.getId() + ")");
                friend.setBirthDate(new Date(cursor.getLong(++i)));

                log("Attempts to convert friend(" + friend.getId() + ")'s byte array into a bitmap");
                byte[] blob = cursor.getBlob(++i);
                Bitmap bitmap = BitmapFactory
                        .decodeByteArray(blob, 0, blob.length, new BitmapFactory.Options());
                friend.setPicture(bitmap);

                log("Add friend(" + friend.getId() + ") to the list of friends");
                friends.add(friend);
            }
            while (cursor.moveToNext());
            log("Finished looping through all the friends from the database");
        }

        if (!cursor.isClosed())
            cursor.close();

        return friends;
    }

    @Override
    public boolean update(Friend item)
    {
        log("Attempts to update Friend " + item.getName());
        try
        {
            log("Attempts to convert bitmap into byte array");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            item.getPicture().compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bytes = stream.toByteArray();
            log("Conversion successful");

            log("Attempts to close the stream");
            stream.close();
            log("Stream closed successfully");

            ContentValues cv = new ContentValues();
            cv.put(FIELD_NAME, item.getName());
            cv.put(FIELD_NAME, item.getAddress());
            cv.put(FIELD_NAME, item.getLatitude());
            cv.put(FIELD_NAME, item.getLongitude());
            cv.put(FIELD_NAME, item.getPhone());
            cv.put(FIELD_NAME, item.getEmail());
            cv.put(FIELD_NAME, item.getWebsite());
            cv.put(FIELD_NAME, item.getBirthDate().getTime());
            cv.put(FIELD_NAME, bytes);

            log("Fires off the UPDATE statement");
            int result = this.database
                    .update(TABLE_NAME, cv, "id = ?", new String[] {"" + item.getId()});

            return result >= 1;
        }
        catch (IOException e)
        {
            log("Stream failed to close. IOException");
            log("Exception: " + e.getMessage());
            return false;
        }
        finally
        {
            log("Recycles the bitmap");
            item.getPicture().recycle();
        }
    }

    @Override
    public boolean deleteAll()
    {
        log("Attempts to clear the table of Friends");
        int result = this.database.delete(TABLE_NAME, null, null);

        return result >= 1;
    }

    @Override
    public boolean deleteById(int id)
    {
        log("Attempts to delete the friend with ID " + id);
        int result = this.database.delete(TABLE_NAME, "id = ?", new String[] {"" + id});

        return result >= 1;
    }

    /**
     * Seeds the SQLite DB with a template
     */
    @Override
    public void seed()
    {
        dropTable(this.database);
        createTable(this.database);

        Friend friend = new Friend();
        friend.setName("Bob Ross");
        friend.setPhone("51239875");
        friend.setEmail("bob@ross.paint");
        friend.setWebsite("https://www.bobross.com/");

        create(friend);

        friend = new Friend();
        friend.setName("Tobi Danforth");
        friend.setAddress("14 Donald Trail");
        friend.setLatitude(35.23194);
        friend.setLongitude(40.32227);
        friend.setPhone("74494110");
        friend.setEmail("tdanforth0@dion.ne.jp");
        friend.setWebsite("http://opera.com/pellentesque/ultrices.html");
        create(friend);

        friend = new Friend();
        friend.setName("Kelly Reijmers");
        friend.setAddress("838 4th Hill");
        friend.setLatitude(-0.5833333);
        friend.setLongitude(73.2333333);
        friend.setPhone("14812627");
        friend.setEmail("kreijmers1@mayoclinic.com");
        friend.setWebsite("https://census.gov/vestibulum/aliquet/ultrices/erat.aspx");
        create(friend);

        friend = new Friend();
        friend.setName("Ruby Hakes");
        friend.setAddress("44027 Chive Hill");
        friend.setLatitude(13.3670968);
        friend.setLongitude(103.8448134);
        friend.setPhone("73742772");
        friend.setEmail("rhakes2@adobe.com");
        friend.setWebsite("https://yolasite.com/quisque.aspx");
        create(friend);

        friend = new Friend();
        friend.setName("Marika Sasser");
        friend.setAddress("76 Walton Pass");
        friend.setLatitude(38.8466225);
        friend.setLongitude(139.8744722);
        friend.setPhone("45148295");
        friend.setEmail("msasser3@sphinn.com");
        friend.setWebsite("https://nature.com/nulla/neque.png");
        create(friend);

        friend = new Friend();
        friend.setName("Cybil Peealess");
        friend.setAddress("0 Sunbrook Place");
        friend.setLatitude(40.0912332);
        friend.setLongitude(44.4037713);
        friend.setPhone("70365509");
        friend.setEmail("cpeealess4@google.co.jp");
        friend.setWebsite("https://dailymail.co.uk/in/sagittis/dui/vel/nisl/duis/ac.png");
        create(friend);

        friend = new Friend();
        friend.setName("Carlye McSherry");
        friend.setAddress("516 Debra Place");
        friend.setLatitude(36.962751);
        friend.setLongitude(100.901228);
        friend.setPhone("69580095");
        friend.setEmail("cmcsherry5@paginegialle.it");
        friend.setWebsite("https://samsung.com/sapien/dignissim.json");
        create(friend);

        friend = new Friend();
        friend.setName("Lib McKeating");
        friend.setAddress("5 Mockingbird Point");
        friend.setLatitude(9.939624);
        friend.setLongitude(126.065213);
        friend.setPhone("57057137");
        friend.setEmail("lmckeating6@amazon.de");
        friend.setWebsite("https://woothemes.com/erat/vestibulum/sed.jpg");
        create(friend);

        friend = new Friend();
        friend.setName("Billy Berrisford");
        friend.setAddress("9 Fallview Plaza");
        friend.setLatitude(59.330173);
        friend.setLongitude(18.0551861);
        friend.setPhone("47435961");
        friend.setEmail("bberrisford7@cdc.gov");
        friend.setWebsite("http://europa.eu/eleifend/donec/ut/dolor/morbi/vel.xml");
        create(friend);

        friend = new Friend();
        friend.setName("Adrian Artrick");
        friend.setAddress("9279 Main Trail");
        friend.setLatitude(9.338241);
        friend.setLongitude(-66.2575425);
        friend.setPhone("50258293");
        friend.setEmail("aartrick8@smh.com.au");
        friend.setWebsite("https://macromedia.com/ut/volutpat.jsp");
        create(friend);

        friend = new Friend();
        friend.setName("Elinore Braidwood");
        friend.setAddress("19 Cody Lane");
        friend.setLatitude(-38.9898711);
        friend.setLongitude(175.8087485);
        friend.setPhone("77620275");
        friend.setEmail("ebraidwood9@washington.edu");
        friend.setWebsite("http://accuweather.com/cursus/vestibulum/proin.jsp");
        create(friend);
    }

    private void createTable(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                               FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                               FIELD_NAME + " TEXT, " +
                               FIELD_ADDRESS + " TEXT, " +
                               FIELD_LATITUDE + " NUM, " +
                               FIELD_LONGITUDE + " NUM, " +
                               FIELD_PHONE + " TEXT, " +
                               FIELD_EMAIL + " TEXT, " +
                               FIELD_WEBSITE + " TEXT, " +
                               FIELD_BIRTHDAY + " INTEGER, " +
                               FIELD_PICTURE + " BLOB" +
                               ")");
    }

    private void dropTable(SQLiteDatabase db)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
    }

    private class OpenHelper extends SQLiteOpenHelper
    {

        OpenHelper(Context context)
        {
            super(context, Common.DATABASE_NAME, null, Common.DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase)
        {
            createTable(sqLiteDatabase);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
        {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        }
    }
}