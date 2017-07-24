package com.hussainmukadam.watchit.mainpage.presenter;

import android.util.Log;
import android.view.View;

import com.hussainmukadam.watchit.BuildConfig;
import com.hussainmukadam.watchit.intropage.model.Genre;
import com.hussainmukadam.watchit.intropage.model.GenreResponse;
import com.hussainmukadam.watchit.mainpage.MainMVPContract;
import com.hussainmukadam.watchit.mainpage.model.Movie;
import com.hussainmukadam.watchit.mainpage.model.MovieResponse;
import com.hussainmukadam.watchit.network.ApiClient;
import com.hussainmukadam.watchit.network.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hussain on 7/23/17.
 */

public class MainPresenter implements MainMVPContract.Presenter {
    private static final String TAG = "MainPresenter";
    MainMVPContract.View mView;

    public MainPresenter(MainMVPContract.View mView) {
        this.mView = mView;
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void fetchMoviesBasedOnGenres(String genreIdsByMovies) {

        Log.d(TAG, "fetchMoviesBasedOnGenres: GenreIds "+genreIdsByMovies);
        mView.showProgress();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<MovieResponse> call = apiService.getMoviesByGenre(BuildConfig.apiKey, 1, genreIdsByMovies);

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                Log.d(TAG, "onResponse: Response Code " + response.code());
                if (response.isSuccessful()) {
                    if (response.body().getMoviesList().size() != 0) {
                        mView.hideProgress();
                        List<Movie> movieList = response.body().getMoviesList();
//                        mView.displayGenresByMovies(mGenreListRetrofit);
                        Log.d(TAG, "onResponse: Songs List " + movieList.size());
                    } else {
                        mView.showError("Couldn't find any movies");
                        mView.hideProgress();
                    }
                } else {
                    mView.showError("Some Error Occurred");
                    mView.hideProgress();
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                mView.hideProgress();
                mView.showError(t.getMessage());
                Log.d(TAG, "onFailure: Failure Occurred " + t.getMessage());
            }
        });
    }
}
