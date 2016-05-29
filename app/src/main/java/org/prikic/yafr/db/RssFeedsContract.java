package org.prikic.yafr.db;

import android.provider.BaseColumns;

public final class RssFeedsContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public RssFeedsContract() {}

    /* Inner class that defines the table contents */
    public static abstract class ChannelEntry implements BaseColumns {
        public static final String TABLE_NAME = "channel";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_IS_CHANNEL_ACTIVE = "active";

        private static final String TEXT_TYPE = " TEXT";
        private static final String INTEGER_TYPE = " INTEGER";
        private static final String COMMA_SEP = ",";
        protected static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + ChannelEntry.TABLE_NAME + " (" +
                ChannelEntry._ID + " INTEGER PRIMARY KEY," +
                ChannelEntry.COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                ChannelEntry.COLUMN_URL + TEXT_TYPE + COMMA_SEP +
                        ChannelEntry.COLUMN_IS_CHANNEL_ACTIVE + INTEGER_TYPE +
                ")";

        protected static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + ChannelEntry.TABLE_NAME;
    }

    /* Inner class that defines the table contents */
    public static abstract class FeedItemEntry implements BaseColumns {
        public static final String TABLE_NAME = "feed";
        //TODO save according to the ISO8601 string ("YYYY-MM-DD HH:MM:SS.SSS")
        public static final String COLUMN_PUB_DATE = "pubDate";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_LINK = "link";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_SOURCE_IMAGE_URL = "sourceImageURL";

        private static final String TEXT_TYPE = " TEXT";
        private static final String COMMA_SEP = ",";
        protected static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + FeedItemEntry.TABLE_NAME + " (" +
                        FeedItemEntry._ID + " INTEGER PRIMARY KEY," +
                        FeedItemEntry.COLUMN_PUB_DATE + TEXT_TYPE + COMMA_SEP +
                        FeedItemEntry.COLUMN_TITLE + TEXT_TYPE + COMMA_SEP +
                        FeedItemEntry.COLUMN_LINK + TEXT_TYPE + COMMA_SEP +
                        FeedItemEntry.COLUMN_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                        FeedItemEntry.COLUMN_SOURCE_IMAGE_URL + TEXT_TYPE +
                        ")";

        protected static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + FeedItemEntry.TABLE_NAME;
    }

}
