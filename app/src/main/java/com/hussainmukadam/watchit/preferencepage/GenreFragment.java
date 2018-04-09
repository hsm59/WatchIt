package com.hussainmukadam.watchit.preferencepage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.hussainmukadam.watchit.R;
import com.hussainmukadam.watchit.intropage.IntroMVPContract;
import com.hussainmukadam.watchit.intropage.adapter.MoviesGenreAdapter;
import com.hussainmukadam.watchit.intropage.adapter.TvGenreAdapter;
import com.hussainmukadam.watchit.intropage.model.Genre;
import com.hussainmukadam.watchit.intropage.presenter.IntroMoviePresenter;
import com.hussainmukadam.watchit.intropage.presenter.IntroTvPresenter;
import com.hussainmukadam.watchit.util.CustomSharedPreference;
import com.hussainmukadam.watchit.util.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by hussain on 25/03/18.
 */

public class GenreFragment extends Fragment implements IntroMVPContract.IntroMoviesView, MoviesGenreAdapter.MoviesListItemClickListener, IntroMVPContract.IntroTvView, TvGenreAdapter.TvListItemClickListener {
    private static final String TAG = "GenreFragment";
    @BindView(R.id.pb_genre_pref)
    ProgressBar progressBar;
    @BindView(R.id.rv_genre_movies_pref)
    RecyclerView rvGenreMovies;
    @BindView(R.id.rv_genre_tv_pref)
    RecyclerView rvGenreTv;
    @BindView(R.id.toolbar_main)
    Toolbar toolbarMain;

    Unbinder unbinder;
    CustomSharedPreference customSharedPreference;
    MoviesGenreAdapter moviesGenreAdapter;
    TvGenreAdapter tvGenreAdapter;
    IntroMoviePresenter moviePresenter;
    IntroTvPresenter tvPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_genre_preference, container, false);
        unbinder = ButterKnife.bind(this, view);

        toolbarMain.setTitle("Settings");
        toolbarMain.setTitleTextColor(getResources().getColor(R.color.colorWhite));

        customSharedPreference = new CustomSharedPreference(getContext());

        toolbarMain.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customSharedPreference.getMoviesGenrePreference().size() >= 3 && customSharedPreference.getTvGenrePreference().size() >= 3) {
                    if (getActivity() != null)
                        getActivity().onBackPressed();
                } else
                    Toast.makeText(getContext(), "Please select 3 genres in each category, for better suggestions!", Toast.LENGTH_SHORT).show();
            }
        });

        moviePresenter = new IntroMoviePresenter(this);
        tvPresenter = new IntroTvPresenter(this);

        if (Util.isConnected(getContext())) {
            setupRecyclerView();
            moviePresenter.fetchGenresByMovies();
            tvPresenter.fetchGenresByTV();
        } else {
            Toast.makeText(getContext(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (customSharedPreference.getMoviesGenrePreference().size() >= 3 && customSharedPreference.getTvGenrePreference().size() >= 3) {
                        if (getActivity() != null)
                            getActivity().onBackPressed();
                    } else
                        Toast.makeText(getContext(), "Please select 3 genres in each category, for better suggestions!", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });

        return view;
    }

    private void setupRecyclerView() {
        FlexboxLayoutManager movieLayoutManager = new FlexboxLayoutManager(getContext());
        movieLayoutManager.setJustifyContent(JustifyContent.SPACE_AROUND);

        FlexboxLayoutManager tvLayoutManager = new FlexboxLayoutManager(getContext());
        tvLayoutManager.setJustifyContent(JustifyContent.SPACE_AROUND);

        rvGenreMovies.setLayoutManager(movieLayoutManager);
        rvGenreTv.setLayoutManager(tvLayoutManager);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void displayGenresByMovies(List<Genre> genreMovieList) {
        moviesGenreAdapter = new MoviesGenreAdapter(genreMovieList, this);
        rvGenreMovies.setAdapter(moviesGenreAdapter);
        moviesGenreAdapter.notifyDataSetChanged();
    }

    @Override
    public void displayGenresByTV(List<Genre> genreTVList) {
        tvGenreAdapter = new TvGenreAdapter(genreTVList, this);
        rvGenreTv.setAdapter(tvGenreAdapter);
        tvGenreAdapter.notifyDataSetChanged();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        if (progressBar != null)
            progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showError(String errorMessage) {
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMovieListItemSelect(Genre genre) {
        customSharedPreference.addMoviesGenrePreference(genre);
    }

    @Override
    public void onMovieListItemUnselect(Genre genre) {
        customSharedPreference.deleteMoviesGenrePreference(genre);
    }

    @Override
    public void onTvListItemSelected(Genre genre) {
        customSharedPreference.addTvGenrePreference(genre);
    }

    @Override
    public void onTvListItemUnSelected(Genre genre) {
        customSharedPreference.deleteTvGenrePreference(genre);
    }

    @Override
    public void setPresenter(Object presenter) {
        if (presenter instanceof IntroMoviePresenter)
            this.moviePresenter = (IntroMoviePresenter) presenter;
        else
            this.tvPresenter = (IntroTvPresenter) presenter;
    }
}
