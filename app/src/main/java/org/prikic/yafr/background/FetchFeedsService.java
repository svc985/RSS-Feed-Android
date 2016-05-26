package org.prikic.yafr.background;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import org.prikic.yafr.db.dao.RssChannelDAO;
import org.prikic.yafr.model.RssChannel;
import org.prikic.yafr.model.xmlService.Feed;
import org.prikic.yafr.model.xmlService.FeedItem;
import org.prikic.yafr.service.ServiceFactory;
import org.prikic.yafr.util.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import timber.log.Timber;

public class FetchFeedsService extends IntentService {

    public FetchFeedsService() {
        super("FetchFeedsService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Timber.d("FetchFeedsService onHandleIntent...");

        RssChannelDAO rssChannelDAO = new RssChannelDAO(this);
        List<RssChannel> rssChannels = rssChannelDAO.getActiveRssChannels();
        Timber.d("num of active channels:%d", rssChannels.size());

        //TODO replace hardcoded value
        for(RssChannel rssChannel : rssChannels) {
            //String url = "http://sport.blic.rs/rss/danasnje-vesti";
            String url = rssChannel.getUrl();
            Call<Feed> fetchFeeds = ServiceFactory.buildService().getFeeds(url);
            Timber.d("test URL is:%s", url);

            try {
                Response<Feed> feedResponse = fetchFeeds.execute();
                Feed feed = feedResponse.body();
                Timber.d("response code:%d", feedResponse.code());
                if (feed.getChannel().getFeedItems() != null) {
                    ArrayList<FeedItem> feedItems = feed.getChannel().getFeedItems();
                    Timber.d("feedResponse:%d", feed.getChannel().getFeedItems().size());
                    Timber.d("feed image logo:%s", feed.getChannel().getFeedImage().getUrl());

                    //send local broadcast
                    Intent localIntent = new Intent(Constants.BROADCAST_ACTION_FEEDS_FETCHED);
                    // Put feedItems list into Intent
                    localIntent.putExtra(Constants.EXTENDED_DATA_FEED_ITEM_LIST, feedItems);
                    //TODO localIntent.putParcelableArrayListExtra(Constants.EXTENDED_DATA_FEED_ITEM_LIST, feedItems);
                    // Broadcasts the Intent to receivers in this app.
                    LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
                }

            } catch (IOException e) {
                Timber.e(e.getMessage());
            }
        }
    }
}
