package org.prikic.yafr.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import org.prikic.yafr.R;
import org.prikic.yafr.util.Constants;

import timber.log.Timber;

public class FeedDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        String feedItemURL = (String) bundle.get(Constants.INTENT_FEED_LINK);

        Timber.d("onCreate:%s", feedItemURL);

        setContentView(R.layout.feed_details);

        WebView webView = (WebView) findViewById(R.id.feed_item_web_view);

        if (webView != null)
            webView.loadUrl(feedItemURL);

    }
}
