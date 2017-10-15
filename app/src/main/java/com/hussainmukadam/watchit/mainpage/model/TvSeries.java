package com.hussainmukadam.watchit.mainpage.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by hussain on 8/19/17.
 */

public class TvSeries extends RealmObject {

    @PrimaryKey
    @SerializedName("id")
    private int tvId;
    @SerializedName("name")
    private String tvTitle;
    @SerializedName("poster_path")
    private String tvPosterPath;
    @SerializedName("first_air_date")
    private String tvReleaseDate;
    @SerializedName("overview")
    private String tvOverview;
    @SerializedName("vote_count")
    private int tvVoteCount;
    @SerializedName("vote_average")
    private float tvVoteAverage;
    @SerializedName("popularity")
    private float tvPopularity;
    @SerializedName("backdrop_path")
    private String tvBackdropPath;
    private boolean isWatchLater;
    private boolean isNotified;

    public boolean isNotified() {
        return isNotified;
    }

    public void setNotified(boolean notified) {
        isNotified = notified;
    }

    public void setWatchLater(boolean watchLater) {
        isWatchLater = watchLater;
    }

    public int getTvId() {
        return tvId;
    }

    public String getTvTitle() {
        return tvTitle;
    }

    public String getTvPosterPath() {
        return tvPosterPath;
    }

    public String getTvReleaseDate() {
        return tvReleaseDate;
    }

    public String getTvOverview() {
        return tvOverview;
    }

    public int getTvVoteCount() {
        return tvVoteCount;
    }

    public float getTvVoteAverage() {
        return tvVoteAverage;
    }

    public float getTvPopularity() {
        return tvPopularity;
    }

    public String getTvBackdropPath() {
        return tvBackdropPath;
    }

    public boolean isWatchLater() {
        return isWatchLater;
    }

    @Override
    public String toString() {
        return "TvSeries{" +
                "tvId=" + tvId +
                ", tvTitle='" + tvTitle + '\'' +
                ", tvPosterPath='" + tvPosterPath + '\'' +
                ", tvReleaseDate='" + tvReleaseDate + '\'' +
                ", tvOverview='" + tvOverview + '\'' +
                ", tvVoteCount=" + tvVoteCount +
                ", tvVoteAverage=" + tvVoteAverage +
                ", tvPopularity=" + tvPopularity +
                ", tvBackdropPath='" + tvBackdropPath + '\'' +
                ", isWatchLater=" + isWatchLater +
                ", isNotified=" + isNotified +
                '}';
    }
}
