package org.prikic.yafr.util;

public class Constants {

    public static final int NUM_OF_FRAGMENTS = 3;

    public static final String BROADCAST_ACTION_RSS_CHANNEL_SAVED =
            "org.prikic.yafr.rssChannel.SAVED";
    public static final String BROADCAST_ACTION_FEEDS_FETCHED =
            "org.prikic.yafr.FeedItemList.FETCHED";

    public static final String EXTENDED_DATA_FEED_ITEM_LIST =
            "org.prikic.yafr.list.feedItem";

    public static final String INTENT_FEED_ITEM =
            "org.prikic.yafr.feedItem";
    public static final String INTENT_FEED_IS_FAVORITE =
            "org.prikic.yafr.feedItem.favorite";

    public static final int HTTP_STATUS_OK = 200;

    public static final int REQUEST_CODE_FETCH_FEEDS_PERIODICALLY = 12345;
}
