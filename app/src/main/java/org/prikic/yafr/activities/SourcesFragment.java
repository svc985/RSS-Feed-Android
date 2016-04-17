package org.prikic.yafr.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.prikic.yafr.R;

import timber.log.Timber;

public class SourcesFragment extends Fragment {

    public SourcesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.sources, container, false);

        FloatingActionButton fab = (FloatingActionButton) fragmentView.findViewById(R.id.btn_add_new_source);

        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Timber.d("open add sources fragment/activity");
                    SaveOrEditChannelFragment fragment = new SaveOrEditChannelFragment();
                    fragment.setFragmentTitle(getResources().getString(R.string.save_new_rss_source));
                    fragment.show(getActivity().getSupportFragmentManager(), "SaveOrEditChannelFragment");
                }
            });
        }

        return fragmentView;
    }
}
