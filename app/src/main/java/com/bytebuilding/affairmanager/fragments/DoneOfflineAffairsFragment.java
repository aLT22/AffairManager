package com.bytebuilding.affairmanager.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bytebuilding.affairmanager.R;
import com.bytebuilding.affairmanager.adapters.DoneOfflineAffairsAdapter;
import com.bytebuilding.affairmanager.model.Affair;

public class DoneOfflineAffairsFragment extends OfflineAffairFragment {

    OnAffairRestoreListener onAffairRestoreListener;

    public DoneOfflineAffairsFragment() {
    }

    public interface OnAffairRestoreListener {
        void onAffairRestore(Affair affair);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            onAffairRestoreListener = (OnAffairRestoreListener) activity;
        } catch (ClassCastException cce) {
            throw new ClassCastException(activity.toString() + " have to implement OnAffairRestoreListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_done_offline_affairs, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_done_offline_affairs);

        layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);

        adapter = new DoneOfflineAffairsAdapter(this);

        recyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void moveAffair(Affair affair) {
        onAffairRestoreListener.onAffairRestore(affair);
    }

    @Override
    public void addAffairFromDB() {

    }
}
