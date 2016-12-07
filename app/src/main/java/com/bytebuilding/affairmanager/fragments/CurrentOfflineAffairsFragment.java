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
import com.bytebuilding.affairmanager.adapters.CurrentOfflineAffairsAdapter;
import com.bytebuilding.affairmanager.database.DBHelper;
import com.bytebuilding.affairmanager.model.Affair;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public void addAffairFromDB() {
        adapter.removeAllItems();
        List<Affair> affairs = new ArrayList<>();
        affairs.addAll(mainOfflineActivity.dbHelper.getDbQueryManager().getAffairs(DBHelper.SELECTION_BY_STATUS
                + " OR " + DBHelper.SELECTION_BY_STATUS, new String[]{Integer.toString(Affair.STATUS_CURRENT),
                Integer.toString(Affair.STATUS_OVERDUE)}, DBHelper.COLOUMN_DATE));
        for (int i = 0; i < affairs.size(); i++) {
            addAffair(affairs.get(i), false);
        }
    }

    @Override
    public void findAffairsByTitle(String title) {
        adapter.removeAllItems();
        List<Affair> affairs = new ArrayList<>();
        affairs.addAll(mainOfflineActivity.dbHelper.getDbQueryManager().getAffairs(DBHelper.LIKE_SELECTION_BY_TITLE +
                " AND " + DBHelper.SELECTION_BY_STATUS + " OR " + DBHelper.SELECTION_BY_STATUS, new String[]{"%" +
                title + "%", Integer.toString(Affair.STATUS_CURRENT), Integer.toString(Affair.STATUS_OVERDUE)}, DBHelper
                .COLOUMN_DATE));
        for (int i = 0; i < affairs.size(); i++) {
            addAffair(affairs.get(i), false);
        }
    }

}
