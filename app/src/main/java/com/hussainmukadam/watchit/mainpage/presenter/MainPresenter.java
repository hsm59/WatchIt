package com.hussainmukadam.watchit.mainpage.presenter;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.hussainmukadam.watchit.BuildConfig;
import com.hussainmukadam.watchit.intropage.model.Genre;
import com.hussainmukadam.watchit.intropage.model.GenreResponse;
import com.hussainmukadam.watchit.mainpage.MainMVPContract;
import com.hussainmukadam.watchit.mainpage.model.Movie;
import com.hussainmukadam.watchit.mainpage.model.MovieResponse;
import com.hussainmukadam.watchit.mainpage.model.TvResponse;
import com.hussainmukadam.watchit.mainpage.model.TvSeries;
import com.hussainmukadam.watchit.network.ApiClient;
import com.hussainmukadam.watchit.network.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hussain on 7/23/17.
 */

public class MainPresenter implements MainMVPContract.Presenter {
    private static final String TAG = "MainPresenter";
    private MainMVPContract.View mView;
    private Realm realm = null;

    public MainPresenter(MainMVPContract.View mView) {
        this.mView = mView;
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void fetchFirstPageMoviesByGenres(String genreIdsByMovies, int pageNumber) {


        Log.d(TAG, "fetchMoviesBasedOnGenres: GenreIds " + genreIdsByMovies);
        mView.showProgress();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<MovieResponse> call = apiService.getMoviesByGenre(BuildConfig.apiKey, pageNumber, genreIdsByMovies, "en-US", "popularity.desc");

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                List<Movie> newMovieList = new ArrayList<>();
                int totalPages = 0;

                Log.d(TAG, "onResponse: Response Code " + response.code());
                if (response.isSuccessful()) {
                    if (response.body().getMoviesList().size() != 0) {
                        mView.hideProgress();
                        totalPages = response.body().getTotalPages();

                        realm = Realm.getDefaultInstance();
                        RealmResults<Movie> allMoviesResults = realm.where(Movie.class).findAll();

                        List<Movie> existingMovieList = new ArrayList<>(allMoviesResults);
                        newMovieList = response.body().getMoviesList();

                        for (Movie e : existingMovieList) {
                            if (newMovieList.contains(e)) {
                                newMovieList.remove(e);
                            }
                        }


                        Log.d(TAG, "onResponse: Existing Movies List " + existingMovieList.size());
                        Log.d(TAG, "onResponse: Movies List " + newMovieList.size());
                    } else {
                        mView.showMovieResponseError("Couldn't find any movies");
                        mView.hideProgress();
                    }
                } else {
                    mView.showMovieResponseError("Some Error Occurred");
                    mView.hideProgress();
                }

                mView.displayFirstPageMovies(newMovieList, totalPages);
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                mView.hideProgress();
                mView.showMovieResponseError(t.getMessage());
                Log.d(TAG, "onFailure: Failure Occurred " + t.getMessage());
            }
        });
    }

    @Override
    public void fetchNextPageMoviesByGenres(String genreIdsByMovies, int pageNumber) {

        Log.d(TAG, "fetchNextPageMoviesByGenres: GenreIds " + genreIdsByMovies + "Page Number "+pageNumber);
        mView.showProgress();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<MovieResponse> call = apiService.getMoviesByGenre(BuildConfig.apiKey, pageNumber, genreIdsByMovies, "en-US", "popularity.desc");

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                Log.d(TAG, "onResponse: Response Code " + response.code());
                if (response.isSuccessful()) {
                    if (response.body().getMoviesList().size() != 0) {
                        mView.hideProgress();

                        realm = Realm.getDefaultInstance();
                        RealmResults<Movie> allMoviesResults = realm.where(Movie.class).findAll();
                        Log.d(TAG, "onResponse: Realm Data Stored "+allMoviesResults.size());
                        List<Movie> existingMovieList = new ArrayList<>(allMoviesResults);

                        List<Movie> newMovieList = response.body().getMoviesList();

                        for (Movie e : existingMovieList) {
                            if (newMovieList.contains(e)) {
                                newMovieList.remove(e);
                            }
                        }

                        mView.displayNextPageMovies(newMovieList);
                        Log.d(TAG, "onResponse: Existing Movies List " + existingMovieList.size());
                        Log.d(TAG, "onResponse: Movies List " + newMovieList.size());
                    } else {
                        mView.showMovieResponseError("Couldn't find any movies");
                        mView.hideProgress();
                    }
                } else {
                    mView.showMovieResponseError("Some Error Occurred");
                    mView.hideProgress();
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                mView.hideProgress();
                mView.showMovieResponseError(t.getMessage());
                Log.d(TAG, "onFailure: Failure Occurred " + t.getMessage());
            }
        });
    }

    @Override
    public void storeMovieData(boolean isWatchLater, final Movie movie) {

        movie.setWatchLater(isWatchLater);
        Log.d(TAG, "storeMovieData: Inside Store Movie Data " + isWatchLater);
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                Realm realm = null;
                String status;

                try { // I could use try-with-resources here
                    realm = Realm.getDefaultInstance();
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.insertOrUpdate(movie);
                        }
                    });

                    RealmQuery<Movie> realmQuery = realm.where(Movie.class).findAll().where().equalTo("isWatchLater", true);
                    Log.d(TAG, "execute: First Movie Saved " + realmQuery.count());

                    status = "Movie stored Count " + realmQuery.count();
                } finally {
                    if (realm != null) {
                        realm.close();
                    }
                }
                return status;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                Log.d(TAG, "onPostExecute: Stored Movie Data "+result);
            }
        }.execute();
    }

    @Override
    public void fetchFirstPageTvSeriesByGenres(String genreIdByTv, int pageNumber) {
        Log.d(TAG, "fetchMoviesBasedOnGenres: GenreIds " + genreIdByTv);
        mView.showProgress();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<TvResponse> call = apiService.getTvSeriesByGenre(BuildConfig.apiKey, pageNumber, genreIdByTv, "en-US", "popularity.desc");

        call.enqueue(new Callback<TvResponse>() {
            @Override
            public void onResponse(Call<TvResponse> call, Response<TvResponse> response) {
                List<TvSeries> newTvList = new ArrayList<>();
                int totalPages = 0;

                Log.d(TAG, "onResponse: Response Code " + response.code());
                if (response.isSuccessful()) {
                    if (response.body().getTvList().size() != 0) {
                        mView.hideProgress();
                        totalPages = response.body().getTotalPages();

                        realm = Realm.getDefaultInstance();
                        RealmResults<TvSeries> allTvResults = realm.where(TvSeries.class).findAll();

                        List<TvSeries> existingTvList = new ArrayList<>(allTvResults);
                        newTvList = response.body().getTvList();

                        for (TvSeries e : existingTvList) {
                            if (newTvList.contains(e)) {
                                newTvList.remove(e);
                            }
                        }


                        Log.d(TAG, "onResponse: Existing Movies List " + existingTvList.size());
                        Log.d(TAG, "onResponse: Movies List " + newTvList.size());
                    } else {
                        mView.showTvSeriesResponseError("Couldn't find any tv series");
                        mView.hideProgress();
                    }
                } else {
                    mView.showTvSeriesResponseError("Some Error Occurred");
                    mView.hideProgress();
                }

                mView.displayFirstPageTvSeries(newTvList, totalPages);
            }

            @Override
            public void onFailure(Call<TvResponse> call, Throwable t) {
                mView.hideProgress();
                mView.showTvSeriesResponseError(t.getMessage());
                Log.d(TAG, "onFailure: Failure Occurred " + t.getMessage());
            }
        });
    }

    @Override
    public void fetchNextPageTvSeriesByGenres(String genreIdByTv, int pageNumber) {
        Log.d(TAG, "fetchNextPageMoviesByGenres: GenreIds " + genreIdByTv + "Page Number "+pageNumber);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<TvResponse> call = apiService.getTvSeriesByGenre(BuildConfig.apiKey, pageNumber, genreIdByTv, "en-US", "popularity.desc");

        call.enqueue(new Callback<TvResponse>() {
            @Override
            public void onResponse(Call<TvResponse> call, Response<TvResponse> response) {
                Log.d(TAG, "onResponse: Response Code " + response.code());
                if (response.isSuccessful()) {
                    if (response.body().getTvList().size() != 0) {

                        realm = Realm.getDefaultInstance();
                        RealmResults<TvSeries> allTvsResults = realm.where(TvSeries.class).findAll();
                        Log.d(TAG, "onResponse: Realm Data Stored "+allTvsResults.size());
                        List<TvSeries> existingTvList = new ArrayList<>(allTvsResults);

                        List<TvSeries> newTvList = response.body().getTvList();

                        for (TvSeries e : existingTvList) {
                            if (newTvList.contains(e)) {
                                newTvList.remove(e);
                            }
                        }

                        mView.displayNextPageTvSeries(newTvList);
                        Log.d(TAG, "onResponse: Existing Tv List " + existingTvList.size());
                        Log.d(TAG, "onResponse: Tv List " + newTvList.size());
                    } else {
                        mView.showTvSeriesResponseError("Couldn't find any tv series");
                    }
                } else {
                    mView.showTvSeriesResponseError("Some Error Occurred");
                }
            }

            @Override
            public void onFailure(Call<TvResponse> call, Throwable t) {
                mView.showTvSeriesResponseError(t.getMessage());
                Log.d(TAG, "onFailure: Failure Occurred " + t.getMessage());
            }
        });
    }

    @Override
    public void storeTvData(boolean isWatchLater, final TvSeries tvSeries) {

        tvSeries.setWatchLater(isWatchLater);
        Log.d(TAG, "storeTvData: Inside Store TvSeries Data " + isWatchLater);
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                Realm realm = null;
                String status;

                try { // I could use try-with-resources here
                    realm = Realm.getDefaultInstance();
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.insertOrUpdate(tvSeries);
                        }
                    });

                    RealmQuery<TvSeries> realmQuery = realm.where(TvSeries.class).findAll().where().equalTo("isWatchLater", true);
                    Log.d(TAG, "execute: First TV Saved " + realmQuery.count());

                    status = "TV stored Count " + realmQuery.count();
                } finally {
                    if (realm != null) {
                        realm.close();
                    }
                }
                return status;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                Log.d(TAG, "onPostExecute: Stored TV Data "+result);
            }
        }.execute();
    }


}
