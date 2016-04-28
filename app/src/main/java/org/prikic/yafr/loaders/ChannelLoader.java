package org.prikic.yafr.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import org.prikic.yafr.model.RssChannel;
import org.prikic.yafr.util.Util;

import java.util.List;

import timber.log.Timber;

public class ChannelLoader extends AsyncTaskLoader<List<RssChannel>>{

    public ChannelLoader(Context context) {
        super(context);
        Timber.d("Channel loader - initialized");
        forceLoad();
        onContentChanged();
    }

    @Override
    public List<RssChannel> loadInBackground() {
        //TODO load data from db here??
        Timber.d("Channel loader - loadInBackground");
        return Util.getDummyDataset();
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
