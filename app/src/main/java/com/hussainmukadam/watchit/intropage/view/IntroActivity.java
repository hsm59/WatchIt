package com.hussainmukadam.watchit.intropage.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.hussainmukadam.watchit.R;
import com.hussainmukadam.watchit.intropage.IntroMVPContract;
import com.hussainmukadam.watchit.intropage.adapter.MoviesGenreAdapter;
import com.hussainmukadam.watchit.intropage.adapter.TvGenreAdapter;
import com.hussainmukadam.watchit.intropage.model.Genre;
import com.hussainmukadam.watchit.intropage.presenter.IntroPresenter;
import com.hussainmukadam.watchit.mainpage.BaseActivity;
import com.hussainmukadam.watchit.util.CustomSharedPreference;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hussain on 7/21/17.
 */

public class IntroActivity extends AppCompatActivity implements IntroMVPContract.View, MoviesGenreAdapter.MoviesListItemClickListener, TvGenreAdapter.TvListItemClickListener {
    private static final String TAG = "IntroActivity";
    IntroMVPContract.Presenter presenter;
    IntroPresenter introPresenter;
    MoviesGenreAdapter moviesGenreAdapter;
    TvGenreAdapter tvGenreAdapter;
    List<Genre> selectedTvGenres = new ArrayList<>();
    List<Genre> selectedMoviesGenres = new ArrayList<>();
    CustomSharedPreference customSharedPreference;

    @BindView(R.id.rv_genre_movies)
    RecyclerView rvGenreMovies;
    @BindView(R.id.rv_genre_tv)
    RecyclerView rvGenreTVShows;
    @BindView(R.id.btn_get_started)
    Button btnGetStarted;
    @BindView(R.id.intro_progress_bar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ButterKnife.bind(this);

        customSharedPreference = new CustomSharedPreference(this);

        if (customSharedPreference.isIntroDisplayed()) {
            Intent startMainActivity = new Intent(IntroActivity.this, BaseActivity.class);
            startActivity(startMainActivity);
            finish();
        } else {
            setupRecyclerView();
            introPresenter = new IntroPresenter(this);
            introPresenter.fetchGenresByMovies();
            introPresenter.fetchGenresByTV();
        }

        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTvGenres.size() >= 3 && selectedMoviesGenres.size() >= 3) {
                    customSharedPreference.setTvGenrePreference(selectedTvGenres);
                    customSharedPreference.setMoviesGenrePreference(selectedMoviesGenres);

                    customSharedPreference.setIntroDisplayed(true);
                    Intent startMainActivity = new Intent(IntroActivity.this, BaseActivity.class);
                    startActivity(startMainActivity);
                } else {
                    Toast.makeText(IntroActivity.this, "Select at least 3 Genres from each", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void setPresenter(IntroMVPContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void displayGenresByTV(List<Genre> genreTVList) {
        tvGenreAdapter = new TvGenreAdapter(genreTVList, this);
        rvGenreTVShows.setAdapter(tvGenreAdapter);
        tvGenreAdapter.notifyDataSetChanged();
        Toast.makeText(getApplicationContext(), "TVs Fetched", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayGenresByMovies(List<Genre> genreMoviesList) {
        moviesGenreAdapter = new MoviesGenreAdapter(genreMoviesList, this);
        rvGenreMovies.setAdapter(moviesGenreAdapter);
        moviesGenreAdapter.notifyDataSetChanged();
        Toast.makeText(getApplicationContext(), "Movies Fetched", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showError(String errorMessage) {
        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    private void setupRecyclerView() {
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        layoutManager.setJustifyContent(JustifyContent.SPACE_AROUND);
        rvGenreMovies.setLayoutManager(layoutManager);

        FlexboxLayoutManager tvLayoutManager = new FlexboxLayoutManager(this);
        tvLayoutManager.setJustifyContent(JustifyContent.SPACE_AROUND);
        rvGenreTVShows.setLayoutManager(tvLayoutManager);
    }


    @Override
    public void onTvListItemSelected(Genre genre) {
        selectedTvGenres.add(genre);
        Log.d(TAG, "onTvListItemSelected: Selected Genre Size " + selectedTvGenres.size());
    }

    @Override
    public void onTvListItemUnSelected(Genre genre) {
        selectedTvGenres.remove(genre);
        Log.d(TAG, "onTvListItemUnSelected: Selected Genre Size " + selectedTvGenres.size());
    }

    @Override
    public void onMovieListItemSelect(Genre genre) {
        selectedMoviesGenres.add(genre);
        Log.d(TAG, "onMovieListItemUnSelected: Selected Genre Size " + selectedMoviesGenres.size());
    }

    @Override
    public void onMovieListItemUnselect(Genre genre) {
        selectedMoviesGenres.remove(genre);
        Log.d(TAG, "onMovieListItemUnSelected: Selected Genre Size " + selectedMoviesGenres.size());
    }
}
