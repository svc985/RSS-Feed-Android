package org.prikic.yafr.background;

import android.app.IntentService;
import android.content.Intent;

import org.prikic.yafr.db.dao.RssChannelDAO;
import org.prikic.yafr.model.RssChannel;
import org.prikic.yafr.model.xmlService.Feed;
import org.prikic.yafr.service.ServiceFactory;

import java.io.IOException;
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
            //String testUrl = rssChannels.get(0).getUrl();
            String url = rssChannel.getUrl();
            Call<Feed> feed = ServiceFactory.buildService().getFeeds(url);
            //Call<Feed> feed = ServiceFactory.buildService().getFeeds("http://www.blic.rs/rss/Komentar/Sport");
            Timber.d("test URL is:%s", url);

            try {
                Response<Feed> feedResponse = feed.execute();
                Timber.d("feedResponse:%d", feedResponse.body().getmChannel().getFeedItems().size());
            } catch (IOException e) {
                Timber.e(e.getMessage());
            }
        }
    }
}
