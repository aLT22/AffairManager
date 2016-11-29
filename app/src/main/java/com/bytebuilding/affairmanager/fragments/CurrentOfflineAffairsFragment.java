package com.bytebuilding.affairmanager.fragments;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bytebuilding.affairmanager.R;
import com.bytebuilding.affairmanager.adapters.CurrentOfflineAffairsAdapter;
import com.bytebuilding.affairmanager.model.Affair;

import butterknife.ButterKnife;

public class CurrentOfflineAffairsFragment extends OfflineAffairFragment {

    public CurrentOfflineAffairsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_current_offline_affairs, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_current_offline_affairs);

        layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);

        currentOfflineAffairsAdapter = new CurrentOfflineAffairsAdapter(this);

        recyclerView.setAdapter(currentOfflineAffairsAdapter);
        return rootView;
    }

    public void addTask(Affair newAffair) {
        int position = -1;

        for (int i = 0; i < currentOfflineAffairsAdapter.getItemCount(); i++) {
            if (currentOfflineAffairsAdapter.getItem(i).isAffair()) {
                Affair affair = (Affair) currentOfflineAffairsAdapter.getItem(i);

                if (newAffair.getTimestamp() < affair.getTimestamp()) position = i;
            }

            if (position != -1) currentOfflineAffairsAdapter.addItem(position, newAffair);
            else currentOfflineAffairsAdapter.addItem(newAffair);
        }
    }

    @Override
    public void moveAffair(Affair affair) {

    }

}
