package org.prikic.yafr.activities;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.prikic.yafr.R;
import org.prikic.yafr.adapters.SourcesAdapter;
import org.prikic.yafr.loaders.ChannelLoader;
import org.prikic.yafr.loaders.Loaders;
import org.prikic.yafr.model.RssChannel;

import java.util.LinkedList;
import java.util.List;

import timber.log.Timber;

public class SourcesFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<RssChannel>>{

    private CoordinatorLayout coordinatorLayout;

    private List<RssChannel> rssChannelList = new LinkedList<>();

    private SourcesAdapter adapter;

    public SourcesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLoaderManager().initLoader(Loaders.GET_ALL_RSS_CHANNELS.ordinal(), null, this).forceLoad();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.sources, container, false);

        RecyclerView recyclerView = (RecyclerView) fragmentView.findViewById(R.id.recycler_view_sources_fragment);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        //recycler view adapter
        adapter = new SourcesAdapter(rssChannelList);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) fragmentView.findViewById(R.id.btn_add_new_source);

        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Timber.d("open add sources fragment/activity");
                    SaveOrEditChannelFragment fragment = new SaveOrEditChannelFragment();
                    fragment.setFragmentTitle(getResources().getString(R.string.save_new_rss_source));
                    fragment.show(getActivity().getSupportFragmentManager(), "SaveOrEditChannelFragment");
                }
            });
        }

        coordinatorLayout = (CoordinatorLayout) fragmentView.findViewById(R.id.coordinator_layout_sources_fragment);

        return fragmentView;
    }

    public void displaySnackbarFor(RssChannel rssChannel) {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, R.string.rss_channel_saved, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundResource(R.color.colorPrimary);
        snackbar.show();

        //add rssChannel to rssChannelList
        adapter.addRssChannelToChannelList(rssChannel);
    }

    @Override
    public ChannelLoader onCreateLoader(int id, Bundle args) {
        Timber.d("onCreate Loader - load rss channels");
        return new ChannelLoader(getContext());
    }

    @Override
    public void onLoadFinished(Loader<List<RssChannel>> loader, List<RssChannel> data) {
        Timber.d("load finished for loading rss channels, with size:%d", data.size());
        adapter.setRssChannelList(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<RssChannel>> loader) {
        //TODO
        Timber.d("loader reset for loading rss channels");
    }
}
