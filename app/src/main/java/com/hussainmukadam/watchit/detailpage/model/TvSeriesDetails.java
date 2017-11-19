package com.hussainmukadam.watchit.detailpage.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;
import com.hussainmukadam.watchit.intropage.model.Genre;

public class TvSeriesDetails {

    @SerializedName("backdrop_path")
    private String backdropPath;
    @SerializedName("episode_run_time")
    private List<Object> episodeRunTime = null;
    @SerializedName("first_air_date")
    private String firstAirDate;
    @SerializedName("genres")
    private List<Genre> genres = null;
    @SerializedName("homepage")
    private String homepage;
    @SerializedName("id")
    private Integer id;
    @SerializedName("in_production")
    private Boolean inProduction;
    @SerializedName("languages")
    private List<String> languages = null;
    @SerializedName("last_air_date")
    private String lastAirDate;
    @SerializedName("name")
    private String name;
    @SerializedName("networks")
    private List<Object> networks = null;
    @SerializedName("number_of_episodes")
    private Integer numberOfEpisodes;
    @SerializedName("number_of_seasons")
    private Integer numberOfSeasons;
    @SerializedName("origin_country")
    private List<Object> originCountry = null;
    @SerializedName("original_language")
    private String originalLanguage;
    @SerializedName("original_name")
    private String originalName;
    @SerializedName("overview")
    private String overview;
    @SerializedName("popularity")
    private Double popularity;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("production_companies")
    private List<Object> productionCompanies = null;
    @SerializedName("seasons")
    private List<Season> seasons = null;
    @SerializedName("status")
    private String status;
    @SerializedName("type")
    private String type;
    @SerializedName("vote_average")
    private float voteAverage;
    @SerializedName("vote_count")
    private Integer voteCount;

    public String getBackdropPath() {
        return backdropPath;
    }

    public List<Object> getEpisodeRunTime() {
        return episodeRunTime;
    }

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public String getHomepage() {
        return homepage;
    }

    public Integer getId() {
        return id;
    }

    public Boolean getInProduction() {
        return inProduction;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public String getLastAirDate() {
        return lastAirDate;
    }

    public String getName() {
        return name;
    }

    public List<Object> getNetworks() {
        return networks;
    }

    public Integer getNumberOfEpisodes() {
        return numberOfEpisodes;
    }

    public Integer getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public List<Object> getOriginCountry() {
        return originCountry;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getOriginalName() {
        return originalName;
    }

    public String getOverview() {
        return overview;
    }

    public Double getPopularity() {
        return popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public List<Object> getProductionCompanies() {
        return productionCompanies;
    }

    public List<Season> getSeasons() {
        return seasons;
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    @Override
    public String toString() {
        return "TvSeriesDetails{" +
                "backdropPath='" + backdropPath + '\'' +
                ", episodeRunTime=" + episodeRunTime +
                ", firstAirDate='" + firstAirDate + '\'' +
                ", genres=" + genres +
                ", homepage='" + homepage + '\'' +
                ", id=" + id +
                ", inProduction=" + inProduction +
                ", languages=" + languages +
                ", lastAirDate='" + lastAirDate + '\'' +
                ", name='" + name + '\'' +
                ", networks=" + networks +
                ", numberOfEpisodes=" + numberOfEpisodes +
                ", numberOfSeasons=" + numberOfSeasons +
                ", originCountry=" + originCountry +
                ", originalLanguage='" + originalLanguage + '\'' +
                ", originalName='" + originalName + '\'' +
                ", overview='" + overview + '\'' +
                ", popularity=" + popularity +
                ", posterPath='" + posterPath + '\'' +
                ", productionCompanies=" + productionCompanies +
                ", seasons=" + seasons +
                ", status='" + status + '\'' +
                ", type='" + type + '\'' +
                ", voteAverage=" + voteAverage +
                ", voteCount=" + voteCount +
                '}';
    }
}