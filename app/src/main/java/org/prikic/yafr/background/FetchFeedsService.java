package org.prikic.yafr.background;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import org.prikic.yafr.db.dao.RssChannelDAO;
import org.prikic.yafr.model.FeedItemExtended;
import org.prikic.yafr.model.RssChannel;
import org.prikic.yafr.model.xmlService.Channel;
import org.prikic.yafr.model.xmlService.Feed;
import org.prikic.yafr.model.xmlService.FeedImage;
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

        RssChannelDAO rssChannelDAO = RssChannelDAO.getInstance(this);
        List<RssChannel> rssChannels = rssChannelDAO.getActiveRssChannels();
        Timber.d("num of active channels:%d", rssChannels.size());

        for(RssChannel rssChannel : rssChannels) {

            String url = rssChannel.getUrl();
            Call<Feed> fetchFeeds = ServiceFactory.buildService().getFeeds(url);
            Timber.d("channel's URL is:%s", url);

            try {
                Response<Feed> feedResponse = fetchFeeds.execute();
                Feed feed = feedResponse.body();
                int responseCode = feedResponse.code();
                if (responseCode == Constants.HTTP_STATUS_OK) {
                    ArrayList<FeedItem> feedItems = feed.getChannel().getFeedItems();
                    String channelImageURL = getChannelImageURL(feed);
                    ArrayList<FeedItemExtended> feedItemsExtended = convertFeedItemsToExtendedForm(feedItems, channelImageURL);

                    Timber.d("feedResponse size:%d", feedItemsExtended.size());
                    Timber.d("channel image logo URL:%s", channelImageURL);

                    //send local broadcast
                    Intent localIntent = new Intent(Constants.BROADCAST_ACTION_FEEDS_FETCHED);
                    // Put feedItems list into Intent
                    localIntent.putExtra(Constants.EXTENDED_DATA_FEED_ITEM_LIST, feedItemsExtended);
                    // Broadcasts the Intent to receivers in this app.
                    LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
                }
                else {
                    Timber.d("response code:%d", responseCode);
                }

            } catch (IOException e) {
                Timber.e(e.getMessage());
            }
        }
    }

    private ArrayList<FeedItemExtended> convertFeedItemsToExtendedForm(ArrayList<FeedItem> feedItems, String feedImageURL) {

        ArrayList<FeedItemExtended> feedItemsExtended = new ArrayList<>();

        for(FeedItem feedItem : feedItems) {
            feedItemsExtended.add(new FeedItemExtended(feedItem.getDescription(), feedItem.getLink(),
                    feedItem.getTitle(), feedItem.getPubDate(), feedImageURL));
        }
        return feedItemsExtended;
    }

    private String getChannelImageURL(Feed feed) {

        Channel channel = feed.getChannel();
        if (channel != null) {
            FeedImage feedImage = channel.getFeedImage();
            if (feedImage != null) {
                return feedImage.getUrl();
            }
        }
        return "";
    }
}
