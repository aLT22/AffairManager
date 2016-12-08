package com.bytebuilding.affairmanager.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.bytebuilding.affairmanager.model.Affair;

public class OfflineNotificationHelper {

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

        intent.putExtra("title", affair.getTitle());
        intent.putExtra("timestamp", affair.getTimestamp());
        intent.putExtra("color", affair.getColor());
        intent.putExtra("description", affair.getDescription());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), (int) affair
                .getTimestamp(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.set(AlarmManager.RTC_WAKEUP, affair.getDate(), pendingIntent);
    }

    public void doneAlarm(long timestamp) {
        Intent intent = new Intent(context, OfflineNotification.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) timestamp, intent, PendingIntent
                .FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);
    }
}
