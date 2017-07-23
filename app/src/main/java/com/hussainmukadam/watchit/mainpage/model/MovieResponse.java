package com.hussainmukadam.watchit.mainpage.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hussain on 7/23/17.
 */

public class MovieResponse {

    @SerializedName("results")
    List<Movie> moviesList;

    public List<Movie> getMoviesList() {
        return moviesList;
    }

    @Override
    public String toString() {
        return "MovieResponse{" +
                "moviesList=" + moviesList +
                '}';
    }
}
