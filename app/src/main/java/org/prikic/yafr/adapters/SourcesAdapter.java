package org.prikic.yafr.adapters;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import org.prikic.yafr.R;
import org.prikic.yafr.fragments.SaveOrEditChannelFragment;
import org.prikic.yafr.fragments.SourcesFragment;
import org.prikic.yafr.background.ChannelOperationAsyncTask;
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

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener{

        TextView txtSourceName, txtSourceURL;
        ImageView btnShowSourceDetails;
        Switch switchChannel;
        ImageButton rowSelection;
        LinearLayout nonTextualComponents;

        ViewHolder(View view) {
            super(view);
            txtSourceName = (TextView) view.findViewById(R.id.txt_source_name);
            txtSourceURL = (TextView) view.findViewById(R.id.txt_source_url);
            btnShowSourceDetails = (ImageView) view.findViewById(R.id.btn_show_source_details);
            switchChannel = (Switch) view.findViewById(R.id.switchChannel);
            rowSelection = (ImageButton) view.findViewById(R.id.img_btn_row_selection);
            nonTextualComponents = (LinearLayout) view.findViewById(R.id.non_textual_components);

            btnShowSourceDetails.setOnClickListener(this);
            switchChannel.setOnCheckedChangeListener(this);
        }

        @Override
        public void onClick(View v) {

            if(v.getId() == btnShowSourceDetails.getId()) {

                int clickedItemPosition = getAdapterPosition();

                Timber.d("open source details for:%s %s at position %d", txtSourceName.getText(), txtSourceURL.getText(), clickedItemPosition);

                RssChannel rssChannel = rssChannelList.get(clickedItemPosition);

                FragmentActivity activity = SourcesAdapter.this.fragmentActivity;
                SaveOrEditChannelFragment fragment = SaveOrEditChannelFragment.newInstance(rssChannel, RssChannelOperation.EDIT);
                fragment.show(activity.getSupportFragmentManager(), "SaveOrEditChannelFragment");

            }
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int itemPosition = getAdapterPosition();
            RssChannel rssChannel = rssChannelList.get(itemPosition);
            Timber.d("%s at position %d is %b", rssChannel, itemPosition, isChecked);
            rssChannel.setChannelActive(isChecked);
            new ChannelOperationAsyncTask(rssChannel, RssChannelOperation.UPDATE_ACTIVE_FLAG, fragmentActivity).execute();
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
        RssChannel rssChannel = rssChannelList.get(position);
        holder.txtSourceName.setText(rssChannel.getName());
        holder.txtSourceURL.setText(rssChannel.getUrl());
        holder.switchChannel.setChecked(rssChannel.isChannelActive());

        /*un select all items by default*/
        setItemUnSelected(holder);

        /*change the colour if the itemTextView is selected
        * -first check if selected list exists and has at least 1 item*/
        if (SourcesFragment.selectedItemIdList != null
                && SourcesFragment.selectedItemIdList.size() > 0) {
            /*we are now in choice mode, so make the user aware
            of this by displaying additional view for row_selected/row_not_selected image (*) */
            setViewIndicatingChoiceMode(holder);
            /*check if the view we're about to display is in the selected list*/
            boolean isItemIdInList
                    = isItemIdInSelectedList(rssChannelList.get(position).getId());
            if (isItemIdInList) {
                /*this item is selected so change the row_not_selected
                to indicate that it's selected*/
                setItemSelected(holder);
            }
        } else {
            /*we are now in normal mode so make the user aware of
            this by removing the additional view at the row beginning*/
            setViewIndicatingNormalMode(holder);
        }
    }

    /*indicate to user that they are in choice mode*/
    private void setViewIndicatingChoiceMode(ViewHolder holder) {
        /*show the row for action mode*/
        holder.rowSelection.setVisibility(View.VISIBLE);
        holder.nonTextualComponents.setVisibility(View.GONE);
    }

    /*indicate to user that they are in normal mode now*/
    private void setViewIndicatingNormalMode(ViewHolder holder) {
        /*hide the row for action mode*/
        holder.rowSelection.setVisibility(View.GONE);
        holder.nonTextualComponents.setVisibility(View.VISIBLE);
    }

    /*set the item to not_selected*/
    private void setItemUnSelected(ViewHolder holder) {
        /*set rowSelection to not selected*/
        holder.rowSelection.setImageResource(R.drawable.row_not_selected);
    }

    /*set the item to selected*/
    private void setItemSelected(ViewHolder holder) {
        /*set rowSelection to selected*/
        holder.rowSelection.setImageResource(R.drawable.row_selected);

    }

    /*check if the given id is in the list of selected ids*/
    private boolean isItemIdInSelectedList(Long itemId) {
        return SourcesFragment.selectedItemIdList.contains(itemId);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return rssChannelList.size();
    }

}
