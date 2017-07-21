package com.hussainmukadam.watchit.intropage;

import com.hussainmukadam.watchit.BasePresenter;
import com.hussainmukadam.watchit.BaseView;
import com.hussainmukadam.watchit.intropage.model.Genre;

import java.util.List;

/**
 * Created by hussain on 7/21/17.
 */

public interface IntroMVPContract {

    interface View extends BaseView<Presenter>{

        void displayGenresByTV(List<Genre> genreTVList);

        void displayGenresByMovies(List<Genre> genreMoviesList);

        void showProgress();

        void hideProgress();

        void showError(String errorMessage);
    }



    interface Presenter extends BasePresenter{

        void fetchGenresByMovies();

        void fetchGenresByTV();
    }
}
