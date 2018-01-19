package com.hussainmukadam.watchit.mainpage.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by hussain on 7/23/17.
 */

public class Movie extends RealmObject implements Parcelable {
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
    @SerializedName("popularity")
    private float moviePopularity;
    private boolean isWatchLater;
    private boolean isNotified;

    public Movie() {
    }

    public Movie(Parcel parcel) {
        super();
        readFromParcel(parcel);
    }

    public boolean isNotified() {
        return isNotified;
    }

    public void setNotified(boolean notified) {
        isNotified = notified;
    }

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

    public float getMoviePopularity() {
        return moviePopularity;
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
                ", moviePopularity=" + moviePopularity +
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
        dest.writeInt(movieId);
        dest.writeInt(movieVoteCount);
        dest.writeFloat(movieVoteAverage);
        dest.writeString(movieTitle);
        dest.writeString(movieOverview);
        dest.writeString(posterPath);
        dest.writeString(releaseDate);
        dest.writeString(backdropPath);
        dest.writeFloat(moviePopularity);
        dest.writeByte((byte) (isWatchLater ? 0 : 1));
        dest.writeByte((byte) (isNotified ? 0 : 1));
    }

    public void readFromParcel(Parcel parcel) {
        movieId = parcel.readInt();
        movieVoteCount = parcel.readInt();
        movieVoteAverage = parcel.readFloat();
        movieTitle = parcel.readString();
        movieOverview = parcel.readString();
        posterPath = parcel.readString();
        releaseDate = parcel.readString();
        backdropPath = parcel.readString();
        moviePopularity = parcel.readFloat();
        isWatchLater = parcel.readByte() != 0;
        isNotified = parcel.readByte() != 0;
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[0];
        }
    };

    @Override
    public boolean equals(Object obj) {
        boolean isEqual = false;
        if (obj != null && obj instanceof Movie) {
            isEqual = (this.movieId == ((Movie) obj).getMovieId());
        }
        return isEqual;
    }
}
