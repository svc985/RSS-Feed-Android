package org.prikic.yafr.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.TextView;

import org.prikic.yafr.R;
import org.prikic.yafr.model.xmlService.FeedItem;
import org.prikic.yafr.util.Constants;

import timber.log.Timber;

public class FeedDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.feed_details);

        TextView feedDetailsTitle = (TextView) findViewById(R.id.feed_details_title);
        TextView feedDetailsDescription = (TextView) findViewById(R.id.feed_details_description);

        Timber.d("opening Feed details");

        Bundle bundle = getIntent().getExtras();
        FeedItem feedItem = (FeedItem) bundle.get(Constants.INTENT_FEED_ITEM);

        WebView webView = (WebView) findViewById(R.id.feed_item_web_view);

        if (webView != null && feedItem != null) {
            webView.loadUrl(feedItem.getLink());
            feedDetailsTitle.setText(feedItem.getTitle());
            feedDetailsDescription.setText(feedItem.getDescription());
        }

    }
}
