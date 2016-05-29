package org.prikic.yafr.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
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
        FeedItemExtended feedItem = (FeedItemExtended) bundle.getSerializable(Constants.INTENT_FEED_ITEM);

        ImageView feedDetailsSourceImage = (ImageView) findViewById(R.id.feed_details_source_image);
        Picasso.with(this)
                .load(feedItem.getSourceImageUrl())
                .resize(60, 30)
                .error(R.mipmap.ic_launcher)
                .placeholder(R.drawable.progress_animation)
                .into(feedDetailsSourceImage);

        final ImageView favoriteSelectionImage = (ImageView) findViewById(R.id.favorite_selection_image);
        if (favoriteSelectionImage != null) {
            favoriteSelectionImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String imageTag = (String) favoriteSelectionImage.getTag();
                    if (imageTag.equals("notSelected")) {
                        Timber.d("saving feed item to favorites...");
                        favoriteSelectionImage.setImageResource(R.drawable.favorite_selected);
                        favoriteSelectionImage.setTag("Selected");
                    } else {
                        Timber.d("removing feed item from favorites...");
                        favoriteSelectionImage.setImageResource(R.drawable.favorite_not_selected);
                        favoriteSelectionImage.setTag("notSelected");
                    }
                }
            });
        }

        WebView webView = (WebView) findViewById(R.id.feed_item_web_view);

        webView.loadUrl(feedItem.getLink());

        TextView feedDetailsTitle = (TextView) findViewById(R.id.feed_details_title);
        TextView feedDetailsDescription = (TextView) findViewById(R.id.feed_details_description);
        TextView feedDetailsPubDate = (TextView) findViewById(R.id.feed_details_published_pub_date);

        feedDetailsTitle.setText(Html.fromHtml(feedItem.getTitle()));
        feedDetailsDescription.setText(Html.fromHtml(feedItem.getDescription()));
        String pubDate = Util.parseDate(feedItem.getPubDate());
        feedDetailsPubDate.setText(pubDate);

    }
}
