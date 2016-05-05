package org.prikic.yafr;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.prikic.yafr.activities.AboutActivity;
import org.prikic.yafr.activities.FavoritesFragment;
import org.prikic.yafr.activities.FeedsFragment;
import org.prikic.yafr.activities.SaveOrEditChannelFragment;
import org.prikic.yafr.activities.SourcesFragment;
import org.prikic.yafr.background.ChannelOperationAsyncTask;
import org.prikic.yafr.model.RssChannel;
import org.prikic.yafr.util.RssChannelOperation;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity
        implements SaveOrEditChannelFragment.OnRssChannelOperationListener {

    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;

    private boolean showProgressSpinnerInToolbar = false;

    TextView txtToolbarCounter;
    Toolbar toolbar;
    ImageView imageIconActionToolbar;

    public void setShowProgressSpinnerInToolbar(boolean showProgressSpinnerInToolbar) {
        this.showProgressSpinnerInToolbar = showProgressSpinnerInToolbar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
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

                    Fragment fragment = getActiveFragment();
                    if (fragment instanceof SourcesFragment) {
                        ((SourcesFragment) fragment).cancelSelections();
                        showNormalToolbar();
                    }
                }
            }
        });
    }

    private Fragment getActiveFragment() {
        int currentFragmentPosition = 0;
        if (viewPager != null)
            currentFragmentPosition = viewPager.getCurrentItem();
        return viewPagerAdapter.getItem(currentFragmentPosition);
    }

    private void setupViewPager(ViewPager viewPager) {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new FeedsFragment(), "Feeds");
        viewPagerAdapter.addFragment(new FavoritesFragment(), "Favorites");
        viewPagerAdapter.addFragment(new SourcesFragment(), "Sources");
        viewPager.setAdapter(viewPagerAdapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.about_menu_item) {
            Timber.d("opening about screen...");
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRssChannelSaved(RssChannel rssChannel) {
        Timber.d("saving Rss channel in db...");

        new ChannelOperationAsyncTask(rssChannel, RssChannelOperation.SAVE, this).execute();

        //TODO fixing??
        SourcesFragment sourcesFragment = (SourcesFragment) viewPagerAdapter.mFragmentList.get(2);
        sourcesFragment.displaySnackBarSavedRssChannel(rssChannel);
    }

    @Override
    public void onRssChannelEdited(RssChannel rssChannel, int clickedItemPosition) {
        Timber.d("editing Rss channel in db on position %d", clickedItemPosition);

        new ChannelOperationAsyncTask(rssChannel, RssChannelOperation.EDIT, this).execute();

        SourcesFragment sourcesFragment = (SourcesFragment) viewPagerAdapter.mFragmentList.get(2);
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
}
