package com.bytebuilding.affairmanager.fragments;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bytebuilding.affairmanager.R;

import butterknife.ButterKnife;

public class DoneOfflineAffairsFragment extends OfflineAffairFragment {

    public DoneOfflineAffairsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_done_offline_affairs, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_done_offline_affairs);

        layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);
        return rootView;
    }

}
