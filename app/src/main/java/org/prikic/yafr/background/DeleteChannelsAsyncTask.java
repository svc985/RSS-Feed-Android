package org.prikic.yafr.background;

import android.content.Context;
import android.os.AsyncTask;

import org.prikic.yafr.activities.MainActivity;
import org.prikic.yafr.db.dao.RssChannelDAO;

import java.util.ArrayList;

import timber.log.Timber;

public class DeleteChannelsAsyncTask extends AsyncTask<Void, Void, Void> {

    private ArrayList<Long> channelListToDelete;
    private RssChannelDAO rssChannelDAO;
    private MainActivity mainActivity;

    public DeleteChannelsAsyncTask(Context context, ArrayList<Long> channelListToDelete) {
        this.channelListToDelete = channelListToDelete;
        rssChannelDAO = RssChannelDAO.getInstance(mainActivity);
        mainActivity = (MainActivity) context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        //TODO perform check - mainActivity may be null
        mainActivity.enableProgressSpinner(true);
    }

    @Override
    protected Void doInBackground(Void... params) {

        for (Long l : channelListToDelete) {
            Timber.d("id to delete:%d", l);
        }

        rssChannelDAO.deleteRssChannels(channelListToDelete);
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {

        mainActivity.enableProgressSpinner(false);

    }
}
