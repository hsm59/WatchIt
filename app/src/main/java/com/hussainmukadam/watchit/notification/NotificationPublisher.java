package com.hussainmukadam.watchit.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.hussainmukadam.watchit.BuildConfig;
import com.hussainmukadam.watchit.R;
import com.hussainmukadam.watchit.mainpage.model.TvSeries;
import com.hussainmukadam.watchit.util.Util;
import com.hussainmukadam.watchit.util.WatchItConstants;
import com.hussainmukadam.watchit.BaseActivity;
import com.hussainmukadam.watchit.mainpage.model.Movie;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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


    private NotificationManager notifManager;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction() != null) {
            Util.debugLog(TAG, "onReceive: Inside Notification Receiver Outside getAction()");
            if (intent.getAction().equals(WatchItConstants.NOTIFICATION_MESSAGE)) {

                Util.debugLog(TAG, "onReceive: Inside Notification Receiver");

                Object randomMovieOrTvSeries = fetchRandomMovieOrTvSeries();

                if (randomMovieOrTvSeries != null && Util.isConnected(context)) {
                    createSuggestionNotification(randomMovieOrTvSeries, context);
                } else {
                    Util.debugLog(TAG, "Inside else");
                    createRetentionNotification(context);
                }
            }
        }
    }

    public void createSuggestionNotification(Object randomMovieOrTvSeries, Context context) {
        NotificationImageTask notificationImageTask = new NotificationImageTask(context);
        Movie movie = null;
        TvSeries tvSeries = null;
        Bitmap largeIconBitmap = null;
        PendingIntent sharePendingIntent;

        Intent doneIntent = new Intent(context, NotificationActionHandler.class);
        Bundle bundle = new Bundle();
        doneIntent.setAction("DONE_ACTION");
        doneIntent.putExtras(bundle);
        PendingIntent donePendingIntent = PendingIntent.getBroadcast(context, WatchItConstants.ALARM_TYPE_RTC, doneIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        if (randomMovieOrTvSeries instanceof Movie) {
            movie = (Movie) randomMovieOrTvSeries;
            Intent shareIntent = new Intent(context, NotificationActionHandler.class);
            Bundle shareBundle = new Bundle();
            shareBundle.putParcelable("NOTIF_DATA", movie);
            shareIntent.setAction("SHARE_ACTION");
            shareIntent.putExtras(shareBundle);
            sharePendingIntent = PendingIntent.getBroadcast(context, WatchItConstants.ALARM_TYPE_RTC, shareIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            try {
                largeIconBitmap = notificationImageTask.execute(BuildConfig.imageBaseUrl + movie.getPosterPath()).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            tvSeries = (TvSeries) randomMovieOrTvSeries;
            Intent shareIntent = new Intent(context, NotificationActionHandler.class);
            Bundle shareBundle = new Bundle();
            shareBundle.putParcelable("NOTIF_DATA", tvSeries);
            shareIntent.setAction("SHARE_ACTION");
            shareIntent.putExtras(shareBundle);
            sharePendingIntent = PendingIntent.getBroadcast(context, WatchItConstants.ALARM_TYPE_RTC, shareIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            try {
                largeIconBitmap = notificationImageTask.execute(BuildConfig.imageBaseUrl + tvSeries.getTvPosterPath()).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // There are hardcoding only for show it's just strings
        String name = "WatchIt Channels";
        String id = "Suggestions"; // The user-visible name of the channel.
        String description = "Movie/TV Series Suggestion on Weekends"; // The user-visible description of the channel.

        Intent intent;
        PendingIntent pendingIntent;
        NotificationCompat.Builder builder;


        if (notifManager == null) {
            notifManager = NotificationHelper.getNotificationManager(context);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel mChannel = notifManager.getNotificationChannel(id);


            if (mChannel == null) {
                mChannel = new NotificationChannel(id, name, importance);
                mChannel.setDescription(description);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notifManager.createNotificationChannel(mChannel);
            }

            builder = new NotificationCompat.Builder(context, id);
            //context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
            Intent launchIntent = new Intent(context, BaseActivity.class);
            launchIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            if (movie != null) {
                launchIntent.putExtra("NOTIFICATION_DETAIL", movie);
            } else {
                launchIntent.putExtra("NOTIFICATION_DETAIL", tvSeries);
            }
            pendingIntent = PendingIntent.getActivity(context, 0, launchIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            if (movie != null) {
                builder.setContentTitle(movie.getMovieTitle())
                        .setTicker(movie.getMovieTitle())
                        .setStyle(new NotificationCompat.BigPictureStyle()
                                .bigPicture(largeIconBitmap));
            } else {
                builder.setContentTitle(tvSeries.getTvTitle())
                        .setTicker(tvSeries.getTvTitle())
                        .setStyle(new NotificationCompat.BigPictureStyle()
                                .bigPicture(largeIconBitmap));
            }


            builder.setSmallIcon(R.drawable.ic_logo_notification)
                    .setContentText(context.getString(R.string.app_name))
                    .addAction(R.drawable.ic_done_black_24dp, "DONE", donePendingIntent)
                    .addAction(R.drawable.ic_share_black_24dp, "SHARE", sharePendingIntent)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        } else {
            Log.d(TAG, "createSuggestionNotification: Inside Else lower SDK");
            builder = new NotificationCompat.Builder(context);

            intent = new Intent(context, BaseActivity.class);
            if (movie != null) {
                intent.putExtra("NOTIFICATION_DETAIL", movie);
            } else {
                intent.putExtra("NOTIFICATION_DETAIL", tvSeries);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            if (movie != null) {
                builder.setContentTitle(movie.getMovieTitle())
                        .setStyle(new NotificationCompat.BigPictureStyle()
                                .bigPicture(largeIconBitmap));
            } else {
                builder.setContentTitle(tvSeries.getTvTitle())
                        .setStyle(new NotificationCompat.BigPictureStyle()
                                .bigPicture(largeIconBitmap));
            }

            builder.setSmallIcon(R.drawable.ic_logo_notification)
                    .setContentText(context.getString(R.string.app_name))
                    .addAction(R.drawable.ic_done_black, "DONE", donePendingIntent)
                    .addAction(R.drawable.ic_share_black, "SHARE", sharePendingIntent)
                    .setContentIntent(pendingIntent)
                    .setPriority(Notification.PRIORITY_HIGH);
        }

        notifManager.notify(WatchItConstants.NOTIFICATION_ID, builder.build());

    }

    private void createRetentionNotification(Context context) {
        // There are hardcoding only for show it's just strings
        String name = "WatchIt Channels";
        String id = "Retention"; // The user-visible name of the channel.
        String description = "Check out Watch it! Discover new Movies/TV Series to Watch"; // The user-visible description of the channel.

        Intent intent;
        PendingIntent pendingIntent;
        NotificationCompat.Builder builder;

        if (notifManager == null) {
            notifManager = NotificationHelper.getNotificationManager(context);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel mChannel = notifManager.getNotificationChannel(id);

            if (mChannel == null) {
                mChannel = new NotificationChannel(id, name, importance);
                mChannel.setDescription(description);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notifManager.createNotificationChannel(mChannel);
            }

            builder = new NotificationCompat.Builder(context, id);

            intent = new Intent(context, BaseActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setContentTitle(description)  // required
                    .setSmallIcon(R.drawable.ic_logo_notification) // required
                    .setContentText(context.getString(R.string.app_name))  // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(context.getString(R.string.app_name))
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        } else {

            builder = new NotificationCompat.Builder(context);

            intent = new Intent(context, BaseActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setContentTitle(description)                           // required
                    .setSmallIcon(R.drawable.ic_logo_notification) // required
                    .setContentText(context.getString(R.string.app_name))  // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(context.getString(R.string.app_name))
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setPriority(Notification.PRIORITY_HIGH);
        }

        Notification notification = builder.build();
        notifManager.notify(WatchItConstants.NOTIFICATION_ID, notification);

    }


    private Object fetchRandomMovieOrTvSeries() {
        Realm realm = Realm.getDefaultInstance();
        //TODO: Add Random TV Series to fetch
        RealmResults<Movie> movieRealmResults = realm.where(Movie.class).equalTo("isNotified", false).findAll();

        RealmResults<TvSeries> tvSeriesRealmResults = realm.where(TvSeries.class).equalTo("isNotified", false).findAll();

        Util.debugLog(TAG, "fetchRandomMovieOrTvSeries: Size of movies with isNotified false " + movieRealmResults.size());

        if (movieRealmResults.size() != 0) {

            if (tvSeriesRealmResults.size() != 0) {

                if (movieRealmResults.size() > tvSeriesRealmResults.size()) {
                    Random r = new Random();
                    int randomNumber = r.nextInt(movieRealmResults.size());

                    Movie movie = movieRealmResults.get(randomNumber);

                    realm.beginTransaction();
                    movieRealmResults.get(randomNumber).setNotified(true);
                    realm.commitTransaction();


                    return movie;

                } else {

                    Random r = new Random();
                    int randomNumber = r.nextInt(tvSeriesRealmResults.size());

                    TvSeries tvSeries = tvSeriesRealmResults.get(randomNumber);

                    realm.beginTransaction();
                    tvSeriesRealmResults.get(randomNumber).setNotified(true);
                    realm.commitTransaction();


                    return tvSeries;
                }

            } else {

                Random r = new Random();
                int randomNumber = r.nextInt(movieRealmResults.size());

                Movie movie = movieRealmResults.get(randomNumber);

                realm.beginTransaction();
                movieRealmResults.get(randomNumber).setNotified(true);
                realm.commitTransaction();


                return movie;
            }

        } else {
            return null;
        }
    }
}
