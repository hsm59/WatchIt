package com.hussainmukadam.watchit.detailpage.model;

import com.google.gson.annotations.SerializedName;
import com.hussainmukadam.watchit.intropage.model.Genre;

import java.util.List;

/**
 * Created by hussain on 17/11/17.
 */

public class MovieDetails {
    @SerializedName("adult")
    private boolean isAdult;
    @SerializedName("backdrop_path")
    private String backdropPath;
    @SerializedName("budget")
    private int budget;
    @SerializedName("genres")
    private List<Genre> genres;
    @SerializedName("homepage")
    private String homepage;
    @SerializedName("id")
    private int movieId;
    @SerializedName("imdb_id")
    private String imdbId;
    @SerializedName("original_language")
    private String originalLanguage;
    @SerializedName("original_title")
    private String originalTitle;
    @SerializedName("overview")
    private String overview;
    @SerializedName("popularity")
    private double popularity;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("runtime")
    private int runtime;
    @SerializedName("tagline")
    private String tagline;
    @SerializedName("title")
    private String movieTitle;
    @SerializedName("video")
    private boolean isVideo;
    @SerializedName("vote_average")
    private float voteAverage;
    @SerializedName("vote_count")
    private float voteCount;

    public boolean isAdult() {
        return isAdult;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public int getBudget() {
        return budget;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public String getHomepage() {
        return homepage;
    }

    public int getMovieId() {
        return movieId;
    }

    public String getImdbId() {
        return imdbId;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public double getPopularity() {
        return popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public int getRuntime() {
        return runtime;
    }

    public String getTagline() {
        return tagline;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public float getVoteCount() {
        return voteCount;
    }

    @Override
    public String toString() {
        return "MovieDetails{" +
                "isAdult=" + isAdult +
                ", backdropPath='" + backdropPath + '\'' +
                ", budget=" + budget +
                ", genres=" + genres +
                ", homepage='" + homepage + '\'' +
                ", movieId=" + movieId +
                ", imdbId='" + imdbId + '\'' +
                ", originalLanguage='" + originalLanguage + '\'' +
                ", originalTitle='" + originalTitle + '\'' +
                ", overview='" + overview + '\'' +
                ", popularity=" + popularity +
                ", posterPath='" + posterPath + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", runtime=" + runtime +
                ", tagline='" + tagline + '\'' +
                ", movieTitle='" + movieTitle + '\'' +
                ", isVideo=" + isVideo +
                ", voteAverage=" + voteAverage +
                ", voteCount=" + voteCount +
                '}';
    }
}
