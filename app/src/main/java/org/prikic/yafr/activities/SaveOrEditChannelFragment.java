package org.prikic.yafr.activities;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.prikic.yafr.R;
import org.prikic.yafr.model.RssChannel;

import timber.log.Timber;

public class SaveOrEditChannelFragment extends DialogFragment {

    private EditText editTxtName, editTxtUrl;

    private String fragmentTitle;

    private OnRssChannelSavedListener mListener;

    public void setFragmentTitle(String fragmentTitle) {
        this.fragmentTitle = fragmentTitle;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnRssChannelSavedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnArticleSelectedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_create_or_edit_source, container, false);

        TextView title = (TextView) view.findViewById(R.id.dialog_create_or_edit_source_title);
        title.setText(fragmentTitle);

        editTxtName = (EditText) view.findViewById(R.id.dialog_create_or_edit_source_name);
        editTxtUrl = (EditText) view.findViewById(R.id.dialog_create_or_edit_source_url);

        Button btnCancel = (Button) view.findViewById(R.id.btnCancelSourceDialog);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveOrEditChannelFragment.this.dismiss();
            }
        });

        Button btnSave = (Button) view.findViewById(R.id.btnSaveSourceDialog);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timber.d("notifying SourcesFragment to save Rss channel...");

                //get channel data from dialog fragment
                RssChannel rssChannel = getRssChannelData();
                mListener.onRssChannelSaved(rssChannel);
                SaveOrEditChannelFragment.this.dismiss();
            }
        });

        return view;

    }

    /**
     * Gets Rss Channel data, name and url, that user provided as input
     * and returns it to the caller
     * @return RssChannel
     */
    private RssChannel getRssChannelData() {

        String name = editTxtName.getText().toString();
        String url = editTxtUrl.getText().toString();

        return new RssChannel(name, url);

    }

    public interface OnRssChannelSavedListener {
        void onRssChannelSaved(RssChannel rssChannel);
    }
}
