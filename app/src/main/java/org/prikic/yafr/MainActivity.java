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

import org.prikic.yafr.activities.AboutActivity;
import org.prikic.yafr.activities.FavoritesFragment;
import org.prikic.yafr.activities.FeedsFragment;
import org.prikic.yafr.activities.SaveOrEditChannelFragment;
import org.prikic.yafr.activities.SourcesFragment;
import org.prikic.yafr.background.SourceSaveOrUpdateAsyncTask;
import org.prikic.yafr.model.RssChannel;
import org.prikic.yafr.util.RssChannelOperation;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity
        implements SaveOrEditChannelFragment.OnRssChannelSavedListener {

    ViewPagerAdapter viewPagerAdapter;

    private boolean showProgressSpinnerInToolbar = false;

    public void setShowProgressSpinnerInToolbar(boolean showProgressSpinnerInToolbar) {
        this.showProgressSpinnerInToolbar = showProgressSpinnerInToolbar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        if (tabLayout != null)
        tabLayout.setupWithViewPager(viewPager);

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
        if (!showProgressSpinnerInToolbar) {
            progressSpinner.setVisible(false);
        }
        else {
            progressSpinner.setVisible(true);
        }

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
    public void onRssChannelSaved(RssChannel rssChannel, RssChannelOperation operation, int clickedItemPosition) {
        Timber.d("saving Rss channel in db...");

        new SourceSaveOrUpdateAsyncTask(rssChannel, operation, this).execute();

        SourcesFragment sourcesFragment = (SourcesFragment) viewPagerAdapter.mFragmentList.get(2);
        sourcesFragment.displaySnackbarFor(rssChannel, operation, clickedItemPosition);
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

}
