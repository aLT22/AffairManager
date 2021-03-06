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
import com.bytebuilding.affairmanager.database.DBHelper;
import com.bytebuilding.affairmanager.model.Affair;

import java.util.ArrayList;
import java.util.List;

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
        if (affair.getDate() != 0) {
            offlineNotificationHelper.setReceiver(affair);
        }
        onAffairRestoreListener.onAffairRestore(affair);
    }

    @Override
    public void addAffairFromDB() {
        adapter.removeAllItems();
        List<Affair> affairs = new ArrayList<>();
        affairs.addAll(mainOfflineActivity.dbHelper.getDbQueryManager().getAffairs(DBHelper.SELECTION_BY_STATUS,
                new String[]{Integer.toString(Affair.STATUS_DONE)}, DBHelper.COLOUMN_DATE));
        for (int i = 0; i < affairs.size(); i++) {
            addAffair(affairs.get(i), false);
        }
    }

    @Override
    public void findAffairsByTitle(String title) {
        adapter.removeAllItems();
        List<Affair> affairs = new ArrayList<>();
        affairs.addAll(mainOfflineActivity.dbHelper.getDbQueryManager().getAffairs(DBHelper.LIKE_SELECTION_BY_TITLE +
                " AND " + DBHelper.SELECTION_BY_STATUS, new String[]{"%" + title + "%", Integer.toString(Affair
                .STATUS_DONE)}, DBHelper.COLOUMN_DATE));
        for (int i = 0; i < affairs.size(); i++) {
            addAffair(affairs.get(i), false);
        }
    }

    @Override
    public void addAffair(Affair affair, boolean isSaveToDB) {
        int position = -1;

        for (int i = 0; i < adapter.getItemCount(); i ++) {
            if (adapter.getItem(i).isAffair()) {
                Affair task = (Affair) adapter.getItem(i);
                if (affair.getDate() < task.getDate()) {
                    position = i;
                    break;
                }
            }
        }

        if (position != -1) {
            adapter.addItem(position, affair);
        } else {
            adapter.addItem(affair);
        }

        if (isSaveToDB) {
            mainOfflineActivity.dbHelper.saveAffair(affair);
        }
    }
}
