package com.hussainmukadam.watchit.intropage.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hussain on 7/21/17.
 */

public class Genre {
    @SerializedName("name")
    private String genreName;
    @SerializedName("id")
    private int genreId;

    public String getGenreName() {
        return genreName;
    }

    public int getGenreId() {
        return genreId;
    }

    @Override
    public String toString() {
        return "Genre{" +
                "genreName='" + genreName + '\'' +
                ", genreId=" + genreId +
                '}';
    }
}
