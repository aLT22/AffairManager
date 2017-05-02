package com.bytebuilding.affairmanager.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.bytebuilding.affairmanager.R;
import com.bytebuilding.affairmanager.activities.DetailAffairActivity;
import com.bytebuilding.affairmanager.activities.MainOfflineActivity;
import com.bytebuilding.affairmanager.adapters.AffairAdapter;
import com.bytebuilding.affairmanager.dialogs.EditAffairDialogFragment;
import com.bytebuilding.affairmanager.model.Affair;
import com.bytebuilding.affairmanager.model.Item;
import com.bytebuilding.affairmanager.notifications.OfflineNotificationHelper;

public abstract class OfflineAffairFragment extends Fragment{

    protected RecyclerView recyclerView;

    protected RecyclerView.LayoutManager layoutManager;

    protected AffairAdapter adapter;

    public MainOfflineActivity mainOfflineActivity;
    public OfflineNotificationHelper offlineNotificationHelper;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() != null) {
            mainOfflineActivity = (MainOfflineActivity) getActivity();
        }

        offlineNotificationHelper = OfflineNotificationHelper.getInstance();

        addAffairFromDB();
    }

    public abstract void addAffair(Affair affair, boolean isSaveToDB);

    public abstract void moveAffair(Affair affair);

    public abstract void addAffairFromDB();

    public abstract void findAffairsByTitle(String title);

    public void deleteDialog(final int position) {
        final AlertDialog.Builder deleteDialog = new AlertDialog
                .Builder(new ContextThemeWrapper(getActivity(), R.style.MyDialogTheme));

        deleteDialog.setMessage(R.string.delete_dialog_message);
        deleteDialog.setIcon(R.drawable.titlecalendar_icon);

        Item item = adapter.getItem(position);

        if (item.isAffair()) {
            Affair affairForDelete = (Affair) item;

            final long timestamp = affairForDelete.getTimestamp();

            deleteDialog.setPositiveButton(R.string.button_accept, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.removeItem(position);

                    mainOfflineActivity.dbHelper.deleteAffair(timestamp);

                    offlineNotificationHelper.doneAlarm(timestamp);

                    Toast.makeText(getActivity().getApplicationContext(), R.string.toast_delete_dialog_accept,
                            Toast.LENGTH_SHORT).show();

                    dialog.dismiss();
                }
            });

            deleteDialog.setNegativeButton(R.string.button_decline, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    Toast.makeText(getActivity().getApplicationContext(), R.string.toast_delete_dialog_cancel,
                            Toast.LENGTH_SHORT).show();
                }
            });
        }

        deleteDialog.show();
    }

    public void updateAffair(Affair affair) {
        adapter.updateAffair(affair);
    }

    public void seeDetails(Affair affair) {
        Bundle innerBundle = new Bundle();
        innerBundle.putString("title", affair.getTitle());
        innerBundle.putString("description", affair.getDescription());
        innerBundle.putLong("date", affair.getDate());
        innerBundle.putLong("time", affair.getTime());
        innerBundle.putString("object", affair.getObject());
        innerBundle.putString("type", affair.getType());
        innerBundle.putString("place", affair.getPlace());
        innerBundle.putInt("status", affair.getStatus());
        Intent startDetailActivity = new Intent(this
                .getContext().getApplicationContext(), DetailAffairActivity.class);
        startDetailActivity.putExtras(innerBundle);
        this.startActivity(startDetailActivity, innerBundle);
    }

}