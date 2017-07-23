package com.hussainmukadam.watchit.network;

import com.hussainmukadam.watchit.intropage.model.GenreResponse;

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

    //Fetch Movies using /discover/movies - GET - https://api.themoviedb.org/3/discover/movie?api_key=07ebecba1d66dde4bda8ea1a0abbc436&
    //language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1&with_genres=878%2C%2012%2C%2016%2C%2035%2C%2010751
//    @GET("discover/movie")


    //Fetch TV Shows using /discover/tv - GET - Similar to above.
}
