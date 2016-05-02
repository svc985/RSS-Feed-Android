package org.prikic.yafr.background;

import android.content.Context;
import android.os.AsyncTask;

import org.prikic.yafr.MainActivity;
import org.prikic.yafr.db.dao.RssChannelDAO;
import org.prikic.yafr.model.RssChannel;
import org.prikic.yafr.util.RssChannelOperation;

import timber.log.Timber;

public class ChannelOperationAsyncTask extends AsyncTask<Void, Void, Long> {

    RssChannel rssChannel;
    RssChannelDAO rssChannelDAO;
    MainActivity mainActivity;
    RssChannelOperation operation;

    public ChannelOperationAsyncTask(RssChannel rssChannel, RssChannelOperation operation, Context context) {
        this.rssChannel = rssChannel;
        this.operation = operation;
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
        switch (operation) {
            case SAVE:
                return rssChannelDAO.saveRssChannel(rssChannel);
            case EDIT:
                rssChannelDAO.updateRssChannel(rssChannel);
            default:
                return -1L;
        }
    }

    @Override
    protected void onPostExecute(Long channelId) {
        Timber.d("id of saved channel:%d", channelId);
        mainActivity.enableProgressSpinner(false);
    }
}
