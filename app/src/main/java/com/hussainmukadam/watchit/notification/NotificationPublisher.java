package com.hussainmukadam.watchit.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.hussainmukadam.watchit.BuildConfig;
import com.hussainmukadam.watchit.R;
import com.hussainmukadam.watchit.util.Util;
import com.hussainmukadam.watchit.util.WatchItConstants;
import com.hussainmukadam.watchit.BaseActivity;
import com.hussainmukadam.watchit.mainpage.model.Movie;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by hussain on 8/28/17.
 */

public class NotificationPublisher extends BroadcastReceiver {
    private static final String TAG = "NotificationPublisher";
    private static final String CHANNEL_ID = "1";

    @Override
    public void onReceive(Context context, Intent intent) {
        //Get notification manager to manage/send notifications

        if(intent.getAction()!=null) {
            if (intent.getAction().equals(WatchItConstants.NOTIFICATION_MESSAGE)) {

                Util.debugLog(TAG, "onReceive: Inside Notification Receiver");
                //Intent to invoke app when click on notification.
                //In this sample, we want to start/launch this sample app when user clicks on notification
                //TODO: Send the user to the detailFragment of the movie shown in the Notification
                Intent intentToRepeat = new Intent(context, BaseActivity.class);
                //set flag to restart/relaunch the app
                intentToRepeat.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                //Pending intent to handle launch of Activity in intent above
                PendingIntent pendingIntent =
                        PendingIntent.getActivity(context, WatchItConstants.ALARM_TYPE_RTC, intentToRepeat, PendingIntent.FLAG_UPDATE_CURRENT);

                //Build notification
//        Notification repeatedNotification = buildNotification(context, pendingIntent).build();

                Movie randomMovieOrTvSeries = fetchRandomMovieOrTvSeries();

                //Send local notification
                if (randomMovieOrTvSeries != null) {
                    NotificationHelper.getNotificationManager(context).notify(WatchItConstants.NOTIFICATION_ID, buildNotification(context, pendingIntent, randomMovieOrTvSeries));
                } else {
                    Toast.makeText(context, "Suggestions notifications enabled", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private Notification buildNotification(Context context, PendingIntent pendingIntent, Movie randomMovieOrTvSeries) {
        Notification repeatedNotification;
        RemoteViews contentView = null;
        RemoteViews expandedView = null;

        Date date = new Date(System.currentTimeMillis());
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSS", Locale.US);
        String dateFormatted = dateFormat.format(date);

        NotificationCompat.Builder notificationBuilder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID);
        } else {
            notificationBuilder = new NotificationCompat.Builder(context);
        }

        Util.debugLog(TAG, "buildNotification: RandomMovieorTVSeries is not null");
        contentView = new RemoteViews(context.getPackageName(), R.layout.notification_normal_layout);
        expandedView = new RemoteViews(context.getPackageName(), R.layout.notification_expanded_layout);

        contentView.setTextViewText(R.id.title, randomMovieOrTvSeries.getMovieTitle());


        notificationBuilder
                .setSmallIcon(R.drawable.ic_logo_notification)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent)
                .setCustomContentView(contentView)
                .setCustomBigContentView(expandedView)
                .setTicker(dateFormatted)
                .setAutoCancel(true);


        repeatedNotification = notificationBuilder.build();

        Util.debugLog(TAG, "buildNotification: Content View is not null");
        Picasso.with(context).load(BuildConfig.imageBaseUrl + randomMovieOrTvSeries.getPosterPath())
                .into(contentView, R.id.image, WatchItConstants.NOTIFICATION_ID, repeatedNotification);

        Picasso.with(context).load(BuildConfig.imageBaseUrl + randomMovieOrTvSeries.getPosterPath())
                .into(expandedView, R.id.iv_notification, WatchItConstants.NOTIFICATION_ID, repeatedNotification);

        return repeatedNotification;
    }

    private Movie fetchRandomMovieOrTvSeries() {
        Realm realm = Realm.getDefaultInstance();
        //TODO: Add Random TV Series to fetch
        RealmResults<Movie> movieRealmResults = realm.where(Movie.class).equalTo("isNotified", false).findAll();

        Util.debugLog(TAG, "fetchRandomMovieOrTvSeries: Size of movies with isNotified false " + movieRealmResults.size());

        if (movieRealmResults.size() != 0) {

            Random r = new Random();
            int randomNumber = r.nextInt(movieRealmResults.size());

            Movie movie = movieRealmResults.get(randomNumber);

            realm.beginTransaction();
            movieRealmResults.get(randomNumber).setNotified(true);
            realm.commitTransaction();


            return movie;
        } else {
            return null;
        }
    }
}
