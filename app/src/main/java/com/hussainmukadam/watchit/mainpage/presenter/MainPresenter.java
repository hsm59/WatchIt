package com.hussainmukadam.watchit.mainpage.presenter;

import android.view.View;

import com.hussainmukadam.watchit.mainpage.MainMVPContract;

/**
 * Created by hussain on 7/23/17.
 */

public class MainPresenter implements MainMVPContract.Presenter {
    MainMVPContract.View mView;

    public MainPresenter(MainMVPContract.View mView) {
        this.mView = mView;
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void fetchMoviesBasedOnGenres() {

    }
}
