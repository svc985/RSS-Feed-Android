package org.prikic.yafr.db;

import android.provider.BaseColumns;

public final class RssFeedsContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public RssFeedsContract() {}

    /* Inner class that defines the table contents */
    public static abstract class ChannelEntry implements BaseColumns {
        public static final String TABLE_NAME = "channel";
        //public static final String COLUMN_CHANNEL_ID = "channelId";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_URL = "url";

        private static final String TEXT_TYPE = " TEXT";
        private static final String COMMA_SEP = ",";
        protected static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + ChannelEntry.TABLE_NAME + " (" +
                ChannelEntry._ID + " INTEGER PRIMARY KEY," +
                ChannelEntry.COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                ChannelEntry.COLUMN_URL + TEXT_TYPE +
                " )";

        protected static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + ChannelEntry.TABLE_NAME;
    }

}
