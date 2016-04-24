package org.prikic.yafr.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import org.prikic.yafr.db.RssFeedsContract;
import org.prikic.yafr.db.RssFeedsDbHelper;
import org.prikic.yafr.model.RssChannel;

public class RssChannelDAO {

    //Database fields
    private SQLiteDatabase database;
    private RssFeedsDbHelper rssFeedsDbHelper;
    private String[] allColumns = {RssFeedsContract.ChannelEntry._ID,
    RssFeedsContract.ChannelEntry.COLUMN_NAME, RssFeedsContract.ChannelEntry.COLUMN_URL};

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
        //TODO what to set for id??
        //values.put(RssFeedsContract.ChannelEntry._ID, id);
        values.put(RssFeedsContract.ChannelEntry.COLUMN_NAME, rssChannel.getName());
        values.put(RssFeedsContract.ChannelEntry.COLUMN_URL, rssChannel.getUrl());

        //Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = database.insert(RssFeedsContract.ChannelEntry.TABLE_NAME, null, values);

        //TODO should not be closed??
        //https://groups.google.com/forum/#!msg/android-developers/nopkaw4UZ9U/cPfPL3uW7nQJ
        //close();

        return newRowId;
    }
}
