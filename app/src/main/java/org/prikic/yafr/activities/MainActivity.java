package org.prikic.yafr.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.prikic.yafr.R;
import org.prikic.yafr.background.ChannelOperationAsyncTask;
import org.prikic.yafr.fragments.FavoritesFragment;
import org.prikic.yafr.fragments.FeedsFragment;
import org.prikic.yafr.fragments.SaveOrEditChannelFragment;
import org.prikic.yafr.fragments.SourcesFragment;
import org.prikic.yafr.model.FeedItemExtended;
import org.prikic.yafr.model.RssChannel;
import org.prikic.yafr.util.Constants;
import org.prikic.yafr.util.FragmentTitle;
import org.prikic.yafr.util.RssChannelOperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity
        implements SaveOrEditChannelFragment.OnRssChannelOperationListener {

    private ViewPagerAdapter viewPagerAdapter;

    private boolean showProgressSpinnerInToolbar = false;

    private TextView txtToolbarCounter;
    private Toolbar toolbar;
    private ImageView imageIconActionToolbar;

    private void setShowProgressSpinnerInToolbar(boolean showProgressSpinnerInToolbar) {
        this.showProgressSpinnerInToolbar = showProgressSpinnerInToolbar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        if (tabLayout != null)
            tabLayout.setupWithViewPager(viewPager);

        txtToolbarCounter = (TextView) findViewById(R.id.textview_toolbar_counter);
        imageIconActionToolbar = (ImageView) findViewById(R.id.imageIconActionToolbar);

        imageIconActionToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageIconActionToolbar.getTag().equals("actionMode")) {
                    Timber.d("return to classic mode...");
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    SourcesFragment sourcesFragment = (SourcesFragment) fragmentManager.findFragmentByTag(viewPagerAdapter.fragmentTags.get(FragmentTitle.SOURCES));
                    sourcesFragment.cancelSelections();
                    showNormalToolbar();
                }
            }
        });

        IntentFilter statusIntentFilter = new IntentFilter();
        statusIntentFilter.addAction(Constants.BROADCAST_ACTION_RSS_CHANNEL_SAVED);
        statusIntentFilter.addAction(Constants.BROADCAST_ACTION_FEEDS_FETCHED);

        // Instantiates a new ResponseReceiver
        ResponseReceiver responseReceiver = new ResponseReceiver();
        // Registers the ResponseReceiver and its intent filters
        LocalBroadcastManager.getInstance(this).registerReceiver(
                responseReceiver,
                statusIntentFilter);

    }

    private void setupViewPager(ViewPager viewPager) {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private final Map<String, String> fragmentTags = new HashMap<>();
        private static final int NUM_OF_FRAGMENTS = 3;

        private ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new FeedsFragment();
                case 1:
                    return new FavoritesFragment();
                case 2:
                    return new SourcesFragment();
                default:
                    return null;
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
            switch (position) {
                case 0:
                    fragmentTags.put(FragmentTitle.FEEDS, createdFragment.getTag());
                    break;
                case 1:
                    fragmentTags.put(FragmentTitle.FAVORITES, createdFragment.getTag());
                    break;
                case 2:
                    fragmentTags.put(FragmentTitle.SOURCES, createdFragment.getTag());
                    break;
                default:
                    break;
            }
            return createdFragment;
        }

        @Override
        public int getCount() {
            return NUM_OF_FRAGMENTS;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return FragmentTitle.FEEDS;
                case 1:
                    return FragmentTitle.FAVORITES;
                case 2:
                    return FragmentTitle.SOURCES;
                default:
                    return "";
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem progressSpinner = menu.findItem(R.id.progress_spinner_menu_item);
        //if (isInChoiceMode) {
            if (!showProgressSpinnerInToolbar) {
                progressSpinner.setVisible(false);
            } else {
                progressSpinner.setVisible(true);
            }
        //}

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.about_menu_item:
                Timber.d("opening about screen...");
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_mode_menu_delete:
                Timber.d("delete selected channels...");
                FragmentManager fragmentManager = getSupportFragmentManager();
                SourcesFragment sourcesFragment = (SourcesFragment) fragmentManager.findFragmentByTag(viewPagerAdapter.fragmentTags.get(FragmentTitle.SOURCES));
                sourcesFragment.deleteSelectedChannels();
                sourcesFragment.cancelSelections();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRssChannelSaved(RssChannel rssChannel) {
        Timber.d("saving Rss channel in db...");

        new ChannelOperationAsyncTask(rssChannel, RssChannelOperation.SAVE, this).execute();

    }

    @Override
    public void onRssChannelEdited(RssChannel rssChannel, int clickedItemPosition) {
        Timber.d("editing Rss channel in db on position %d", clickedItemPosition);

        new ChannelOperationAsyncTask(rssChannel, RssChannelOperation.EDIT, this).execute();

        SourcesFragment sourcesFragment = (SourcesFragment) getSupportFragmentManager().findFragmentByTag(viewPagerAdapter.fragmentTags.get(FragmentTitle.SOURCES));
        sourcesFragment.displaySnackbarEditedRssChannel(rssChannel, clickedItemPosition);
    }

    public void enableProgressSpinner(boolean flag) {
        if (flag) {
            setShowProgressSpinnerInToolbar(true);
        }
        else {
            setShowProgressSpinnerInToolbar(false);
        }
        invalidateOptionsMenu();
    }

    /*shows the action mode toolbar*/
    public void showActionModeToolbar() {
        Timber.d("show action mode toolbar");
        /*show the title (X) and the counter*/
        txtToolbarCounter.setVisibility(View.VISIBLE);
        imageIconActionToolbar.setImageResource(R.mipmap.ic_arrow_back_white_24dp);
        imageIconActionToolbar.setTag("actionMode");
        /*clear the overflow menu*/
        toolbar.getMenu().clear();
        /*inflate the action menu*/
        toolbar.inflateMenu(R.menu.action_mode_menu);
        /*if you need to use the tag later, set the tag*/
        toolbar.setTag("actionModeToolbar");
        /*show the save icon in the toolbar*/
        MenuItem saveSelectedItemsMenuItem = toolbar.getMenu().findItem(R.id.action_mode_menu_delete);
        saveSelectedItemsMenuItem.setIcon(R.mipmap.ic_delete_white_24dp);
    }

    /*show the normal toolbar*/
    public void showNormalToolbar() {
        /*set the counter and X text views invisible*/
        //txtToolbarCounter.setVisibility(View.INVISIBLE);
        txtToolbarCounter.setText(getResources().getString(R.string.app_name));
        //txtToolbarTitle.setVisibility(View.INVISIBLE);
        imageIconActionToolbar.setImageResource(R.mipmap.ic_launcher);
        imageIconActionToolbar.setTag("classicMode");
        /*clear the menu*/
        toolbar.getMenu().clear();
        /*inflate the overflow menu*/
        toolbar.inflateMenu(R.menu.menu_main);
        /*if you need to use the tag later, set the tag*/
        toolbar.setTag("normalToolbar");

        //TODO progress spinner should be fixed, here it just remains hidden
        enableProgressSpinner(false);
    }

    /*updates the counter on toolbar to show
    * how many items have been selected*/
    public void updateToolbar(int listSize) {
        //int selectedItemsCounter = selectedItemIdList.size();
        /*show the title (X) and the counter if there are selected items*/
        if (listSize > 0) {
            /*show the text views*/
            txtToolbarCounter.setVisibility(View.VISIBLE);
            //txtToolbarTitle.setVisibility(View.VISIBLE);
            /*set the counter*/
            txtToolbarCounter.setText(String.valueOf(listSize));
        }
    }

    // Broadcast receiver for receiving updates from the IntentService
    private class ResponseReceiver extends BroadcastReceiver {
        // Prevents instantiation
        private ResponseReceiver() {
        }
        // Called when the BroadcastReceiver gets an Intent it's registered to receive

        @Override
        public void onReceive(Context context, Intent intent) {
        /*
         * Handle Intents here.
         */
            Timber.d("on receive...");
            switch (intent.getAction()) {
                case Constants.BROADCAST_ACTION_RSS_CHANNEL_SAVED: {
                    SourcesFragment sourcesFragment = (SourcesFragment) getSupportFragmentManager().findFragmentByTag(viewPagerAdapter.fragmentTags.get(FragmentTitle.SOURCES));
                    if (sourcesFragment != null)
                        sourcesFragment.displaySnackBarSavedRssChannel();
                    break;
                }
                case Constants.BROADCAST_ACTION_FEEDS_FETCHED: {
                    Bundle bundle = intent.getExtras();

                    @SuppressWarnings("unchecked")
                    ArrayList<FeedItemExtended> feedItems = (ArrayList<FeedItemExtended>) bundle.getSerializable(Constants.EXTENDED_DATA_FEED_ITEM_LIST);

                    if (feedItems != null)
                        Timber.d("size of fetched list of feeditems:%d, notifying FeedsFragment...",feedItems.size());
                    else
                        Timber.e("feedItems is null!!!");

                    FeedsFragment feedsFragment = (FeedsFragment) getSupportFragmentManager().findFragmentByTag(viewPagerAdapter.fragmentTags.get(FragmentTitle.FEEDS));

                    if (feedsFragment == null)
                        return;

                    feedsFragment.updateFeedItemsList(feedItems);
                    break;
                }
            }
        }
    }
}
