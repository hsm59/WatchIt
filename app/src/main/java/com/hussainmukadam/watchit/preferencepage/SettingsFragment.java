package com.hussainmukadam.watchit.preferencepage;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

import android.os.SystemClock;
import android.support.v7.preference.Preference;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.util.Log;

import com.hussainmukadam.watchit.R;
import com.hussainmukadam.watchit.notification.NotificationHelper;
import com.hussainmukadam.watchit.notification.NotificationPublisher;


/**
 * Created by hussain on 8/24/17.
 */

public class SettingsFragment extends PreferenceFragmentCompat implements OnPreferenceChangeListener{
    private static final String TAG = "SettingsFragment";
    SwitchPreferenceCompat suggestions;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.app_preference);

        suggestions = (SwitchPreferenceCompat) findPreference("suggestions");

        suggestions.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Boolean isSuggestion = (Boolean) newValue;
        Log.d(TAG, "onCreatePreferences: Suggestions are "+isSuggestion);
        if(isSuggestion){
            suggestions.setChecked(true);
            Log.d(TAG, "onCreatePreferences: Is Checked "+isSuggestion);
            NotificationHelper.scheduleRepeatingRTCNotification(getContext());
            NotificationHelper.enableBootReceiver(getContext());
            return true;
        } else {
            suggestions.setChecked(false);
            Log.d(TAG, "onCreatePreferences: Is Checked "+isSuggestion);
            NotificationHelper.cancelAlarmRTC();
            NotificationHelper.disableBootReceiver(getContext());
            return false;
        }
    }
}
