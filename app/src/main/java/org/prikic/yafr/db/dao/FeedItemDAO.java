package org.prikic.yafr.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import org.prikic.yafr.db.RssFeedsContract;
import org.prikic.yafr.db.RssFeedsDbHelper;
import org.prikic.yafr.model.FeedItemExtended;

import timber.log.Timber;

public class FeedItemDAO {

    private static FeedItemDAO instance;

    //Database fields
    private SQLiteDatabase database;
    private RssFeedsDbHelper rssFeedsDbHelper;
    private String[] allColumns = {RssFeedsContract.FeedItemEntry._ID,
            RssFeedsContract.FeedItemEntry.COLUMN_PUB_DATE,
            RssFeedsContract.FeedItemEntry.COLUMN_TITLE,
            RssFeedsContract.FeedItemEntry.COLUMN_LINK,
            RssFeedsContract.FeedItemEntry.COLUMN_DESCRIPTION,
            RssFeedsContract.FeedItemEntry.COLUMN_SOURCE_IMAGE_URL};


    private FeedItemDAO () {
    }

    private FeedItemDAO(Context context) {
        rssFeedsDbHelper = new RssFeedsDbHelper(context);
    }

    public static FeedItemDAO getInstance(Context context) {
        if (instance == null)
            instance = new FeedItemDAO(context);
        return instance;
    }

    public void open() throws SQLException {
        database = rssFeedsDbHelper.getWritableDatabase();
    }

    public void close() {
        database.close();
    }

    public long saveFeed(FeedItemExtended feedItemExtended) {

        //open db connection - write mode
        open();

        //Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(RssFeedsContract.FeedItemEntry.COLUMN_PUB_DATE, feedItemExtended.getPubDate());
        values.put(RssFeedsContract.FeedItemEntry.COLUMN_TITLE, feedItemExtended.getTitle());
        values.put(RssFeedsContract.FeedItemEntry.COLUMN_LINK, feedItemExtended.getLink());
        values.put(RssFeedsContract.FeedItemEntry.COLUMN_DESCRIPTION, feedItemExtended.getDescription());
        values.put(RssFeedsContract.FeedItemEntry.COLUMN_SOURCE_IMAGE_URL, feedItemExtended.getSourceImageUrl());

        //Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = database.insert(RssFeedsContract.FeedItemEntry.TABLE_NAME, null, values);

        close();

        return newRowId;
    }

    public void deleteFeed(FeedItemExtended feedItemExtended) {

        open();

        Timber.d("deleting Feed:%s", feedItemExtended.getLink());
        // Define 'where' part of query.
        String selection = RssFeedsContract.FeedItemEntry.COLUMN_LINK + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {feedItemExtended.getLink()};
        // Issue SQL statement.
        database.delete(RssFeedsContract.FeedItemEntry.TABLE_NAME, selection, selectionArgs);

        close();

    }

    
}