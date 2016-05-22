package org.prikic.yafr.screens;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import org.prikic.yafr.R;
import org.prikic.yafr.adapters.SourcesAdapter;
import org.prikic.yafr.background.ChannelOperationAsyncTask;
import org.prikic.yafr.loaders.ChannelLoader;
import org.prikic.yafr.loaders.Loaders;
import org.prikic.yafr.model.RssChannel;
import org.prikic.yafr.util.RssChannelOperation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import timber.log.Timber;

public class SourcesFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<RssChannel>>{

    private CoordinatorLayout coordinatorLayout;

    private List<RssChannel> rssChannelList = new LinkedList<>();
    public static List<Long> selectedItemIdList;

    private SourcesAdapter adapter;

    private boolean isInChoiceMode;

    MainActivity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLoaderManager().initLoader(Loaders.GET_ALL_RSS_CHANNELS.ordinal(), null, this).forceLoad();

        activity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        /*create an arraylist for the selected items ids*/
        selectedItemIdList = new ArrayList<>();
        /*we're not in choice mode so set the default*/
        isInChoiceMode = false;

        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.sources, container, false);

        final RecyclerView recyclerView = (RecyclerView) fragmentView.findViewById(R.id.recycler_view_sources_fragment);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        //recycler view adapter
        adapter = new SourcesAdapter(getActivity(), rssChannelList);
        recyclerView.setAdapter(adapter);

        //set recycler view gesture detector
        recyclerView.addOnItemTouchListener(new MyRecyclerViewOnItemTouchListener(getContext(), recyclerView));

        FloatingActionButton fab = (FloatingActionButton) fragmentView.findViewById(R.id.btn_add_new_source);

        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Timber.d("open add sources fragment/activity");
                    SaveOrEditChannelFragment fragment = SaveOrEditChannelFragment.newInstance(new RssChannel(), RssChannelOperation.SAVE, -1);
                    fragment.show(getActivity().getSupportFragmentManager(), "SaveOrEditChannelFragment");
                }
            });
        }

        coordinatorLayout = (CoordinatorLayout) fragmentView.findViewById(R.id.coordinator_layout_sources_fragment);

        return fragmentView;
    }

    public void displaySnackbarEditedRssChannel(RssChannel rssChannel, int clickedItemPosition) {

        Snackbar snackbar = Snackbar.make(coordinatorLayout, R.string.rss_channel_edited, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundResource(R.color.colorPrimary);
        snackbar.show();
        adapter.updateRssChannelListAtPosition(rssChannel, clickedItemPosition);

    }

    public void displaySnackBarSavedRssChannel(RssChannel rssChannel) {

        Snackbar snackbar = Snackbar.make(coordinatorLayout, R.string.rss_channel_saved, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundResource(R.color.colorPrimary);
        snackbar.show();
        adapter.addRssChannelToChannelList(rssChannel);
    }

    /*cancel any selections*/
    public void cancelSelections() {
        isInChoiceMode = false;
        /*empty the list of selected ids*/
        selectedItemIdList.clear();
        /*notify adapter so we can reset the item display to normal
        * -LayoutManagers will be forced to fully rebind and relayout all visible views*/
        adapter.notifyDataSetChanged();
    }

    @Override
    public ChannelLoader onCreateLoader(int id, Bundle args) {
        Timber.d("onCreate Loader - load rss channels");
        return new ChannelLoader(getContext());
    }

    @Override
    public void onLoadFinished(Loader<List<RssChannel>> loader, List<RssChannel> data) {
        Timber.d("load finished for loading rss channels, with size:%d", data.size());
        rssChannelList = data;
        adapter.setRssChannelList(data);
    }

    @Override
    public void onLoaderReset(Loader<List<RssChannel>> loader) {
        //TODO
        Timber.d("loader reset for loading rss channels");
    }

    private class MyRecyclerViewOnItemTouchListener implements RecyclerView.OnItemTouchListener {

        private final GestureDetectorCompat gestureDetectorCompat;

        public MyRecyclerViewOnItemTouchListener (Context context, final RecyclerView recyclerView) {
            gestureDetectorCompat = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener() {


                @Override
                public boolean onDown(MotionEvent e) {
                   /*tap occurs with down motion event
                    * -best practice to include it with true returned*/
                    return true;
                }

                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
                    /*check if in choice mode
                    * -single tap only works if in choice mode*/
                    /*check if in choice mode
                    * -single tap only works if in choice mode*/
                    if (isInChoiceMode) {
                        /*get the selected view*/
                        View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                         /*add haptic feedback*/
                        child.setHapticFeedbackEnabled(true);
                        child.performHapticFeedback(
                                HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING);
                        /*get the selected itemTextView's position in the adapter*/
                        int selectedPosition = recyclerView.getChildAdapterPosition(child);
                        /*check if we get a valid selected position id*/
                        if (selectedPosition != -1) {
                           /*add selected itemTextView to selected list*/
                            addItemIdToSelectedList(rssChannelList.get(selectedPosition).getId());
                            /*update adapter to mark the selected item*/
                            adapter.notifyDataSetChanged();
                            /*update the toolbar to show increased number of selected items*/
                            activity.updateToolbar(selectedItemIdList.size());
                        }
                    }
                    return false;
                }

                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    /*get the selected view*/
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    /*get the selected itemTextView's position in the adapter*/
                    int selectedPosition = recyclerView.getChildAdapterPosition(child);
                    /*check if we get a valid selected position id*/
                    if (selectedPosition != -1) {
                        Timber.d("on double tap registered.");
                        /*we're currently not using double tap
                        * so no code here*/
                    }
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    /*check if in choice mode
                    * -long press starts choice mode
                    * -long press will only work if not in choice mode*/
                    Timber.d("on long press detected");

                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());

                    if (child == null) {
                        return;
                    }

                    if (!isInChoiceMode) {
                        /*we're now in choice mode*/
                        isInChoiceMode = true;
                        /*show the action mode toolbar*/
                        activity.showActionModeToolbar();
                         /*get the selected view*/

                        /*add haptic feedback*/
                        child.setHapticFeedbackEnabled(true);
                        child.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                        /*get the selected itemTextView's position in the adapter*/
                        int selectedPosition = recyclerView.getChildAdapterPosition(child);
                        /*check if we get a valid selected position id*/
                        if (selectedPosition != -1) {
                            /*add selected id to selected list*/
                            addItemIdToSelectedList(rssChannelList.get(selectedPosition).getId());
                            /*update adapter to mark the selected item*/
                            adapter.notifyDataSetChanged();
                            /*update toolbar counter to show increased number of selected items*/
                            activity.updateToolbar(selectedItemIdList.size());
                        }
                    }
                }
            });
        }

        /*checks if items id in list, adds to list if not
    * - or removes it (for un selecting)*/
        private void addItemIdToSelectedList(Long itemId) {
        /*if not already in list*/
            if (!selectedItemIdList.contains(itemId)) {
            /*add items id to the list*/
                selectedItemIdList.add(itemId);
            } else {
            /*item already selected, so in the list
            - un select it by
            * removing id from selected list*/
                Iterator<Long> i = selectedItemIdList.iterator();
                while (i.hasNext()) {
                    Long listItemId = i.next();
                    if (listItemId.equals(itemId)) {
                        i.remove();
                    }
                }
             /*if no more selected items*/
                if (selectedItemIdList.size() < 1) {
                /*exit choice mode*/
                    isInChoiceMode = false;
                /*show the normal toolbar*/
                    activity.showNormalToolbar();
                }
            }
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                        /*required*/
            /*passes event on to gestureDetectorCompat because we return false
            ,indicating we did not consume event here*/
            gestureDetectorCompat.onTouchEvent(e);
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            /*required*/
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            /*required*/
        }
    }

    protected void deleteSelectedChannels() {
        Timber.d("delete selected channels...");
        new ChannelOperationAsyncTask(selectedItemIdList, RssChannelOperation.DELETE, getActivity()).execute();
        getLoaderManager().getLoader(Loaders.GET_ALL_RSS_CHANNELS.ordinal()).onContentChanged();
        activity.showNormalToolbar();
    }
}
