package com.hussainmukadam.watchit.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.hussainmukadam.watchit.BuildConfig;
import com.hussainmukadam.watchit.R;
import com.hussainmukadam.watchit.mainpage.BaseActivity;
import com.hussainmukadam.watchit.mainpage.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.Random;

import io.realm.Realm;
import io.realm.RealmQuery;
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
//        Notification repeatedNotification = buildNotification(context, pendingIntent).build();

        //Send local notification
        NotificationHelper.getNotificationManager(context).notify(NotificationHelper.NOTIFICATION_ID, buildNotification(context, pendingIntent));
    }

    private Notification buildNotification(Context context, PendingIntent pendingIntent) {
        Notification repeatedNotification;
        RemoteViews contentView = null;

        NotificationCompat.Builder notificationBuilder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder = new NotificationCompat.Builder(context,
                    CHANNEL_ID);
        } else {
            notificationBuilder = new NotificationCompat.Builder(context);
        }

        Movie randomMovieOrTvSeries = fetchRandomMovieOrTvSeries();

        if(randomMovieOrTvSeries!=null) {
            contentView = new RemoteViews(context.getPackageName(), R.layout.notification_layout);

            contentView.setTextViewText(R.id.title, randomMovieOrTvSeries.getMovieTitle());
            contentView.setTextViewText(R.id.text, randomMovieOrTvSeries.getMovieOverview());
        }

        notificationBuilder
                .setContentIntent(pendingIntent)
                .setSmallIcon(android.R.drawable.arrow_up_float)
                .setContent(contentView)
                .setAutoCancel(true);

        repeatedNotification = notificationBuilder.build();

        if(contentView!=null) {
            Picasso.with(context).load(BuildConfig.imageBaseUrl + randomMovieOrTvSeries.getPosterPath())
                    .into(contentView, R.id.image, NotificationHelper.NOTIFICATION_ID, repeatedNotification);
        }

        return repeatedNotification;
    }

    private Movie fetchRandomMovieOrTvSeries() {
        Realm realm = Realm.getDefaultInstance();

        RealmResults<Movie> movieRealmResults = realm.where(Movie.class).equalTo("isNotified", false).findAll();
        Log.d(TAG, "fetchRandomMovieOrTvSeries: Size of movies with isNotified false " + movieRealmResults.size());

        if (movieRealmResults.size() != 0) {

            Random r = new Random();
            int randomNumber = r.nextInt(movieRealmResults.size());

            return movieRealmResults.get(randomNumber);
        } else {
            return null;
        }
    }
}
