package com.hussainmukadam.watchit.preferencepage;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

/**
 * Created by hussain on 8/28/17.
 */

public class NotificationPublisher extends BroadcastReceiver {
    public static String NOTIFICATION_ID = "1";
    public static String NOTIFICATION = "suggestions";

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        Notification notification = buildNotification(context).build();

        notificationManager.notify(100, notification);
    }

    private NotificationCompat.Builder buildNotification(Context context) {
        NotificationCompat.Builder builder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                        .setSmallIcon(android.R.drawable.arrow_up_float)
                        .setContentTitle("Morning Notification")
                        .setAutoCancel(true);

        return builder;
    }
}
