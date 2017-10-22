package com.hussainmukadam.watchit.intropage.presenter;

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
 * Created by hussain on 18/10/17.
 */

public class IntroTvPresenter implements IntroMVPContract.IntroTvPresenter {
    private static final String TAG = "IntroTvPresenter";
    private IntroMVPContract.IntroTvView mView;

    public IntroTvPresenter(IntroMVPContract.IntroTvView mView){
        this.mView = mView;
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void fetchGenresByTV() {
        mView.showProgress();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<GenreResponse> call = apiService.getGenreByTV(BuildConfig.apiKey);

        call.enqueue(new Callback<GenreResponse>() {
            @Override
            public void onResponse(Call<GenreResponse> call, Response<GenreResponse> response) {
                Log.d(TAG, "onResponse: Response Code " + response.code());
                if (response.isSuccessful()) {
                    if (response.body().getGenreResults().size() != 0) {
                        mView.hideProgress();
                        List<Genre> mGenreListRetrofit = response.body().getGenreResults();
                        mView.displayGenresByTV(mGenreListRetrofit);
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
