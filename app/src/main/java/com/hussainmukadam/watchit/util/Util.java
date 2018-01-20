package com.hussainmukadam.watchit.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.hussainmukadam.watchit.BuildConfig;

/**
 * Created by hussain on 8/30/17.
 */

public class Util {

    public static boolean isConnected(Context mContext) {
        if (mContext != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            return info != null && info.isConnected();
        } else {
            return false;
        }
    }

    public static void debugLog(String TAG, String log) {
        if(BuildConfig.DEBUG) {
            Log.d(TAG, log);
        }
    }

}
