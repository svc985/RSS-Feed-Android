package org.prikic.yafr.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.prikic.yafr.R;
import org.prikic.yafr.adapters.FeedsAdapter;
import org.prikic.yafr.background.FetchFeedsService;
import org.prikic.yafr.model.FeedItemExtended;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FeedsFragment extends Fragment {

    private List<FeedItemExtended> feedItemList;
    RecyclerView.Adapter adapter;

    public FeedsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //start service for fetching feeds
        Activity activity = getActivity();
        Intent intent = new Intent(activity, FetchFeedsService.class);
        activity.startService(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.feeds, container, false);

        final RecyclerView recyclerView = (RecyclerView) fragmentView.findViewById(R.id.recycler_view_feeds_fragment);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        feedItemList = new ArrayList<>();

        //recycler view adapter
        adapter = new FeedsAdapter(getActivity(), feedItemList);
        recyclerView.setAdapter(adapter);

        return fragmentView;
    }

    public void updateFeedItemsList(ArrayList feedItems) {

        feedItemList.clear();
        feedItemList.addAll(feedItems == null ? Collections.EMPTY_LIST : feedItems);
        adapter.notifyDataSetChanged();
    }
}
