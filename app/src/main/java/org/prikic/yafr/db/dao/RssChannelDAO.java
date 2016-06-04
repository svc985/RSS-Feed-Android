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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import timber.log.Timber;

@SuppressWarnings("TryFinallyCanBeTryWithResources")
public class RssChannelDAO {

    //Database fields
    private SQLiteDatabase database;
    private RssFeedsDbHelper rssFeedsDbHelper;
    private String[] allColumns = {RssFeedsContract.ChannelEntry._ID,
    RssFeedsContract.ChannelEntry.COLUMN_NAME, RssFeedsContract.ChannelEntry.COLUMN_URL,
    RssFeedsContract.ChannelEntry.COLUMN_IS_CHANNEL_ACTIVE};

    private static RssChannelDAO instance;

    @SuppressWarnings("unused")
    private RssChannelDAO () {
    }

    private RssChannelDAO(Context context) {
        rssFeedsDbHelper = new RssFeedsDbHelper(context);
    }

    public static RssChannelDAO getInstance(Context context) {
        if (instance == null)
            instance = new RssChannelDAO(context);
        return instance;
    }

    public void open() throws SQLException {
        database = rssFeedsDbHelper.getWritableDatabase();
    }

    private void close() {
        database.close();
    }

    public long saveRssChannel(RssChannel rssChannel) {
        //open db connection - write mode
        open();

        //Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(RssFeedsContract.ChannelEntry.COLUMN_NAME, rssChannel.getName());
        values.put(RssFeedsContract.ChannelEntry.COLUMN_URL, rssChannel.getUrl());
        //default for activeRssChannel is true
        //TODO for testing purposes
        int activeFlag = DBUtil.convertBooleanToInt(false);
        values.put(RssFeedsContract.ChannelEntry.COLUMN_IS_CHANNEL_ACTIVE, activeFlag);

        //Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = database.insert(RssFeedsContract.ChannelEntry.TABLE_NAME, null, values);

        close();

        return newRowId;
    }

    public List<RssChannel> getRssChannels() {

        open();

        List<RssChannel> rssChannels = new LinkedList<>();

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                RssFeedsContract.ChannelEntry.COLUMN_NAME + " ASC";

        Cursor c = database.query(
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
            close();
        }
        return rssChannels;
    }

    public void updateRssChannel(RssChannel rssChannel) {

        Timber.d("updated:%s", rssChannel);
        open();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(RssFeedsContract.ChannelEntry.COLUMN_NAME, rssChannel.getName());
        values.put(RssFeedsContract.ChannelEntry.COLUMN_URL, rssChannel.getUrl());

        // Which row to update, based on the ID
        String selection = RssFeedsContract.ChannelEntry._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(rssChannel.getId()) };

        database.update(RssFeedsContract.ChannelEntry.TABLE_NAME, values, selection, selectionArgs);

        close();
    }

    public void updateRssChannelActiveFlag(RssChannel rssChannel) {

        open();

        // New value for one column
        ContentValues values = new ContentValues();
        int isChannelActive = DBUtil.convertBooleanToInt(rssChannel.isChannelActive());
        Timber.d("is channel active:%d", isChannelActive);
        values.put(RssFeedsContract.ChannelEntry.COLUMN_IS_CHANNEL_ACTIVE, isChannelActive);

        // Which row to update, based on the ID
        String selection = RssFeedsContract.ChannelEntry._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(rssChannel.getId()) };

        database.update(RssFeedsContract.ChannelEntry.TABLE_NAME, values, selection, selectionArgs);

        close();
    }

    public void deleteRssChannels(ArrayList<Long> idsToDeleteList) {

        open();

        Timber.d("list size to delete:%s", idsToDeleteList.size());
        for (Long l: idsToDeleteList) {
            Timber.d("delete id:%d", l);
            // Define 'where' part of query.
            String selection = RssFeedsContract.ChannelEntry._ID + " LIKE ?";
            // Specify arguments in placeholder order.
            String[] selectionArgs = {String.valueOf(l)};
            // Issue SQL statement.
            database.delete(RssFeedsContract.ChannelEntry.TABLE_NAME, selection, selectionArgs);
        }
        close();
    }

    public List<RssChannel> getActiveRssChannels() {

        open();

        List<RssChannel> activeRssChannels = new LinkedList<>();

        SQLiteDatabase db = rssFeedsDbHelper.getReadableDatabase();

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                RssFeedsContract.ChannelEntry.COLUMN_NAME + " ASC";

        Cursor c = db.query(
                RssFeedsContract.ChannelEntry.TABLE_NAME,  // The table to query
                allColumns,                               // The columns to return
                RssFeedsContract.ChannelEntry.COLUMN_IS_CHANNEL_ACTIVE + "=?",                                // The columns for the WHERE clause
                new String[] {"1"},                            // The values for the WHERE clause
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
                activeRssChannels.add(rssChannel);
            }
        }
        finally {
            c.close();
            close();
        }
        return activeRssChannels;
    }
}
