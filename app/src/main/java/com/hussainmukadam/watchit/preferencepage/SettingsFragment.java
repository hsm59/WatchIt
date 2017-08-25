package com.hussainmukadam.watchit.preferencepage;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.hussainmukadam.watchit.R;

/**
 * Created by hussain on 8/24/17.
 */

public class SettingsFragment extends PreferenceFragmentCompat {


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.app_preference);
    }
}
