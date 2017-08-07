package com.hussainmukadam.watchit;

import android.app.Application;

import com.crashlytics.android.Crashlytics;


import io.fabric.sdk.android.Fabric;
import io.realm.Realm;

/**
 * Created by hussain on 7/23/17.
 */

public class WatchItApplication extends Application {



    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        Realm.init(this);
    }
}
