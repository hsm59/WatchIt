package com.hussainmukadam.watchit.network;

import com.hussainmukadam.watchit.intropage.model.GenreResponse;
import com.hussainmukadam.watchit.mainpage.model.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by hussain on 7/21/17.
 */

public interface ApiInterface {

    //Movie List Genres - GET - https://api.themoviedb.org/3/genre/movie/list?api_key=07ebecba1d66dde4bda8ea1a0abbc436&language=en-US
    @GET("genre/movie/list")
    Call<GenreResponse> getGenreByMovies(@Query("api_key") String apiKey);

    //TV Shows List Genres - GET - https://api.themoviedb.org/3/genre/tv/list?api_key=07ebecba1d66dde4bda8ea1a0abbc436&language=en-US
    @GET("genre/tv/list")
    Call<GenreResponse> getGenreByTV(@Query("api_key") String apiKey);

    //https://api.themoviedb.org/3/discover/movie?api_key=07ebecba1d66dde4bda8ea1a0abbc436&&page=1&with_genres=28%2C37
    @GET("discover/movie")
    Call<MovieResponse> getMoviesByGenre(@Query("api_key") String apiKey, @Query("page") int pageNo, @Query("with_genres") String genres,
                                         @Query("language") String language, @Query("sort_by") String sortBy);

    //Fetch TV Shows using /discover/tv - GET - Similar to above.
}
