package org.prikic.yafr.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.prikic.yafr.R;
import org.prikic.yafr.adapters.FeedsAdapter;
import org.prikic.yafr.background.FetchFeedsAlarmReceiver;
import org.prikic.yafr.background.FetchFeedsAsyncTask;
import org.prikic.yafr.model.FeedItemExtended;
import org.prikic.yafr.util.Constants;

import java.util.ArrayList;
import java.util.Collections;

import timber.log.Timber;

public class FeedsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private ArrayList<FeedItemExtended> feedItemList;
    private RecyclerView.Adapter adapter;
    private SwipeRefreshLayout swipeToRefreshFeedsLayout;

    public FeedsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Timber.d("FeedsFragment - onCreate");

        scheduleAlarm();
    }

    // Setup a recurring alarm every half hour
    public void scheduleAlarm() {
        // Construct an intent that will execute the AlarmReceiver
        Intent intent = new Intent(getActivity().getApplicationContext(), FetchFeedsAlarmReceiver.class);
        // Create a PendingIntent to be triggered when the alarm goes off
        final PendingIntent pIntent = PendingIntent.getBroadcast(getContext(), Constants.REQUEST_CODE_FETCH_FEEDS_PERIODICALLY,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Setup periodic alarm every 5 seconds
        long firstMillis = System.currentTimeMillis(); // alarm is set right away
        AlarmManager alarm = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        // First parameter is the type: ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP, RTC_WAKEUP
        // Interval can be INTERVAL_FIFTEEN_MINUTES, INTERVAL_HALF_HOUR, INTERVAL_HOUR, INTERVAL_DAY
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
                AlarmManager.INTERVAL_HALF_HOUR, pIntent);
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

        swipeToRefreshFeedsLayout = (SwipeRefreshLayout) fragmentView.findViewById(R.id.swipe_to_refresh_feeds_container);
        swipeToRefreshFeedsLayout.setOnRefreshListener(this);
        // Configure the refreshing colors
        swipeToRefreshFeedsLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return fragmentView;
    }

    public void updateFeedItemsList(ArrayList<FeedItemExtended> feedItems) {
        feedItemList.addAll(feedItems == null ? Collections.<FeedItemExtended>emptyList() : feedItems);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {

        Timber.d("on refresh triggered...");

        // Your code to refresh the list here.
        // Make sure you call swipeContainer.setRefreshing(false)
        // once the network request has completed successfully.
        feedItemList.clear();
        adapter.notifyDataSetChanged();
        //start Async Task for fetching feeds
        new FetchFeedsAsyncTask(this).execute();

        // Now we call setRefreshing(false) to signal refresh has finished
        swipeToRefreshFeedsLayout.setRefreshing(false);
    }

}
