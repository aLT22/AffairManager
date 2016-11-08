package com.bytebuilding.affairmanager.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bytebuilding.affairmanager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CurrentOfflineAffairsFragment extends Fragment {

    @BindView(R.id.rv_current_offline_affairs)
    RecyclerView rvCurrentOfflineAffairs;

    private RecyclerView.LayoutManager layoutManager;

    public CurrentOfflineAffairsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_current_offline_affairs, container, false);

        ButterKnife.bind(this, rootView);

        layoutManager = new LinearLayoutManager(getActivity());

        rvCurrentOfflineAffairs.setLayoutManager(layoutManager);
        return rootView;
    }

}
