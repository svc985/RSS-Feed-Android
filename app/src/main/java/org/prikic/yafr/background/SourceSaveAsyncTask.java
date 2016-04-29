package org.prikic.yafr.background;

import android.content.Context;
import android.os.AsyncTask;

import org.prikic.yafr.MainActivity;
import org.prikic.yafr.db.dao.RssChannelDAO;
import org.prikic.yafr.model.RssChannel;

import timber.log.Timber;

public class SourceSaveAsyncTask extends AsyncTask<Void, Void, Long> {

    RssChannel rssChannel;
    RssChannelDAO rssChannelDAO;
    MainActivity mainActivity;

    public SourceSaveAsyncTask(RssChannel rssChannel, Context context) {
        this.rssChannel = rssChannel;
        rssChannelDAO = new RssChannelDAO(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mainActivity.enableProgressSpinner(true);
    }

    @Override
    protected Long doInBackground(Void... params) {
        //return rssChannelDAO.saveRssChannel(rssChannel);
        //TODO fixed for testing purposes
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 1l;
    }



    @Override
    protected void onPostExecute(Long channelId) {
        Timber.d("id of saved channel:%d", channelId);
        mainActivity.enableProgressSpinner(false);
    }
}
