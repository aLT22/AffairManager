package com.bytebuilding.affairmanager.fragments;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

import com.bytebuilding.affairmanager.R;
import com.bytebuilding.affairmanager.adapters.CurrentOfflineAffairsAdapter;
import com.bytebuilding.affairmanager.model.Affair;

import butterknife.BindView;

/**
 * Created by turki on 27.11.2016.
 */

public abstract class OfflineAffairFragment extends Fragment{

    @BindView(R.id.rv_current_offline_affairs)
    RecyclerView recyclerView;

    protected RecyclerView.LayoutManager layoutManager;

    protected CurrentOfflineAffairsAdapter currentOfflineAffairsAdapter;

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

}
