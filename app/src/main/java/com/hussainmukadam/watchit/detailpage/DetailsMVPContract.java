package com.hussainmukadam.watchit.detailpage;

import com.hussainmukadam.watchit.BasePresenter;
import com.hussainmukadam.watchit.BaseView;
import com.hussainmukadam.watchit.detailpage.model.MovieDetails;
import com.hussainmukadam.watchit.detailpage.model.TvSeriesDetails;

/**
 * Created by hussain on 18/11/17.
 */

public interface DetailsMVPContract {

    interface DetailsView extends BaseView<DetailsBasePresenter>{

        void showProgress(boolean isLoading);

        void showError(String error);

        void displayMovieDetails(MovieDetails movieDetails);

        void displayTvDetails(TvSeriesDetails tvSeriesDetails);
    }

    interface DetailsBasePresenter extends BasePresenter{

        void fetchMovieDetails(int movieId);

        void fetchTvSeriesDetails(int tvSeriesId);
    }

}
