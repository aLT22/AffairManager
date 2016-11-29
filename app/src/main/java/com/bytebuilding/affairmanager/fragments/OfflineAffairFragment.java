package com.bytebuilding.affairmanager.fragments;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

import com.bytebuilding.affairmanager.adapters.AffairAdapter;
import com.bytebuilding.affairmanager.model.Affair;

/**
 * Created by turki on 27.11.2016.
 */

public abstract class OfflineAffairFragment extends Fragment{

    protected RecyclerView recyclerView;

    protected RecyclerView.LayoutManager layoutManager;

    protected AffairAdapter adapter;

    public void addAffair(Affair affair) {
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
    }

    public abstract void moveAffair(Affair affair);

}
