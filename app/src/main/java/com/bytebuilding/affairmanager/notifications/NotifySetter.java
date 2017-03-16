package com.bytebuilding.affairmanager.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.bytebuilding.affairmanager.database.DBHelper;
import com.bytebuilding.affairmanager.model.Affair;

import java.util.ArrayList;
import java.util.List;

public class NotifySetter extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        DBHelper dbHelper = new DBHelper(context);

        OfflineNotificationHelper.getInstance().initializeAlarmManager(context);
        OfflineNotificationHelper offlineNotificationHelper = OfflineNotificationHelper
                .getInstance();

        List<Affair> affairs = new ArrayList<>();
        affairs.addAll(dbHelper.getDbQueryManager().getAffairs(DBHelper.SELECTION_BY_STATUS
                + " OR " + DBHelper.SELECTION_BY_STATUS, new String[]{Integer.toString(Affair
                .STATUS_CURRENT), Integer.toString(Affair.STATUS_OVERDUE)}, DBHelper.COLOUMN_DATE));

        for (Affair affair : affairs) {
            if (affair.getDate() != 0) {
                offlineNotificationHelper.setReceiver(affair);
            }
        }
    }
}
