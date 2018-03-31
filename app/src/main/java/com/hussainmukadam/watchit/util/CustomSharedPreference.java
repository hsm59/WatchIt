package com.hussainmukadam.watchit.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hussainmukadam.watchit.BuildConfig;
import com.hussainmukadam.watchit.intropage.model.Genre;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


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

        preferenceEditor.putString("TV_GENRE_DATA", json);
        preferenceEditor.apply();
    }


    public List<Genre> getTvGenrePreference() {
        Gson gson = new Gson();
        String json = preference.getString("TV_GENRE_DATA", "");

        Type type = new TypeToken<List<Genre>>() {
        }.getType();
        List<Genre> genreList = gson.fromJson(json, type);

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

    public void deleteTvGenrePreference(Genre genre) {
        Gson gson = new Gson();
        String json = preference.getString("TV_GENRE_DATA", "");

        Type type = new TypeToken<List<Genre>>() {
        }.getType();
        List<Genre> genreList = gson.fromJson(json, type);

        for (Iterator<Genre> iterator = genreList.iterator(); iterator.hasNext(); ) {
            Genre existingGenre = iterator.next();
            if (existingGenre.getGenreId() == genre.getGenreId())
                iterator.remove();
        }

        String newJson = gson.toJson(genreList);
        Util.debugLog("Deleting Genres ", newJson);
        preferenceEditor.putString("TV_GENRE_DATA", newJson);
        preferenceEditor.apply();
    }

    public void addTvGenrePreference(Genre genre) {
        Gson gson = new Gson();
        String json = preference.getString("TV_GENRE_DATA", "");

        Type type = new TypeToken<List<Genre>>() {
        }.getType();
        List<Genre> genreList = gson.fromJson(json, type);

        genreList.add(genre);

        String newJson = gson.toJson(genreList);
        preferenceEditor.putString("TV_GENRE_DATA", newJson);
        preferenceEditor.apply();
    }

    public void deleteMoviesGenrePreference(Genre genre) {
        Gson gson = new Gson();
        String json = preference.getString("MOVIES_GENRE_DATA", "");

        Type type = new TypeToken<List<Genre>>() {
        }.getType();
        List<Genre> genreList = gson.fromJson(json, type);

        for (Iterator<Genre> iterator = genreList.iterator(); iterator.hasNext(); ) {
            Genre existingGenre = iterator.next();
            if (existingGenre.getGenreId() == genre.getGenreId())
                iterator.remove();
        }

        String newJson = gson.toJson(genreList);
        Util.debugLog("Deleting Genres ", newJson);
        preferenceEditor.putString("MOVIES_GENRE_DATA", newJson);
        preferenceEditor.apply();
    }

    public void addMoviesGenrePreference(Genre genre) {
        Gson gson = new Gson();
        String json = preference.getString("MOVIES_GENRE_DATA", "");

        Type type = new TypeToken<List<Genre>>() {
        }.getType();
        List<Genre> genreList = gson.fromJson(json, type);

        genreList.add(genre);

        String newJson = gson.toJson(genreList);
        preferenceEditor.putString("MOVIES_GENRE_DATA", newJson);
        preferenceEditor.apply();
    }

    public void setMoviesGenrePreference(List<Genre> genreArrayList) {
        Gson gson = new Gson();
        String json = gson.toJson(genreArrayList);

        preferenceEditor.putString("MOVIES_GENRE_DATA", json);
        preferenceEditor.apply();
    }

    public List<Genre> getMoviesGenrePreference() {
        Gson gson = new Gson();
        String json = preference.getString("MOVIES_GENRE_DATA", "");

        Type type = new TypeToken<List<Genre>>() {
        }.getType();
        List<Genre> genreList = gson.fromJson(json, type);

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
    }

    public void setTopRatedUrl(boolean topRatedOnly) {
        preferenceEditor.putBoolean("TOP_RATED_ONLY", topRatedOnly).commit();
    }

    public boolean isTopRatedOnly() {
        return preference.getBoolean("TOP_RATED_ONLY", false);
    }

    public void setSuggestNotificationFlag(boolean suggestNotification) {
        preferenceEditor.putBoolean("SUGGEST_NOTIFICATION", suggestNotification).commit();
    }

    public boolean suggestNotification() {
        return preference.getBoolean("SUGGEST_NOTIFICATION", false);
    }
}
