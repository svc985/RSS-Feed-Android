package org.prikic.yafr.background;

import android.content.Context;
import android.os.AsyncTask;

import org.prikic.yafr.activities.MainActivity;
import org.prikic.yafr.db.dao.RssChannelDAO;
import org.prikic.yafr.model.RssChannel;
import org.prikic.yafr.util.RssChannelOperation;

import java.util.List;

public class ChannelOperationAsyncTask extends AsyncTask<Void, Void, Void> {

    Object object;
    RssChannelDAO rssChannelDAO;
    MainActivity mainActivity;
    RssChannelOperation operation;

    public ChannelOperationAsyncTask(Object obj, RssChannelOperation operation, Context context) {
        this.object = obj;
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
                RssChannel rssChannel1 = (RssChannel) object;
                rssChannelDAO.saveRssChannel(rssChannel1);
                break;
            case EDIT:
                RssChannel rssChannel2 = (RssChannel) object;
                rssChannelDAO.updateRssChannel(rssChannel2);
                break;
            case UPDATE_ACTIVE_FLAG:
                RssChannel rssChannel3 = (RssChannel) object;
                rssChannelDAO.updateRssChannelActiveFlag(rssChannel3);
                break;
            case DELETE:
                @SuppressWarnings("unchecked")
                List<Long> channelIdsToDelete = (List<Long>) object;
                rssChannelDAO.deleteRssChannels(channelIdsToDelete);
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
