package org.prikic.yafr.background;

import android.content.Context;
import android.os.AsyncTask;

import org.prikic.yafr.activities.MainActivity;
import org.prikic.yafr.db.dao.RssChannelDAO;
import org.prikic.yafr.model.RssChannel;
import org.prikic.yafr.util.RssChannelOperation;

public class ChannelOperationAsyncTask extends AsyncTask<Void, Void, Void> {

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
    protected Void doInBackground(Void... params) {
        switch (operation) {
            case SAVE:
                rssChannelDAO.saveRssChannel(rssChannel);
                break;
            case EDIT:
                rssChannelDAO.updateRssChannel(rssChannel);
                break;
            case UPDATE_ACTIVE_FLAG:
                rssChannelDAO.updateRssChannelActiveFlag(rssChannel);
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        mainActivity.enableProgressSpinner(false);
    }
}
