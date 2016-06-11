package org.prikic.yafr.adapters;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.prikic.yafr.R;
import org.prikic.yafr.activities.FeedDetailsActivity;
import org.prikic.yafr.model.FeedItemExtended;
import org.prikic.yafr.util.Constants;
import org.prikic.yafr.util.Util;

import java.util.List;

import timber.log.Timber;

public class FeedsAdapter extends RecyclerView.Adapter<FeedsAdapter.ViewHolder> {

    private FragmentActivity fragmentActivity;
    private List<FeedItemExtended> rssFeedsList;

    public FeedsAdapter(FragmentActivity fragmentActivity, List<FeedItemExtended> rssFeedsList) {
        this.fragmentActivity = fragmentActivity;
        this.rssFeedsList = rssFeedsList;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle, txtPubDate;
        ImageView imgFeedItemDetails, imgChannel;

        ViewHolder(View view) {
            super(view);

            txtTitle = (TextView) view.findViewById(R.id.txt_title);
            txtPubDate = (TextView) view.findViewById(R.id.txt_pub_date);
            imgFeedItemDetails = (ImageView) view.findViewById(R.id.btn_show_feed_details);
            imgChannel = (ImageView) view.findViewById(R.id.feed_image);

            imgFeedItemDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FeedItemExtended feedItem = rssFeedsList.get(getAdapterPosition());
                    Timber.d("opening feed item details:%s", feedItem.getLink());
                    Intent intent = new Intent(fragmentActivity, FeedDetailsActivity.class);
                    intent.putExtra(Constants.INTENT_FEED_ITEM, feedItem);
                    fragmentActivity.startActivity(intent);
                }
            });
        }
    }

    @Override
    public FeedsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feeds_recycler_view_row, parent, false);

        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FeedsAdapter.ViewHolder holder, int position) {

        FeedItemExtended feedItem = rssFeedsList.get(position);
        holder.txtTitle.setText(Html.fromHtml(feedItem.getTitle()));

        String pubDate = feedItem.getPubDate();
        String formatedDate = Util.parseDate(pubDate);
        holder.txtPubDate.setText(formatedDate);

        Picasso.with(fragmentActivity)
                .load(feedItem.getSourceImageUrl())
                .resize(60, 30)
                .error(R.mipmap.ic_launcher)
                .placeholder(R.drawable.progress_animation)
                .into(holder.imgChannel);

    }

    @Override
    public int getItemCount() {
        return rssFeedsList.size();
    }
}
