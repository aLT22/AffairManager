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
import com.bytebuilding.affairmanager.model.Separator;

import java.util.ArrayList;
import java.util.Calendar;
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
        offlineNotificationHelper.doneAlarm(affair.getTimestamp());
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

    @Override
    public void addAffair(Affair affair, boolean isSaveToDB) {
        int position = -1;
        Separator separator = null;

        for (int i = 0; i < adapter.getItemCount(); i ++) {
            if (adapter.getItem(i).isAffair()) {
                Affair task = (Affair) adapter.getItem(i);
                if (affair.getDate() < task.getDate()) {
                    position = i;
                    break;
                }
            }
        }

        if (affair.getDate() != 0) {
            Calendar calendar = Calendar.getInstance();

            calendar.setTimeInMillis(affair.getDate());

            if (calendar.get(Calendar.DAY_OF_YEAR) < Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) {
                affair.setAffairDateStatus(Separator.SEPARATOR_TYPE_LOST);
                if (!adapter.separatorLost) {
                    adapter.separatorLost = true;
                    separator = new Separator(Separator.SEPARATOR_TYPE_LOST);
                }
            } else if (calendar.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) {
                affair.setAffairDateStatus(Separator.SEPARATOR_TYPE_NOW);
                if (!adapter.separatorNow) {
                    adapter.separatorNow = true;
                    separator = new Separator(Separator.SEPARATOR_TYPE_NOW);
                }
            } else if (calendar.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) {
                affair.setAffairDateStatus(Separator.SEPARATOR_TYPE_TOMORROW);
                if (!adapter.separatorTomorrow) {
                    adapter.separatorTomorrow = true;
                    separator = new Separator(Separator.SEPARATOR_TYPE_TOMORROW);
                }
            } else if (calendar.get(Calendar.DAY_OF_YEAR) > Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) {
                affair.setAffairDateStatus(Separator.SEPARATOR_TYPE_FUTURE);
                if (!adapter.separatorFuture) {
                    adapter.separatorFuture = true;
                    separator = new Separator(Separator.SEPARATOR_TYPE_FUTURE);
                }
            }
        }

        if (position != -1) {
            if (!adapter.getItem(position - 1).isAffair()) {
                if (position - 2 >= 0 && adapter.getItem(position - 2).isAffair()) {
                    Affair previousAffair = (Affair) adapter.getItem(position - 2);
                    if (affair.getAffairDateStatus() == previousAffair.getAffairDateStatus()) {
                        position -= 1;
                    }
                } else if (position - 2 < 0 && affair.getDate() == 0) {
                    position -= 1;
                }
            }

            adapter.addItem(position, affair);
        } else {
            if (separator != null) {
                adapter.addItem(separator);
            }
            adapter.addItem(affair);
        }

        if (isSaveToDB) {
            mainOfflineActivity.dbHelper.saveAffair(affair);
        }
    }

}
