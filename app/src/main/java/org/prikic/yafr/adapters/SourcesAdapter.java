package org.prikic.yafr.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.prikic.yafr.R;
import org.prikic.yafr.model.RssChannel;

import java.util.List;

public class SourcesAdapter extends RecyclerView.Adapter<SourcesAdapter.ViewHolder> {
    private List<RssChannel> rssChannelList;

    public void setRssChannelList(List<RssChannel> rssChannelList) {
        this.rssChannelList = rssChannelList;
    }

    public void addRssChannelToChannelList(RssChannel rssChannel) {
        rssChannelList.add(rssChannel);
        notifyDataSetChanged();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.txt_source_name);
        }
    }

    public SourcesAdapter(List<RssChannel> rssChannelList) {
        this.rssChannelList = rssChannelList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SourcesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sources_recycler_view_row, parent, false);

        //TextView textView = (TextView) v.findViewById(R.id.text_view_test_id);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(rssChannelList.get(position).getName());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return rssChannelList.size();
    }

}
