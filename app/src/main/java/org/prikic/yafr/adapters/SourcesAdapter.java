package org.prikic.yafr.adapters;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.prikic.yafr.R;
import org.prikic.yafr.activities.SaveOrEditChannelFragment;
import org.prikic.yafr.model.RssChannel;
import org.prikic.yafr.util.RssChannelOperation;

import java.util.List;

import timber.log.Timber;

public class SourcesAdapter extends RecyclerView.Adapter<SourcesAdapter.ViewHolder> {

    private FragmentActivity fragmentActivity;
    private List<RssChannel> rssChannelList;

    public SourcesAdapter(FragmentActivity fragmentActivity, List<RssChannel> rssChannelList) {
        this.fragmentActivity = fragmentActivity;
        this.rssChannelList = rssChannelList;
    }

    public void setRssChannelList(List<RssChannel> rssChannelList) {
        this.rssChannelList = rssChannelList;
    }

    public void addRssChannelToChannelList(RssChannel rssChannel) {
        rssChannelList.add(rssChannel);
        notifyDataSetChanged();
    }

    public void updateRssChannelListAtPosition(RssChannel rssChannel, int position) {
        rssChannelList.set(position, rssChannel);
        notifyDataSetChanged();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView txtSourceName, txtSourceURL;
        public ImageView btnShowSourceDetails;

        public ViewHolder(View view) {
            super(view);
            txtSourceName = (TextView) view.findViewById(R.id.txt_source_name);
            txtSourceURL = (TextView) view.findViewById(R.id.txt_source_url);
            btnShowSourceDetails = (ImageView) view.findViewById(R.id.btn_show_source_details);

            btnShowSourceDetails.setOnClickListener(this);
            view.setOnClickListener(this);
            //TODO add long click listener
        }

        @Override
        public void onClick(View v) {

            if(v.getId() == btnShowSourceDetails.getId()) {

                int clickedItemPosition = getAdapterPosition();

                Timber.d("open source details for:%s %s at position %d", txtSourceName.getText(), txtSourceURL.getText(), clickedItemPosition);

                RssChannel rssChannel = rssChannelList.get(clickedItemPosition);

                FragmentActivity activity = SourcesAdapter.this.fragmentActivity;
                SaveOrEditChannelFragment fragment = SaveOrEditChannelFragment.newInstance(rssChannel, RssChannelOperation.EDIT, clickedItemPosition);
                fragment.show(activity.getSupportFragmentManager(), "SaveOrEditChannelFragment");

            }
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SourcesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sources_recycler_view_row, parent, false);

        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.txtSourceName.setText(rssChannelList.get(position).getName());
        holder.txtSourceURL.setText(rssChannelList.get(position).getUrl());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return rssChannelList.size();
    }

}
