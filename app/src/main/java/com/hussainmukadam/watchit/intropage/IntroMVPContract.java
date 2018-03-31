package com.hussainmukadam.watchit.intropage;

import com.hussainmukadam.watchit.BasePresenter;
import com.hussainmukadam.watchit.BaseView;
import com.hussainmukadam.watchit.intropage.model.Genre;

import java.util.List;

/**
 * Created by hussain on 7/21/17.
 */

public interface IntroMVPContract {

    interface IntroMoviesView extends BaseView<Object>{

        void displayGenresByMovies(List<Genre> genreMovieList);

        void showProgress();

        void hideProgress();

        void showError(String errorMessage);
    }

    interface IntroTvView extends BaseView<Object>{

        void displayGenresByTV(List<Genre> genreTVList);

        void showProgress();

        void hideProgress();

        void showError(String errorMessage);
    }

    interface IntroMoviesPresenter extends BasePresenter{

        void fetchGenresByMovies();
    }

    interface IntroTvPresenter extends BasePresenter{

        void fetchGenresByTV();
    }
}
