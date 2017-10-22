package com.hussainmukadam.watchit.intropage.presenter;

import android.content.Context;
import android.util.Log;

import com.hussainmukadam.watchit.BuildConfig;
import com.hussainmukadam.watchit.intropage.IntroMVPContract;
import com.hussainmukadam.watchit.intropage.model.Genre;
import com.hussainmukadam.watchit.intropage.model.GenreResponse;
import com.hussainmukadam.watchit.network.ApiClient;
import com.hussainmukadam.watchit.network.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hussain on 7/21/17.
 */

public class IntroMoviePresenter implements IntroMVPContract.IntroMoviesPresenter {
    private static final String TAG = "IntroMoviePresenter";
    private IntroMVPContract.IntroMoviesView mView;

    public IntroMoviePresenter(IntroMVPContract.IntroMoviesView view) {
        this.mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void fetchGenresByMovies() {
        mView.showProgress();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<GenreResponse> call = apiService.getGenreByMovies(BuildConfig.apiKey);

        call.enqueue(new Callback<GenreResponse>() {
            @Override
            public void onResponse(Call<GenreResponse> call, Response<GenreResponse> response) {
                Log.d(TAG, "onResponse: Response Code " + response.code());
                if (response.isSuccessful()) {
                    if (response.body().getGenreResults().size() != 0) {
                        mView.hideProgress();
                        List<Genre> mGenreListRetrofit = response.body().getGenreResults();
                        mView.displayGenresByMovies(mGenreListRetrofit);
                        Log.d(TAG, "onResponse: Songs List " + mGenreListRetrofit.size());
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
            public void onFailure(Call<GenreResponse> call, Throwable t) {
                mView.hideProgress();
                mView.showError(t.getMessage());
                Log.d(TAG, "onFailure: Failure Occurred " + t.getMessage());
            }
        });
    }
}
