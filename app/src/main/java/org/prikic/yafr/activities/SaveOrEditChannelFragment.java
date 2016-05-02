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

    private int clickedItemPosition;

    private RssChannel rssChannel;

    private RssChannelOperation operation;

    private OnRssChannelOperationListener mListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        operation = (RssChannelOperation) getArguments().getSerializable("operation");
        rssChannel = (RssChannel) getArguments().getSerializable("rssChannel");
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
            mListener = (OnRssChannelOperationListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnRssChannelOperationListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_create_or_edit_source, container, false);

        TextView title = (TextView) view.findViewById(R.id.dialog_create_or_edit_source_title);

        String fragmentTitle = getFragmentTitle();
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

                rssChannel.setName(editTxtName.getText().toString());
                rssChannel.setUrl(editTxtUrl.getText().toString());

                if (operation == RssChannelOperation.SAVE) {
                    mListener.onRssChannelSaved(rssChannel);
                }
                else {
                    mListener.onRssChannelEdited(rssChannel, clickedItemPosition);
                }

                SaveOrEditChannelFragment.this.dismiss();
            }
        });

        if (operation == RssChannelOperation.EDIT) {
            editTxtName.setText(rssChannel.getName());
            editTxtUrl.setText(rssChannel.getUrl());
            btnSave.setText(getResources().getText(R.string.edit));
        }

        return view;
    }

    private String getFragmentTitle() {

        switch (operation) {
            case SAVE:
                return getResources().getString(R.string.save_new_rss_source);
            case EDIT:
                return getResources().getString(R.string.edit_rss_source);
            default:
                return getResources().getString(R.string.lorem_ipsum_short);
        }
    }

    public interface OnRssChannelOperationListener {
        void onRssChannelSaved(RssChannel rssChannel);
        void onRssChannelEdited(RssChannel rssChannel, int clickedItemPosition);
    }

    public static SaveOrEditChannelFragment newInstance(RssChannel rssChannel, RssChannelOperation operation, int clickedItemPosition) {
        SaveOrEditChannelFragment myFragment = new SaveOrEditChannelFragment();

        Bundle args = new Bundle();
        args.putSerializable("rssChannel", rssChannel);
        args.putSerializable("operation", operation);
        args.putInt("clickedItemPosition", clickedItemPosition);
        myFragment.setArguments(args);

        return myFragment;
    }

}
