package com.hussainmukadam.watchit.network;

import com.hussainmukadam.watchit.detailpage.model.MovieDetails;
import com.hussainmukadam.watchit.detailpage.model.TvSeriesDetails;
import com.hussainmukadam.watchit.intropage.model.GenreResponse;
import com.hussainmukadam.watchit.mainpage.model.MovieResponse;
import com.hussainmukadam.watchit.mainpage.model.TvResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by hussain on 7/21/17.
 */

public interface ApiInterface {

    //Movie List Genres - GET - https://api.themoviedb.org/3/genre/movie/list?api_key=&language=en-US
    @GET("genre/movie/list")
    Call<GenreResponse> getGenreByMovies(@Query("api_key") String apiKey);

    //TV Shows List Genres - GET - https://api.themoviedb.org/3/genre/tv/list?api_key=&language=en-US
    @GET("genre/tv/list")
    Call<GenreResponse> getGenreByTV(@Query("api_key") String apiKey);

    //https://api.themoviedb.org/3/discover/movie?api_key=&&page=1&with_genres=28%2C37
    @GET("discover/movie")
    Call<MovieResponse> getMoviesByGenre(@Query("api_key") String apiKey, @Query("page") int pageNo, @Query("with_genres") String genres,
                                         @Query("language") String language, @Query("sort_by") String sortBy);

    //Fetch TV Shows using /discover/tv - GET - Similar to above.
    @GET("discover/tv")
    Call<TvResponse> getTvSeriesByGenre(@Query("api_key") String apiKey, @Query("page") int pageNo, @Query("with_genres") String genres,
                                      @Query("language") String language, @Query("sort_by") String sortBy);

    @GET("movie/{movieId}")
    Call<MovieDetails> getMovieDetails(@Path("movieId") int movieId, @Query("api_key") String apiKey);

    @GET("tv/{tv_id}")
    Call<TvSeriesDetails> getTvSeriesDetails(@Path("tv_id") int movieId, @Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Call<MovieResponse> getTopRatedMovies(@Query("api_key") String apiKey, @Query("page") int pageNo, @Query("language") String language);

    @GET("tv/top_rated")
    Call<TvResponse> getTopRatedTvSeries(@Query("api_key") String apiKey, @Query("page") int pageNo, @Query("language") String language);
}
