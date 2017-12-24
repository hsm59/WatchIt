package com.hussainmukadam.watchit.mainpage;

import com.hussainmukadam.watchit.BasePresenter;
import com.hussainmukadam.watchit.BaseView;
import com.hussainmukadam.watchit.mainpage.model.Movie;
import com.hussainmukadam.watchit.mainpage.model.TvSeries;

import java.util.List;

/**
 * Created by hussain on 7/21/17.
 */

public interface MainMVPContract {

    interface View extends BaseView<Presenter>{

        void showProgress();

        void hideProgress();

        void displayNextPageMovies(List<Movie> movieList, int totalPages);

        void displayNextPageTvSeries(List<TvSeries> tvSeriesList, int totalPages);

        void showMovieResponseError(String errorMessage);

        void showTvSeriesResponseError(String errorMessage);
    }

    interface Presenter extends BasePresenter{

        void fetchFirstPageMoviesByGenres(String genreIdsByMovies, int pageNumber);

        void fetchNextPageMoviesByGenres(String genreIdsByMovies, int pageNumber);

        void storeMovieData(boolean isWatchLater, Movie movie);

        void fetchFirstPageTvSeriesByGenres(String genreIdByTv, int pageNumber);

        void fetchNextPageTvSeriesByGenres(String genreIdByTv, int pageNumber);

        void storeTvData(boolean isWatchLater, TvSeries tvSeries);

    }

}
