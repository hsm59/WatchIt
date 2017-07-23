package com.hussainmukadam.watchit.mainpage;

import com.hussainmukadam.watchit.BasePresenter;
import com.hussainmukadam.watchit.BaseView;
import com.hussainmukadam.watchit.mainpage.model.Movie;

import java.util.List;

/**
 * Created by hussain on 7/21/17.
 */

public interface MainMVPContract {

    interface View extends BaseView<Presenter>{

        void showProgress();

        void hideProgress();

        void displayMoviesCards(List<Movie> movieList);

        void showError(String errorMessage);

    }

    interface Presenter extends BasePresenter{

        void fetchMoviesBasedOnGenres();

    }

}
