package com.hussainmukadam.watchit.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hussainmukadam.watchit.BuildConfig;
import com.hussainmukadam.watchit.intropage.model.Genre;

import java.lang.reflect.Type;
import java.util.List;


/**
 * Created by hussain on 7/21/17.
 */

public class CustomSharedPreference {
    private static final String TAG = "CustomSharedPreference";
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "WatchIt";
    private SharedPreferences preference;
    private SharedPreferences.Editor preferenceEditor;
    private Context mContext;


    public CustomSharedPreference(Context context) {
        this.mContext = context;
        preference = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        preferenceEditor = preference.edit();
    }

    public void setTvGenrePreference(List<Genre> genreArrayList) {
        Gson gson = new Gson();
        String json = gson.toJson(genreArrayList);

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "setMoviesGenrePreference: JSON String of TV " + json);
        }

        preferenceEditor.putString("TV_GENRE_DATA", json);
        preferenceEditor.commit();
    }


    public List<Genre> getTvGenrePreference() {
        Gson gson = new Gson();
        String json = preference.getString("TV_GENRE_DATA", "");

        Type type = new TypeToken<List<Genre>>() {
        }.getType();
        List<Genre> genreList = gson.fromJson(json, type);

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "getGenrePreference: Genre List " + genreList);
        }

        if(genreList!=null) {
            if (genreList.size() != 0) {
                return genreList;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public void setMoviesGenrePreference(List<Genre> genreArrayList) {
        Gson gson = new Gson();
        String json = gson.toJson(genreArrayList);

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "setMoviesGenrePreference: JSON String of Movies " + json);
        }

        preferenceEditor.putString("MOVIES_GENRE_DATA", json);
        preferenceEditor.commit();
    }

    public List<Genre> getMoviesGenrePreference() {
        Gson gson = new Gson();
        String json = preference.getString("MOVIES_GENRE_DATA", "");

        Type type = new TypeToken<List<Genre>>() {
        }.getType();
        List<Genre> genreList = gson.fromJson(json, type);

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "getGenrePreference: Genre List " + genreList);
        }

        if (genreList != null) {
            if (genreList.size() != 0) {
                return genreList;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public boolean isIntroDisplayed() {
        return preference.getBoolean("HAS_INTRO_DISPLAYED", false);
    }

    public void setIntroDisplayed(boolean introDisplayed) {
        preferenceEditor.putBoolean("HAS_INTRO_DISPLAYED", introDisplayed).commit();
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "setLogin: " + introDisplayed);
        }
    }


}
