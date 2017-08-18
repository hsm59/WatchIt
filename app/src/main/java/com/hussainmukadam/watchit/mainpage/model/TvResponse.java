package com.hussainmukadam.watchit.mainpage.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hussain on 8/19/17.
 */

public class TvResponse {

    @SerializedName("results")
    private List<TvSeries> tvList;

    @SerializedName("total_pages")
    private int totalPages;

    public List<TvSeries> getTvList() {
        return tvList;
    }

    public int getTotalPages() {
        return totalPages;
    }

    @Override
    public String toString() {
        return "TvResponse{" +
                "tvList=" + tvList +
                ", totalPages=" + totalPages +
                '}';
    }
}
