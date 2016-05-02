package org.prikic.yafr.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import org.prikic.yafr.db.RssFeedsContract;
import org.prikic.yafr.db.RssFeedsDbHelper;
import org.prikic.yafr.model.RssChannel;
import org.prikic.yafr.util.DBUtil;

import java.util.LinkedList;
import java.util.List;

import timber.log.Timber;

public class RssChannelDAO {

    //Database fields
    private SQLiteDatabase database;
    private RssFeedsDbHelper rssFeedsDbHelper;
    private String[] allColumns = {RssFeedsContract.ChannelEntry._ID,
    RssFeedsContract.ChannelEntry.COLUMN_NAME, RssFeedsContract.ChannelEntry.COLUMN_URL,
    RssFeedsContract.ChannelEntry.COLUMN_IS_CHANNEL_ACTIVE};

    public RssChannelDAO(Context context) {
        rssFeedsDbHelper = new RssFeedsDbHelper(context);
    }

    public void open() throws SQLException {
        database = rssFeedsDbHelper.getWritableDatabase();
    }

    public void close() {
        rssFeedsDbHelper.close();
    }

    public long saveRssChannel(RssChannel rssChannel) {
        //open db connection - write mode
        open();

        //Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(RssFeedsContract.ChannelEntry.COLUMN_NAME, rssChannel.getName());
        values.put(RssFeedsContract.ChannelEntry.COLUMN_URL, rssChannel.getUrl());
        //default for activeRssChannel is true
        int activeFlag = DBUtil.convertBooleanToInt(rssChannel.isChannelActive());
        values.put(RssFeedsContract.ChannelEntry.COLUMN_IS_CHANNEL_ACTIVE, activeFlag);

        //Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = database.insert(RssFeedsContract.ChannelEntry.TABLE_NAME, null, values);

        //TODO should not be closed??
        //https://groups.google.com/forum/#!msg/android-developers/nopkaw4UZ9U/cPfPL3uW7nQJ
        //close();

        return newRowId;
    }

    public List<RssChannel> getRssChannels() {

        //TODO test
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<RssChannel> rssChannels = new LinkedList<>();

        SQLiteDatabase db = rssFeedsDbHelper.getReadableDatabase();

// How you want the results sorted in the resulting Cursor
        String sortOrder =
                RssFeedsContract.ChannelEntry.COLUMN_NAME + " ASC";

        Cursor c = db.query(
                RssFeedsContract.ChannelEntry.TABLE_NAME,  // The table to query
                allColumns,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        try {
            while (c.moveToNext()) {
                long id = c.getLong(c.getColumnIndexOrThrow(RssFeedsContract.ChannelEntry._ID));
                String name = c.getString(c.getColumnIndexOrThrow(RssFeedsContract.ChannelEntry.COLUMN_NAME));
                String url = c.getString(c.getColumnIndexOrThrow(RssFeedsContract.ChannelEntry.COLUMN_URL));
                boolean active = c.getInt(c.getColumnIndexOrThrow(RssFeedsContract.ChannelEntry.COLUMN_IS_CHANNEL_ACTIVE))==1;
                RssChannel rssChannel = new RssChannel(id, name, url, active);
                rssChannels.add(rssChannel);
            }
        }
        finally {
            c.close();
        }
        return rssChannels;
    }

    public void updateRssChannel(RssChannel rssChannel) {

        open();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(RssFeedsContract.ChannelEntry.COLUMN_NAME, rssChannel.getName());
        values.put(RssFeedsContract.ChannelEntry.COLUMN_URL, rssChannel.getUrl());

        // Which row to update, based on the ID
        String selection = RssFeedsContract.ChannelEntry._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(rssChannel.getId()) };

        database.update(RssFeedsContract.ChannelEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    public void updateRssChannelActiveFlag(RssChannel rssChannel) {

        open();

        // New value for one column
        ContentValues values = new ContentValues();
        int isChannelActive = DBUtil.convertBooleanToInt(rssChannel.isChannelActive());
        Timber.d("%d", isChannelActive);
        values.put(RssFeedsContract.ChannelEntry.COLUMN_IS_CHANNEL_ACTIVE, isChannelActive);

        // Which row to update, based on the ID
        String selection = RssFeedsContract.ChannelEntry._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(rssChannel.getId()) };

        database.update(RssFeedsContract.ChannelEntry.TABLE_NAME, values, selection, selectionArgs);
    }
}
