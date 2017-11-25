package com.hussainmukadam.watchit.notification;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import com.hussainmukadam.watchit.util.WatchItConstants;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by hussain on 9/1/17.
 */

public class NotificationHelper {
    private static final String TAG = "NotificationHelper";

    private static AlarmManager alarmManagerRTC;
    private static PendingIntent alarmIntentRTC;

    /**
     * This is the real time /wall clock time
     *
     * @param context
     */
    public static void scheduleRepeatingRTCNotification(Context context) {
        //get calendar instance to be able to select what time notification should be scheduled
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        //Setting time of the day (8am here) when notification will be sent every day (default)
        //TODO: Set it to only go off on Saturday nights
        calendar.set(Calendar.DAY_OF_WEEK, 7);
        calendar.set(Calendar.HOUR_OF_DAY, 20);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.MILLISECOND, 0);

        //Setting intent to class where Alarm broadcast message will be handled
        Intent intent = new Intent(context, NotificationPublisher.class);
        intent.setAction(WatchItConstants.NOTIFICATION_MESSAGE);
        //Setting alarm pending intent
        alarmIntentRTC = PendingIntent.getBroadcast(context, WatchItConstants.ALARM_TYPE_RTC, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //getting instance of AlarmManager service
        alarmManagerRTC = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        //Setting alarm to wake up device every day for clock time.
        //AlarmManager.RTC_WAKEUP is responsible to wake up device for sure, which may not be good practice all the time.
        // Use this when you know what you're doing.
        //Use RTC when you don't need to wake up device, but want to deliver the notification whenever device is woke-up
        //We'll be using RTC.WAKEUP for demo purpose only
        long interval = 7 * AlarmManager.INTERVAL_DAY;
        setRepeatingExactAlarm(calendar.getTimeInMillis(), interval, alarmIntentRTC);
    }

    private static void setRepeatingExactAlarm(long time, long interval, PendingIntent pIntent) {
        Log.d(TAG, "setSingleExactAlarm: Alarm time " + time);
        alarmManagerRTC.setRepeating(AlarmManager.RTC, time, interval, pIntent);
    }

    public static NotificationManager getNotificationManager(Context context) {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public static void cancelAlarmRTC() {
        if (alarmManagerRTC != null) {
            Log.d(TAG, "cancelAlarmRTC: Alarm Manager Cancelled");
            alarmManagerRTC.cancel(alarmIntentRTC);
        }
    }

    /**
     * Enable boot receiver to persist alarms set for notifications across device reboots
     */
    public static void enableBootReceiver(Context context) {
        ComponentName receiver = new ComponentName(context, NotificationPublisher.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    /**
     * Disable boot receiver when user cancels/opt-out from notifications
     */
    public static void disableBootReceiver(Context context) {
        ComponentName receiver = new ComponentName(context, NotificationPublisher.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

}
