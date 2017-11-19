package com.hussainmukadam.watchit.detailpage.model;

import com.google.gson.annotations.SerializedName;

public class Season {

    @SerializedName("air_date")
    private String airDate;
    @SerializedName("episode_count")
    private Integer episodeCount;
    @SerializedName("id")
    private Integer id;
    @SerializedName("poster_path")
    private Object posterPath;
    @SerializedName("season_number")
    private Integer seasonNumber;

    public String getAirDate() {
        return airDate;
    }

    public Integer getEpisodeCount() {
        return episodeCount;
    }

    public Integer getId() {
        return id;
    }

    public Object getPosterPath() {
        return posterPath;
    }

    public Integer getSeasonNumber() {
        return seasonNumber;
    }

}
