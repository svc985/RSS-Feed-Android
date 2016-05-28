package org.prikic.yafr.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.prikic.yafr.R;
import org.prikic.yafr.model.FeedItemExtended;
import org.prikic.yafr.util.Constants;
import org.prikic.yafr.util.Util;

import timber.log.Timber;

public class FeedDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        Timber.d("opening Feed details");

        super.onCreate(savedInstanceState);

        setContentView(R.layout.feed_details);

        Bundle bundle = getIntent().getExtras();
        FeedItemExtended feedItem = (FeedItemExtended) bundle.get(Constants.INTENT_FEED_ITEM);

        TextView feedDetailsTitle = (TextView) findViewById(R.id.feed_details_title);
        TextView feedDetailsDescription = (TextView) findViewById(R.id.feed_details_description);
        TextView feedDetailsPubDate = (TextView) findViewById(R.id.feed_details_published_pub_date);

        ImageView feedDetailsSourceImage = (ImageView) findViewById(R.id.feed_details_source_image);
        Picasso.with(this)
                .load(feedItem.getSource_image_url())
                .resize(50, 30)
                .centerCrop()
                .into(feedDetailsSourceImage);

        WebView webView = (WebView) findViewById(R.id.feed_item_web_view);

        if (webView != null && feedItem != null) {
            webView.loadUrl(feedItem.getLink());
            feedDetailsTitle.setText(Html.fromHtml(feedItem.getTitle()));
            feedDetailsDescription.setText(feedItem.getDescription());
            String pubDate = Util.parseDate(feedItem.getPubDate());
            feedDetailsPubDate.setText(pubDate);

        }

    }
}
