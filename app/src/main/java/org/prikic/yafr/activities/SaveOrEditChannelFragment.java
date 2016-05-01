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
import org.prikic.yafr.util.RssChannelOperation;

import timber.log.Timber;

public class SaveOrEditChannelFragment extends DialogFragment {

    private EditText editTxtName, editTxtUrl;

    private String fragmentTitle;

    private int clickedItemPosition;

    private RssChannel rssChannel;

    private RssChannelOperation operation;

    private OnRssChannelSavedListener mListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentTitle = getArguments().getString("fragmentTitle", "Error");
        rssChannel = (RssChannel) getArguments().getSerializable("rssChannel");
        operation = (RssChannelOperation) getArguments().getSerializable("operation");
        clickedItemPosition = getArguments().getInt("clickedItemPosition");
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

        final Button btnSave = (Button) view.findViewById(R.id.btnSaveSourceDialog);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timber.d("notifying SourcesFragment to save/edit Rss channel...");

                long id = 0;
                if (operation == RssChannelOperation.EDIT) {
                    id = rssChannel.getId();
                }
                //get channel data from dialog fragment
                RssChannel rssChannel = getRssChannelData();

                if (operation == RssChannelOperation.EDIT) {
                    rssChannel.setId(id);
                }

                mListener.onRssChannelSaved(rssChannel, operation, clickedItemPosition);
                SaveOrEditChannelFragment.this.dismiss();
            }
        });

        if (rssChannel != null) {
            editTxtName.setText(rssChannel.getName());
            editTxtName.setSelection(rssChannel.getName().length());
            editTxtUrl.setText(rssChannel.getUrl());
            btnSave.setText(getResources().getText(R.string.edit));
        }

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
        void onRssChannelSaved(RssChannel rssChannel, RssChannelOperation operation, int clickedItemPosition);
    }

    public static SaveOrEditChannelFragment newInstance(String fragmentTitle, RssChannel rssChannel,
                                                        RssChannelOperation operation, int clickedItemPosition) {
        SaveOrEditChannelFragment myFragment = new SaveOrEditChannelFragment();

        Bundle args = new Bundle();
        args.putString("fragmentTitle", fragmentTitle);
        args.putSerializable("rssChannel", rssChannel);
        args.putSerializable("operation", operation);
        args.putInt("clickedItemPosition", clickedItemPosition);
        myFragment.setArguments(args);

        return myFragment;
    }

}
