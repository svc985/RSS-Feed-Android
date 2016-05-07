package org.prikic.yafr.background;

import android.app.IntentService;
import android.content.Intent;

import org.prikic.yafr.model.xmlService.Feed;
import org.prikic.yafr.service.ServiceFactory;

import java.io.IOException;

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

        //TODO replace hardcoded value
        Call<Feed> feed = ServiceFactory.buildService().getFeeds("http://www.blic.rs/rss/Komentar/Sport");

        try {
            Response<Feed> feedResponse = feed.execute();
            Timber.d(feedResponse.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
