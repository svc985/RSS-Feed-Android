package org.prikic.yafr.background;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;

import org.prikic.yafr.screens.MainActivity;
import org.prikic.yafr.db.dao.RssChannelDAO;
import org.prikic.yafr.model.RssChannel;
import org.prikic.yafr.util.Constants;
import org.prikic.yafr.util.RssChannelOperation;

import java.util.List;

public class ChannelOperationAsyncTask extends AsyncTask<Void, Void, RssChannel> {

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
    protected RssChannel doInBackground(Void... params) {
        switch (operation) {
            case SAVE:
                RssChannel rssChannel1 = (RssChannel) object;
                Long id = rssChannelDAO.saveRssChannel(rssChannel1);
                rssChannel1.setId(id);
                return rssChannel1;
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
    protected void onPostExecute(RssChannel rssChannel) {
        super.onPostExecute(rssChannel);
        mainActivity.enableProgressSpinner(false);

        if ( operation == RssChannelOperation.SAVE) {
            //TODO channel is saved - send local broadcast
            Intent localIntent = new Intent(Constants.BROADCAST_ACTION_RSS_CHANNEL_SAVED);

            // Puts the rssChannel into the Intent
            localIntent.putExtra(Constants.EXTENDED_DATA_RSS_CHANNEL, rssChannel);
            // Broadcasts the Intent to receivers in this app.
            LocalBroadcastManager.getInstance(mainActivity).sendBroadcast(localIntent);
        }
    }
}
