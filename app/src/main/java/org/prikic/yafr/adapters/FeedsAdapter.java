package org.prikic.yafr.adapters;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.prikic.yafr.R;
import org.prikic.yafr.model.xmlService.FeedItem;

import java.util.List;

public class FeedsAdapter extends RecyclerView.Adapter<FeedsAdapter.ViewHolder> {

    private FragmentActivity fragmentActivity;
    private List<FeedItem> rssFeedsList;

    public FeedsAdapter(FragmentActivity fragmentActivity, List<FeedItem> rssFeedsList) {
        this.fragmentActivity = fragmentActivity;
        this.rssFeedsList = rssFeedsList;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtTitle, txtPubDate;

        public ViewHolder(View view) {
            super(view);

            txtTitle = (TextView) view.findViewById(R.id.txt_title);
            txtPubDate = (TextView) view.findViewById(R.id.txt_pub_date);
        }
    }

    @Override
    public FeedsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feeds_recycler_view_row, parent, false);

        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FeedsAdapter.ViewHolder holder, int position) {

        FeedItem feedItem = rssFeedsList.get(position);
        holder.txtTitle.setText(feedItem.getTitle());
        holder.txtPubDate.setText(feedItem.getPubDate());

    }

    @Override
    public int getItemCount() {
        return rssFeedsList.size();
    }
}
