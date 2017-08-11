package com.hussainmukadam.watchit.mainpage.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by hussain on 7/23/17.
 */

public class Movie extends RealmObject {
    @PrimaryKey
    @SerializedName("id")
    private int movieId;
    @SerializedName("vote_count")
    private int movieVoteCount;
    @SerializedName("vote_average")
    private float movieVoteAverage;
    @SerializedName("title")
    private String movieTitle;
    @SerializedName("overview")
    private String movieOverview;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("backdrop_path")
    private String backdropPath;

    private boolean isWatchLater;

    public int getMovieId() {
        return movieId;
    }

    public int getMovieVoteCount() {
        return movieVoteCount;
    }

    public float getMovieVoteAverage() {
        return movieVoteAverage;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getMovieOverview() {
        return movieOverview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public boolean isWatchLater() {
        return isWatchLater;
    }

    public void setWatchLater(boolean watchLater) {
        isWatchLater = watchLater;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "movieId=" + movieId +
                ", movieVoteCount=" + movieVoteCount +
                ", movieVoteAverage=" + movieVoteAverage +
                ", movieTitle='" + movieTitle + '\'' +
                ", movieOverview='" + movieOverview + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", backdropPath='" + backdropPath + '\'' +
                ", isWatchLater=" + isWatchLater +
                '}';
    }
}
