package com.hussainmukadam.watchit.notification;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.hussainmukadam.watchit.mainpage.model.Movie;
import com.hussainmukadam.watchit.util.WatchItConstants;

/**
 * Created by hussain on 30/01/18.
 */

public class NotificationActionHandler extends BroadcastReceiver {
    private static final String TAG = "NotificationActionHandl";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getExtras() != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Log.d(TAG, "onReceive: " + bundle.getString("CHECK_ACTION"));
                if (bundle.getString("CHECK_ACTION").equals("done")) {
                    handleNotificationDone(context);
                } else {
                    Movie randomMovie = bundle.getParcelable("MOVIE_DATA");
                    handleNotificationShare(context, randomMovie);
                }
            }
        }
    }

    private void handleNotificationDone(Context mContext) {
        NotificationManager notificationManager = (NotificationManager) mContext
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(WatchItConstants.NOTIFICATION_ID);
    }

    private void handleNotificationShare(Context mContext, Movie randomMovieData) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Let's watch "+randomMovieData.getMovieTitle()+ " together!");
        sendIntent.setType("text/plain");
        mContext.startActivity(sendIntent);
        handleNotificationDone(mContext);
    }
}
