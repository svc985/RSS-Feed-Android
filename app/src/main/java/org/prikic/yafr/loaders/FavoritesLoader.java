package org.prikic.yafr.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import org.prikic.yafr.db.dao.FeedItemDAO;
import org.prikic.yafr.model.FeedItemExtended;

import java.util.List;

import timber.log.Timber;

public class FavoritesLoader extends AsyncTaskLoader<List<FeedItemExtended>> {

    private Context context;

    public FavoritesLoader(Context context) {
        super(context);
        Timber.d("Favorites loader - initialized");
        this.context = context;
    }

    @Override
    public List<FeedItemExtended> loadInBackground() {
        Timber.d("Favorites loader - loadInBackground");
        FeedItemDAO feedItemDAO = FeedItemDAO.getInstance(context);
        return feedItemDAO.getFavorites();
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