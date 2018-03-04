package com.hussainmukadam.watchit.watchlaterpage.view;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hussainmukadam.watchit.BaseActivity;
import com.hussainmukadam.watchit.R;
import com.hussainmukadam.watchit.detailpage.view.DetailsFragment;
import com.hussainmukadam.watchit.detailpage.view.DetailsTransition;
import com.hussainmukadam.watchit.mainpage.model.Movie;
import com.hussainmukadam.watchit.mainpage.view.MainFragment;
import com.hussainmukadam.watchit.watchlaterpage.adapter.MoviesWatchLaterAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Created by hussain on 26/11/17.
 */

public class MoviesWatchLaterFragment extends Fragment implements MoviesWatchLaterAdapter.OnClickMovieItemListener {
    private static final String TAG = "MoviesWatchLaterFragmen";
    @BindView(R.id.rv_movies_watch_later)
    RecyclerView rvMoviesWatchLater;

    Toolbar toolbarMain;
    Realm realm;

    public static MoviesWatchLaterFragment newInstance() {
        return new MoviesWatchLaterFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies_watch_later, container, false);
        ButterKnife.bind(this, view);

        realm = Realm.getDefaultInstance();

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        if (Build.VERSION.SDK_INT >= 21) {
            getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        rvMoviesWatchLater.setLayoutManager(layoutManager);

        rvMoviesWatchLater.setAdapter(new MoviesWatchLaterAdapter(realm.where(Movie.class)
                .equalTo("isWatchLater", true).findAllAsync(), true, this));

        return view;
    }

    @Override
    public void itemOnClick(Movie movie) {

        Bundle bundle = new Bundle();
        bundle.putParcelable("ITEM", movie);
        WatchLaterActivity watchLaterActivity = (WatchLaterActivity) getActivity();
        watchLaterActivity.saveData(bundle);

        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container_watch_later, new DetailsFragment());
        ft.hide(MoviesWatchLaterFragment.this);
        ft.addToBackStack(null);
        ft.commit();

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        toolbarMain = getActivity().findViewById(R.id.toolbar_main);
        toolbarMain.setVisibility(View.VISIBLE);
    }
}
