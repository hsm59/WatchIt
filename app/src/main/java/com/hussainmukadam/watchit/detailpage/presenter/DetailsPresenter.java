package com.hussainmukadam.watchit.detailpage.presenter;

import android.util.Log;

import com.hussainmukadam.watchit.BuildConfig;
import com.hussainmukadam.watchit.detailpage.DetailsMVPContract;
import com.hussainmukadam.watchit.detailpage.model.MovieDetails;
import com.hussainmukadam.watchit.detailpage.model.TvSeriesDetails;
import com.hussainmukadam.watchit.network.ApiClient;
import com.hussainmukadam.watchit.network.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hussain on 18/11/17.
 */

public class DetailsPresenter implements DetailsMVPContract.DetailsBasePresenter{
    private static final String TAG = "DetailsMoviePresenter";
    private DetailsMVPContract.DetailsView mView;

    public DetailsPresenter(DetailsMVPContract.DetailsView detailsView){
        this.mView = detailsView;
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void fetchMovieDetails(int movieId) {

        mView.showProgress(true);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<MovieDetails> call = apiService.getMovieDetails(movieId, BuildConfig.apiKey);
        call.enqueue(new Callback<MovieDetails>() {
            @Override
            public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response) {
                Log.d(TAG, "onResponse: "+response.body());
                mView.showProgress(false);
                mView.displayMovieDetails(response.body());
            }

            @Override
            public void onFailure(Call<MovieDetails> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
                mView.showProgress(false);
                mView.showError(t.getMessage());
            }
        });

    }

    @Override
    public void fetchTvSeriesDetails(int tvSeriesId) {

        mView.showProgress(true);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<TvSeriesDetails> call = apiService.getTvSeriesDetails(tvSeriesId, BuildConfig.apiKey);
        call.enqueue(new Callback<TvSeriesDetails>() {
            @Override
            public void onResponse(Call<TvSeriesDetails> call, Response<TvSeriesDetails> response) {
                Log.d(TAG, "onResponse: "+response.body());
                mView.showProgress(false);
                mView.displayTvDetails(response.body());
            }

            @Override
            public void onFailure(Call<TvSeriesDetails> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
                mView.showProgress(false);
                mView.showError(t.getMessage());
            }
        });

    }
}
