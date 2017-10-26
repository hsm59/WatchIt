package com.hussainmukadam.watchit.mainpage.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by hussain on 8/19/17.
 */

public class TvSeries extends RealmObject implements Parcelable{

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

    public TvSeries(){}

    public TvSeries(Parcel parcel) {
        super();
        readFromParcel(parcel);
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(tvId);
        dest.writeString(tvTitle);
        dest.writeString(tvPosterPath);
        dest.writeString(tvReleaseDate);
        dest.writeString(tvOverview);
        dest.writeFloat(tvVoteAverage);
        dest.writeFloat(tvPopularity);
        dest.writeInt(tvVoteCount);
        dest.writeString(tvBackdropPath);
        dest.writeByte((byte) (isWatchLater? 0: 1));
        dest.writeByte((byte)(isNotified? 0: 1));
    }

    private void readFromParcel(Parcel parcel) {
        tvId = parcel.readInt();
        tvTitle = parcel.readString();
        tvPosterPath = parcel.readString();
        tvReleaseDate = parcel.readString();
        tvOverview = parcel.readString();
        tvVoteAverage = parcel.readFloat();
        tvVoteCount = parcel.readInt();
        tvPopularity = parcel.readFloat();
        tvBackdropPath = parcel.readString();
        isWatchLater = parcel.readByte()!=0;
        isNotified = parcel.readByte()!=0;
    }

    public static final Parcelable.Creator<TvSeries> CREATOR = new Creator<TvSeries>() {
        @Override
        public TvSeries createFromParcel(Parcel source) {
            return new TvSeries(source);
        }

        @Override
        public TvSeries[] newArray(int size) {
            return new TvSeries[0];
        }
    };
}
