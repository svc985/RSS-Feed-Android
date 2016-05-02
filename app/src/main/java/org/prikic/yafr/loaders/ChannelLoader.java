package org.prikic.yafr.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import org.prikic.yafr.db.dao.RssChannelDAO;
import org.prikic.yafr.model.RssChannel;

import java.util.List;

import timber.log.Timber;

public class ChannelLoader extends AsyncTaskLoader<List<RssChannel>>{

    private Context context;

    public ChannelLoader(Context context) {
        super(context);
        Timber.d("Channel loader - initialized");
        this.context = context;
    }

    @Override
    public List<RssChannel> loadInBackground() {
        Timber.d("Channel loader - loadInBackground");
        RssChannelDAO rssChannelDAO = new RssChannelDAO(context);
        return rssChannelDAO.getRssChannels();
    }

    @Override
    protected void onStartLoading() {
        if (takeContentChanged())
            forceLoad();
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }
}
