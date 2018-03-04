package com.hussainmukadam.watchit.notification;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.hussainmukadam.watchit.mainpage.model.Movie;
import com.hussainmukadam.watchit.mainpage.model.TvSeries;
import com.hussainmukadam.watchit.util.WatchItConstants;

/**
 * Created by hussain on 30/01/18.
 */

public class NotificationActionHandler extends BroadcastReceiver {
    private static final String TAG = "NotificationActionHandl";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getExtras() != null) {
            String action = intent.getAction();

            if (action != null) {
                if (action.equals("DONE_ACTION")) {
                    handleNotificationDone(context);
                } else {
                    Bundle bundle = intent.getExtras();
                    Object randomMovie = bundle.getParcelable("NOTIF_DATA");
                    handleNotificationShare(context, randomMovie);
                }
            }
        }
    }

    private void handleNotificationDone(Context mContext) {
        NotificationManager notificationManager = (NotificationManager) mContext
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(WatchItConstants.NOTIFICATION_ID);
        mContext.sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }

    private void handleNotificationShare(Context mContext, Object randomMovieData) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (randomMovieData instanceof Movie) {
            Movie movie = (Movie) randomMovieData;
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Let's watch " + movie.getMovieTitle() + " together!");
        } else {
            TvSeries tvSeries = (TvSeries) randomMovieData;
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Let's watch " + tvSeries.getTvTitle() + " together!");
        }
        sendIntent.setType("text/plain");
        mContext.startActivity(sendIntent);
        handleNotificationDone(mContext);
    }
}
