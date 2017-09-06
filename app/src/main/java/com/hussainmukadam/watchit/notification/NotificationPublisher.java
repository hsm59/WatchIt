package com.hussainmukadam.watchit.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.hussainmukadam.watchit.mainpage.BaseActivity;

/**
 * Created by hussain on 8/28/17.
 */

public class NotificationPublisher extends BroadcastReceiver {
    private static final String TAG = "NotificationPublisher";

    @Override
    public void onReceive(Context context, Intent intent) {
        //Get notification manager to manage/send notifications

        Log.d(TAG, "onReceive: Inside Notification Receiver");
        //Intent to invoke app when click on notification.
        //In this sample, we want to start/launch this sample app when user clicks on notification
        Intent intentToRepeat = new Intent(context, BaseActivity.class);
        //set flag to restart/relaunch the app
        intentToRepeat.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //Pending intent to handle launch of Activity in intent above
        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, NotificationHelper.ALARM_TYPE_RTC, intentToRepeat, PendingIntent.FLAG_UPDATE_CURRENT);

        //Build notification
        Notification repeatedNotification = buildNotification(context, pendingIntent).build();

        //Send local notification
        NotificationHelper.getNotificationManager(context).notify(NotificationHelper.ALARM_TYPE_RTC, repeatedNotification);
    }

    private NotificationCompat.Builder buildNotification(Context context, PendingIntent pendingIntent) {
        NotificationCompat.Builder notificationBuilder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder = new NotificationCompat.Builder(context,
                    NotificationHelper.NOTIFICATION_ID);
        } else {
            notificationBuilder = new NotificationCompat.Builder(context);
        }

        notificationBuilder
        .setContentIntent(pendingIntent)
        .setSmallIcon(android.R.drawable.arrow_up_float)
        .setContentTitle("Test Notification")
        .setAutoCancel(true);

        return notificationBuilder;
    }
}
