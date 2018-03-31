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
import com.hussainmukadam.watchit.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import okhttp3.ResponseBody;
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
    public void fetchFirstPageMoviesByGenres(final String genreIdsByMovies, int pageNumber) {

        Util.debugLog(TAG, "fetchMoviesBasedOnGenres: GenreIds " + genreIdsByMovies);
        mView.showProgress();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<MovieResponse> call = apiService.getMoviesByGenre(BuildConfig.apiKey, pageNumber, genreIdsByMovies, "en-US", "popularity.desc");

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                List<Movie> newMovieList = new ArrayList<>();
                int totalPages = 0;
                int nextPg = 0;

                Util.debugLog(TAG, "onResponse: Response Code " + response.code());
                if (response.isSuccessful()) {
                    if (response.body().getMoviesList().size() != 0) {
                        mView.hideProgress();
                        totalPages = response.body().getTotalPages();

                        if (totalPages > 1) {
                            Random random = new Random();
                            nextPg = random.nextInt(totalPages) + 1;
                            Util.debugLog(TAG, " NextPageNo: " + nextPg);
                            fetchNextPageMoviesByGenres(genreIdsByMovies, nextPg);
                        } else {
                            mView.showMovieResponseError("Not enough Pages");
                        }
                    } else {
                        mView.showMovieResponseError("Couldn't find any movies");
                        mView.hideProgress();
                    }
                } else {
                    mView.showMovieResponseError("Some Error Occurred");
                    mView.hideProgress();
                }

                // mView.displayFirstPageMovies(newMovieList, totalPages);
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                mView.hideProgress();
                mView.showMovieResponseError(t.getMessage());
                Util.debugLog(TAG, "onFailure: Failure Occurred " + t.getMessage());
            }
        });
    }

    @Override
    public void fetchNextPageMoviesByGenres(String genreIdsByMovies, int pageNumber) {
        Util.debugLog(TAG, "fetchNextPageMoviesByGenres: GenreIds " + genreIdsByMovies + "Page Number " + pageNumber);
        mView.showProgress();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<MovieResponse> call = apiService.getMoviesByGenre(BuildConfig.apiKey, pageNumber, genreIdsByMovies, "en-US", "popularity.desc");

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                Util.debugLog(TAG, "onResponse: Response Code " + response.code());
                int totalPages = 0;

                if (response.isSuccessful()) {
                    if (response.body().getMoviesList().size() != 0) {
                        mView.hideProgress();
                        totalPages = response.body().getTotalPages();


                        realm = Realm.getDefaultInstance();
                        RealmResults<Movie> allMoviesResults = realm.where(Movie.class).findAll();
                        Util.debugLog(TAG, "onResponse: Realm Data Stored " + allMoviesResults.size());
                        List<Movie> existingMovieList = new ArrayList<>(allMoviesResults);

                        List<Movie> newMovieList = response.body().getMoviesList();

                        for (Movie e : existingMovieList) {
                            if (newMovieList.contains(e)) {
                                newMovieList.remove(e);
                            }
                        }

                        if(newMovieList.size()>1) {
                            mView.displayNextPageMovies(newMovieList, totalPages);
                        } else {
                            mView.showMovieResponseError("No new movies");
                        }


                        Util.debugLog(TAG, "onResponse: Existing Movies List " + existingMovieList.size());
                        Util.debugLog(TAG, "onResponse: Movies List " + newMovieList.size());
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
                Util.debugLog(TAG, "onFailure: Failure Occurred " + t.getMessage());
            }
        });
    }

    @Override
    public void storeMovieData(boolean isWatchLater, final Movie movie) {

        movie.setWatchLater(isWatchLater);

        if (isWatchLater) {
            Util.debugLog(TAG, "storeMovieData: Movie has to be Notified " + isWatchLater);
            movie.setNotified(false);
        } else {
            movie.setNotified(true);
        }

        Util.debugLog(TAG, "storeMovieData: Inside Store Movie Data " + isWatchLater);
        new RealmStoreTask(null, movie, true).execute();
    }

    @Override
    public void fetchFirstPageTvSeriesByGenres(final String genreIdByTv, int pageNumber) {
        Util.debugLog(TAG, "fetchMoviesBasedOnGenres: GenreIds " + genreIdByTv);
        mView.showProgress();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<TvResponse> call = apiService.getTvSeriesByGenre(BuildConfig.apiKey, pageNumber, genreIdByTv, "en-US", "popularity.desc");

        call.enqueue(new Callback<TvResponse>() {
            @Override
            public void onResponse(Call<TvResponse> call, Response<TvResponse> response) {
                int totalPages, nextPg = 0;

                Util.debugLog(TAG, "onResponse: Response Code " + response.code());
                if (response.isSuccessful()) {
                    if (response.body().getTvList().size() != 0) {
                        //mView.hideProgress();
                        totalPages = response.body().getTotalPages();

                        if (totalPages > 0) {
                            Util.debugLog(TAG, "onResponse: Total Page Nubmers " + totalPages);
                            Random random = new Random();
                            nextPg = random.nextInt(totalPages) + 1;
                            Util.debugLog(TAG, "onResponse: Next Page " + nextPg);
                            fetchNextPageTvSeriesByGenres(genreIdByTv, nextPg);
                        } else {
                            Util.debugLog(TAG, "onResponse: Total Page is not more than 0");
                            mView.showTvSeriesResponseError("Not enough Pages");
                        }
                    } else {
                        mView.showTvSeriesResponseError("Couldn't find any tv series");
                        mView.hideProgress();
                    }
                } else {
                    mView.showTvSeriesResponseError("Some Error Occurred");
                    mView.hideProgress();
                }
            }

            @Override
            public void onFailure(Call<TvResponse> call, Throwable t) {
                mView.hideProgress();
                mView.showTvSeriesResponseError(t.getMessage());
                Util.debugLog(TAG, "onFailure: Failure Occurred " + t.getMessage());
            }
        });
    }

    @Override
    public void fetchNextPageTvSeriesByGenres(String genreIdByTv, int pageNumber) {
        Util.debugLog(TAG, "fetchNextPageMoviesByGenres: GenreIds " + genreIdByTv + "Page Number " + pageNumber);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<TvResponse> call = apiService.getTvSeriesByGenre(BuildConfig.apiKey, pageNumber, genreIdByTv, "en-US", "popularity.desc");

        call.enqueue(new Callback<TvResponse>() {
            @Override
            public void onResponse(Call<TvResponse> call, Response<TvResponse> response) {
                Util.debugLog(TAG, "onResponse: Response Code " + response.code());
                if (response.isSuccessful()) {
                    mView.hideProgress();
                    if (response.body().getTvList().size() != 0) {
                        int totalPages = response.body().getTotalPages();

                        realm = Realm.getDefaultInstance();
                        RealmResults<TvSeries> allTvsResults = realm.where(TvSeries.class).findAll();
                        Util.debugLog(TAG, "onResponse: Realm Data Stored " + allTvsResults.size());
                        List<TvSeries> existingTvList = new ArrayList<>(allTvsResults);

                        List<TvSeries> newTvList = response.body().getTvList();

                        for (TvSeries e : existingTvList) {
                            if (newTvList.contains(e)) {
                                newTvList.remove(e);
                            }
                        }

                        if (newTvList.size() > 1) {
                            Util.debugLog(TAG, "onResponse: When the New TV List size is more than 1");
                            mView.displayNextPageTvSeries(newTvList, totalPages);
                        } else {
                            Util.debugLog(TAG, "onResponse: When the new TV List size is not more than 1");
                            mView.showTvSeriesResponseError("Try Refreshing again");
                        }

                        Util.debugLog(TAG, "onResponse: Existing Tv List " + existingTvList.size());
                        Util.debugLog(TAG, "onResponse: Tv List " + newTvList.size());
                    } else {
                        Util.debugLog(TAG, "onResponse: When TV List Size is equal to 0");
                        mView.showTvSeriesResponseError("Couldn't find any tv series");
                        mView.hideProgress();
                    }
                } else {
                    Util.debugLog(TAG, "onResponse: When TV List Response is not successful");
                    mView.showTvSeriesResponseError("Some Error Occurred");
                    mView.hideProgress();
                }
            }

            @Override
            public void onFailure(Call<TvResponse> call, Throwable t) {
                mView.showTvSeriesResponseError(t.getMessage());
                Util.debugLog(TAG, "onFailure: Failure Occurred " + t.getMessage());
            }
        });
    }

    @Override
    public void storeTvData(boolean isWatchLater, final TvSeries tvSeries) {

        tvSeries.setWatchLater(isWatchLater);

        if (isWatchLater) {
            tvSeries.setNotified(false);
        } else {
            tvSeries.setNotified(true);
        }

        Util.debugLog(TAG, "storeTvData: Inside Store TvSeries Data " + isWatchLater);
        new RealmStoreTask(tvSeries, null, false).execute();
    }

    @Override
    public void fetchFirstPageTopRatedMovies(int pageNumber) {
        mView.showProgress();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<MovieResponse> call = apiService.getTopRatedMovies(BuildConfig.apiKey, pageNumber, "en-US");

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                List<Movie> newMovieList = new ArrayList<>();
                int totalPages = 0;
                int nextPg = 0;

                Util.debugLog(TAG, "onResponse: Response Code " + response.code());
                if (response.isSuccessful()) {
                    if (response.body().getMoviesList().size() != 0) {
                        mView.hideProgress();
                        totalPages = response.body().getTotalPages();

                        if (totalPages > 1) {
                            Random random = new Random();
                            nextPg = random.nextInt(totalPages) + 1;
                            Util.debugLog(TAG, " NextPageNo: " + nextPg);
                            fetchNextPageTopRatedMovies(nextPg);
                        } else {
                            mView.showMovieResponseError("Not enough Pages");
                        }
                    } else {
                        mView.showMovieResponseError("Couldn't find any movies");
                        mView.hideProgress();
                    }
                } else {
                    mView.showMovieResponseError("Some Error Occurred");
                    mView.hideProgress();
                }

                // mView.displayFirstPageMovies(newMovieList, totalPages);
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                mView.hideProgress();
                mView.showMovieResponseError(t.getMessage());
                Util.debugLog(TAG, "onFailure: Failure Occurred " + t.getMessage());
            }
        });
    }

    @Override
    public void fetchNextPageTopRatedMovies(int pageNumber) {
        mView.showProgress();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<MovieResponse> call = apiService.getTopRatedMovies(BuildConfig.apiKey, pageNumber, "en-US");

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                Util.debugLog(TAG, "onResponse: Response Code " + response.code());
                int totalPages = 0;

                if (response.isSuccessful()) {
                    if (response.body().getMoviesList().size() != 0) {
                        mView.hideProgress();
                        totalPages = response.body().getTotalPages();


                        realm = Realm.getDefaultInstance();
                        RealmResults<Movie> allMoviesResults = realm.where(Movie.class).findAll();
                        Util.debugLog(TAG, "onResponse: Realm Data Stored " + allMoviesResults.size());
                        List<Movie> existingMovieList = new ArrayList<>(allMoviesResults);

                        List<Movie> newMovieList = response.body().getMoviesList();

                        for (Movie e : existingMovieList) {
                            if (newMovieList.contains(e)) {
                                newMovieList.remove(e);
                            }
                        }

                        if(newMovieList.size()>1) {
                            mView.displayNextPageMovies(newMovieList, totalPages);
                        } else {
                            mView.showMovieResponseError("No new movies");
                        }


                        Util.debugLog(TAG, "onResponse: Existing Movies List " + existingMovieList.size());
                        Util.debugLog(TAG, "onResponse: Movies List " + newMovieList.size());
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
                Util.debugLog(TAG, "onFailure: Failure Occurred " + t.getMessage());
            }
        });
    }

    @Override
    public void fetchFirstPageTopRatedTvSeries(int pageNumber) {
        mView.showProgress();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<TvResponse> call = apiService.getTopRatedTvSeries(BuildConfig.apiKey, pageNumber, "en-US");

        call.enqueue(new Callback<TvResponse>() {
            @Override
            public void onResponse(Call<TvResponse> call, Response<TvResponse> response) {
                int totalPages, nextPg = 0;

                Util.debugLog(TAG, "onResponse: Response Code " + response.code());
                if (response.isSuccessful()) {
                    if (response.body().getTvList().size() != 0) {
                        //mView.hideProgress();
                        totalPages = response.body().getTotalPages();

                        if (totalPages > 0) {
                            Util.debugLog(TAG, "onResponse: Total Page Nubmers " + totalPages);
                            Random random = new Random();
                            nextPg = random.nextInt(totalPages) + 1;
                            Util.debugLog(TAG, "onResponse: Next Page " + nextPg);
                            fetchNextPageTopRatedTvSeries(nextPg);
                        } else {
                            Util.debugLog(TAG, "onResponse: Total Page is not more than 0");
                            mView.showTvSeriesResponseError("Not enough Pages");
                        }
                    } else {
                        mView.showTvSeriesResponseError("Couldn't find any tv series");
                        mView.hideProgress();
                    }
                } else {
                    mView.showTvSeriesResponseError("Some Error Occurred");
                    mView.hideProgress();
                }
            }

            @Override
            public void onFailure(Call<TvResponse> call, Throwable t) {
                mView.hideProgress();
                mView.showTvSeriesResponseError(t.getMessage());
                Util.debugLog(TAG, "onFailure: Failure Occurred " + t.getMessage());
            }
        });
    }

    @Override
    public void fetchNextPageTopRatedTvSeries(int pageNumber) {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<TvResponse> call = apiService.getTopRatedTvSeries(BuildConfig.apiKey, pageNumber, "en-US");

        call.enqueue(new Callback<TvResponse>() {
            @Override
            public void onResponse(Call<TvResponse> call, Response<TvResponse> response) {
                Util.debugLog(TAG, "onResponse: Response Code " + response.code());
                if (response.isSuccessful()) {
                    mView.hideProgress();
                    if (response.body().getTvList().size() != 0) {
                        int totalPages = response.body().getTotalPages();

                        realm = Realm.getDefaultInstance();
                        RealmResults<TvSeries> allTvsResults = realm.where(TvSeries.class).findAll();
                        Util.debugLog(TAG, "onResponse: Realm Data Stored " + allTvsResults.size());
                        List<TvSeries> existingTvList = new ArrayList<>(allTvsResults);

                        List<TvSeries> newTvList = response.body().getTvList();

                        for (TvSeries e : existingTvList) {
                            if (newTvList.contains(e)) {
                                newTvList.remove(e);
                            }
                        }

                        if (newTvList.size() > 1) {
                            Util.debugLog(TAG, "onResponse: When the New TV List size is more than 1");
                            mView.displayNextPageTvSeries(newTvList, totalPages);
                        } else {
                            Util.debugLog(TAG, "onResponse: When the new TV List size is not more than 1");
                            mView.showTvSeriesResponseError("Try Refreshing again");
                        }

                        Util.debugLog(TAG, "onResponse: Existing Tv List " + existingTvList.size());
                        Util.debugLog(TAG, "onResponse: Tv List " + newTvList.size());
                    } else {
                        Util.debugLog(TAG, "onResponse: When TV List Size is equal to 0");
                        mView.showTvSeriesResponseError("Couldn't find any tv series");
                        mView.hideProgress();
                    }
                } else {
                    Util.debugLog(TAG, "onResponse: When TV List Response is not successful");
                    mView.showTvSeriesResponseError("Some Error Occurred");
                    mView.hideProgress();
                }
            }

            @Override
            public void onFailure(Call<TvResponse> call, Throwable t) {
                mView.showTvSeriesResponseError(t.getMessage());
                Util.debugLog(TAG, "onFailure: Failure Occurred " + t.getMessage());
            }
        });
    }

    static class RealmStoreTask extends AsyncTask<Void, Void, String> {
        TvSeries tvSeries;
        Movie movie;
        boolean isMovie;


        private RealmStoreTask(TvSeries tvSeries, Movie movie, boolean isMovie) {
            this.tvSeries = tvSeries;
            this.movie = movie;
            this.isMovie = isMovie;
        }

        @Override
        protected String doInBackground(Void... voids) {
            Realm realm = null;
            String status;

            try { // I could use try-with-resources here
                realm = Realm.getDefaultInstance();

                if (isMovie) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.insertOrUpdate(movie);
                        }
                    });

                    RealmQuery<Movie> realmQuery = realm.where(Movie.class).findAll().where().equalTo("isWatchLater", true);
                    Util.debugLog(TAG, "execute: First Movie Saved " + realmQuery.count());

                    status = "Movie stored Count " + realmQuery.count();
                } else {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.insertOrUpdate(tvSeries);
                        }
                    });

                    RealmQuery<TvSeries> realmQuery = realm.where(TvSeries.class).findAll().where().equalTo("isWatchLater", true);
                    Util.debugLog(TAG, "execute: First TV Saved " + realmQuery.count());

                    status = "TV stored Count " + realmQuery.count();
                }

            } finally {
                if (realm != null) {
                    realm.close();
                }
            }
            return status;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Util.debugLog(TAG, "onPostExecute: Stored TV Data " + s);
        }
    }
}
