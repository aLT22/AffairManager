package com.bytebuilding.affairmanager.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.bytebuilding.affairmanager.R;
import com.bytebuilding.affairmanager.activities.MainOfflineActivity;
import com.bytebuilding.affairmanager.utils.AffairManagerApplication;

public class OfflineNotification extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");
        long timestamp = intent.getLongExtra("timestamp", 0);
        int color = intent.getIntExtra("color", 0);

        Intent result = new Intent(context, MainOfflineActivity.class);

        if (AffairManagerApplication.isVisible()) result = intent;
        else result.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        /*В отличие от интента, работающего только внутри приложения, PendingIntent позволяет запустить хранящиеся
        внутри него интенты от имени того приложения, а также с теми полномочиями, что важно, в котором этот
        PendingIntent создавался*/
        /*В случае нмже PendingIntent передает данные приложения сервису для реализации оповещения*/
        PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) timestamp, result, PendingIntent
                .FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle(title);
        builder.setContentText(description);
        builder.setColor(context.getResources().getColor(color));
        builder.setSmallIcon(R.drawable.ic_offline_notification);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        builder.setSound(soundUri);
        builder.setVibrate(new long[]{1500, 1500, 500, 1500, 1500, 500, 1500, 1500, 500});
        builder.setLights(color, 3000, 3000);

        /*Указание, какие свойства уведомления от системы будут у уведомления моего приложения*/
        builder.setDefaults(Notification.FLAG_INSISTENT);
        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_INSISTENT;

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context
                .NOTIFICATION_SERVICE);
        notificationManager.notify((int) timestamp, notification);
    }
}