package com.bytebuilding.affairmanager.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bytebuilding.affairmanager.R;
import com.bytebuilding.affairmanager.adapters.CurrentOfflineAffairsAdapter;
import com.bytebuilding.affairmanager.model.Affair;

public class CurrentOfflineAffairsFragment extends OfflineAffairFragment {

    OnAffairDoneListener onAffairDoneListener;

    public CurrentOfflineAffairsFragment() {
    }

    public interface OnAffairDoneListener {
        void onAffairDone(Affair affair);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            onAffairDoneListener = (OnAffairDoneListener) activity;
        } catch (ClassCastException cce) {
            throw new ClassCastException(activity.toString() + " must implement OnAffairDoneListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_current_offline_affairs, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_current_offline_affairs);

        layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);

        adapter = new CurrentOfflineAffairsAdapter(this);

        recyclerView.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void moveAffair(Affair affair) {
        onAffairDoneListener.onAffairDone(affair);
    }

}
