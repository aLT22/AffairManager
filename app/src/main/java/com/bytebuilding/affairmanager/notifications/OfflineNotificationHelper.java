package com.bytebuilding.affairmanager.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.bytebuilding.affairmanager.model.Affair;
import com.bytebuilding.affairmanager.model.realm.UserAffair;

public class OfflineNotificationHelper {

    public static final int AFFAIR_TYPE_OFFLINE = 0;
    public static final int AFFAIR_TYPE_ONLINE = 1;

    private static OfflineNotificationHelper instance;

    private Context context;

    private AlarmManager alarmManager;

    public static OfflineNotificationHelper getInstance() {
        if (instance == null) instance = new OfflineNotificationHelper();

        return instance;
    }

    public void initializeAlarmManager(Context context) {
        this.context = context;
        this.alarmManager = (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
    }

    public void setReceiver(Affair affair) {
        Intent intent = new Intent(context, OfflineNotification.class);

        intent.putExtra("type", AFFAIR_TYPE_OFFLINE);
        intent.putExtra("title", affair.getTitle());
        intent.putExtra("timestamp", affair.getTimestamp());
        intent.putExtra("color", affair.getColor());
        intent.putExtra("description", affair.getDescription());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(),
                (int) affair.getTimestamp(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (affair.getStatus() != Affair.STATUS_DONE) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, affair.getDate(), affair
                    .getRepeatTimestamp(), pendingIntent);
        }
    }

    public void setReceiver(UserAffair userAffair) {
        Intent intent = new Intent(context, OfflineNotification.class);

        intent.putExtra("type", AFFAIR_TYPE_ONLINE);
        intent.putExtra("title", userAffair.getTitle());
        intent.putExtra("timestamp", userAffair.getTimestamp());
        intent.putExtra("color", userAffair.getColor());
        intent.putExtra("description", userAffair.getDescription());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(),
                (int) userAffair.getTimestamp(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (userAffair.getStatus() != Affair.STATUS_DONE) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, userAffair.getDate(), userAffair.getRepeatTimestamp(), pendingIntent);
        }
    }

    public void doneAlarm(long timestamp) {
        Intent intent = new Intent(context, OfflineNotification.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) timestamp, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);
    }
}
