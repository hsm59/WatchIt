package com.hussainmukadam.watchit.watchlaterpage.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hussainmukadam.watchit.R;
import com.hussainmukadam.watchit.mainpage.model.Movie;
import com.hussainmukadam.watchit.watchlaterpage.adapter.MoviesWatchLaterAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Created by hussain on 26/11/17.
 */

public class MoviesWatchLaterFragment extends Fragment {
    @BindView(R.id.rv_movies_watch_later)
    RecyclerView rvMoviesWatchLater;

    Realm realm;

    public static MoviesWatchLaterFragment newInstance(){
        MoviesWatchLaterFragment moviesWatchLaterFragment = new MoviesWatchLaterFragment();
        return moviesWatchLaterFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies_watch_later, container, false);
        ButterKnife.bind(this, view);

        realm = Realm.getDefaultInstance();

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rvMoviesWatchLater.setLayoutManager(layoutManager);

        rvMoviesWatchLater.setAdapter(new MoviesWatchLaterAdapter(realm.where(Movie.class).equalTo("isWatchLater", true).findAllAsync(), true));

        return view;
    }
}
