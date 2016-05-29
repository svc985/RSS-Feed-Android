package org.prikic.yafr.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.prikic.yafr.R;
import org.prikic.yafr.adapters.FavoritesAdapter;
import org.prikic.yafr.loaders.FavoritesLoader;
import org.prikic.yafr.loaders.Loaders;
import org.prikic.yafr.model.FeedItemExtended;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class FavoritesFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<FeedItemExtended>>{

    RecyclerView.Adapter adapter;

    private ArrayList<FeedItemExtended> feedItems;

    public FavoritesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLoaderManager().initLoader(Loaders.GET_ALL_FAVORITES.ordinal(), null, this).forceLoad();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.favorites, container, false);

        final RecyclerView recyclerView = (RecyclerView) fragmentView.findViewById(R.id.recycler_view_favorites_fragment);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        feedItems = new ArrayList<>();

        //recycler view adapter
        adapter = new FavoritesAdapter(getActivity(), feedItems);
        recyclerView.setAdapter(adapter);

        return fragmentView;
    }

    @Override
    public FavoritesLoader onCreateLoader(int id, Bundle args) {
        Timber.d("onCreate Loader - load feed favorites");
        return new FavoritesLoader(getContext());
    }

    @Override
    public void onLoadFinished(Loader<List<FeedItemExtended>> loader, List<FeedItemExtended> data) {
        Timber.d("load finished for loading rss channels, with size:%d", data.size());
        feedItems.clear();
        feedItems.addAll(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<FeedItemExtended>> loader) {
        //TODO
        Timber.d("loader reset for loading rss channels");
    }

}
