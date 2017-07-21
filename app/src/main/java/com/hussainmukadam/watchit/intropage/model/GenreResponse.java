package com.hussainmukadam.watchit.intropage.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hussain on 7/21/17.
 */

public class GenreResponse {
    @SerializedName("genres")
    private List<Genre> genreResults;

    public List<Genre> getGenreResults() {
        return genreResults;
    }

    @Override
    public String toString() {
        return "GenreResponse{" +
                "genreResults=" + genreResults +
                '}';
    }
}
