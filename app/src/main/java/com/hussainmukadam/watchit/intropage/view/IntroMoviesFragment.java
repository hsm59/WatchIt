package com.hussainmukadam.watchit.intropage.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.hussainmukadam.watchit.R;
import com.hussainmukadam.watchit.intropage.IntroMVPContract;
import com.hussainmukadam.watchit.intropage.adapter.MoviesGenreAdapter;
import com.hussainmukadam.watchit.intropage.model.Genre;
import com.hussainmukadam.watchit.intropage.presenter.IntroMoviePresenter;
import com.hussainmukadam.watchit.util.CustomSharedPreference;
import com.hussainmukadam.watchit.util.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hussain on 18/10/17.
 */

public class IntroMoviesFragment extends Fragment implements IntroMVPContract.IntroMoviesView, MoviesGenreAdapter.MoviesListItemClickListener {
    private static final String TAG = "IntroMoviesFragment";
    private String title;
    private int page;
    private IntroMoviePresenter presenter;
    private IntroMVPContract.IntroMoviesPresenter introMoviesPresenter;
    private MoviesGenreAdapter moviesGenreAdapter;
    List<Genre> selectedMoviesGenres = new ArrayList<>();
    CustomSharedPreference customSharedPreference;

    @BindView(R.id.intro_title)
    TextView moviesTitle;
    @BindView(R.id.intro_movie_progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.rv_genre_movies)
    RecyclerView rvGenreMovies;

    public static IntroMoviesFragment newInstance(String title, int page){
        IntroMoviesFragment introMoviesFragment = new IntroMoviesFragment();
        Bundle args = new Bundle();
        args.putString("TITLE", title);
        args.putInt("PAGE", page);
        introMoviesFragment.setArguments(args);
        return introMoviesFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!getArguments().containsKey("TITLE") && !getArguments().containsKey("PAGE")) {
            Toast.makeText(getContext(), "No Title or Page found", Toast.LENGTH_SHORT).show();
        } else {
            title = getArguments().getString("TITLE");
            page = getArguments().getInt("PAGE");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_intro_movies, container, false);
        ButterKnife.bind(this, view);

        view.setTag(page);

        moviesTitle.setText(title);
        presenter = new IntroMoviePresenter(this);
        customSharedPreference = new CustomSharedPreference(getContext());

        if(Util.isConnected(getContext())) {
            setupRecyclerView();
            presenter.fetchGenresByMovies();
        } else {
            Toast.makeText(getContext(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    @Override
    public void displayGenresByMovies(List<Genre> genreMovieList) {
        moviesGenreAdapter = new MoviesGenreAdapter(genreMovieList, this);
        rvGenreMovies.setAdapter(moviesGenreAdapter);
        moviesGenreAdapter.notifyDataSetChanged();
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
        Toast.makeText(getContext(), "Some Error Occurred!", Toast.LENGTH_SHORT).show();
    }

    private void setupRecyclerView(){
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
        layoutManager.setJustifyContent(JustifyContent.SPACE_AROUND);
        rvGenreMovies.setLayoutManager(layoutManager);
    }

    @Override
    public void onMovieListItemSelect(Genre genre) {
        selectedMoviesGenres.add(genre);
        customSharedPreference.setMoviesGenrePreference(selectedMoviesGenres);
    }

    @Override
    public void onMovieListItemUnselect(Genre genre) {
        selectedMoviesGenres.remove(genre);
        customSharedPreference.setMoviesGenrePreference(selectedMoviesGenres);
    }

    @Override
    public void setPresenter(Object presenter) {
        this.introMoviesPresenter = (IntroMVPContract.IntroMoviesPresenter) presenter;
    }
}
