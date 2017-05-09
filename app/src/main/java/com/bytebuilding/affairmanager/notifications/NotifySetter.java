package com.bytebuilding.affairmanager.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.bumptech.glide.load.engine.executor.FifoPriorityThreadPoolExecutor;
import com.bytebuilding.affairmanager.database.DBHelper;
import com.bytebuilding.affairmanager.model.Affair;
import com.bytebuilding.affairmanager.model.realm.UserAffair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class NotifySetter extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        DBHelper dbHelper = new DBHelper(context);
        Realm realm = Realm.getDefaultInstance();

        OfflineNotificationHelper.getInstance().initializeAlarmManager(context);
        OfflineNotificationHelper offlineNotificationHelper = OfflineNotificationHelper.getInstance();

        List<Affair> affairs = new ArrayList<>();
        List<UserAffair> userAffairs = new ArrayList<>();
        RealmQuery<UserAffair> userAffairRealmResults = realm.where(UserAffair.class)
                                                                .equalTo("status", Affair.STATUS_CURRENT)
                                                                .or()
                                                                .equalTo("status", Affair.STATUS_OVERDUE);
        affairs.addAll(dbHelper.getDbQueryManager().getAffairs(DBHelper.SELECTION_BY_STATUS
                + " OR " + DBHelper.SELECTION_BY_STATUS, new String[]{Integer.toString(Affair
                .STATUS_CURRENT), Integer.toString(Affair.STATUS_OVERDUE)}, DBHelper.COLOUMN_DATE));
        userAffairs.addAll((Collection<? extends UserAffair>) userAffairRealmResults);

        for (Affair affair : affairs) {
            if (affair.getDate() != 0) {
                offlineNotificationHelper.setReceiver(affair);
            }
        }

        for (UserAffair userAffair : userAffairs) {
            if (userAffair.getDate() != 0) {
                offlineNotificationHelper.setReceiver(userAffair);
            }
        }
    }
}
