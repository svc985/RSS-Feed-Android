package org.prikic.yafr.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.prikic.yafr.R;
import org.prikic.yafr.adapters.FeedsAdapter;
import org.prikic.yafr.util.Util;

import java.util.List;

public class FeedsFragment extends Fragment {

    public FeedsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.feeds, container, false);

        final RecyclerView recyclerView = (RecyclerView) fragmentView.findViewById(R.id.recycler_view_feeds_fragment);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        //TODO test list
        List<Object> rssFeedsList = Util.getDummyList();

        //recycler view adapter
        RecyclerView.Adapter adapter = new FeedsAdapter(getActivity(), rssFeedsList);
        recyclerView.setAdapter(adapter);

        return fragmentView;
    }
}
