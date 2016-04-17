package org.prikic.yafr.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import org.prikic.yafr.R;

import timber.log.Timber;

public class SaveOrEditChannelFragment extends DialogFragment {

    private String fragmentTitle;

    public void setFragmentTitle(String fragmentTitle) {
        this.fragmentTitle = fragmentTitle;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_create_or_edit_source, container, false);

        TextView title = (TextView) view.findViewById(R.id.dialog_create_or_edit_source_title);
        title.setText(fragmentTitle);


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
                Timber.d("saving RSS channel");
                SaveOrEditChannelFragment.this.dismiss();
            }
        });

        return view;

    }
}
