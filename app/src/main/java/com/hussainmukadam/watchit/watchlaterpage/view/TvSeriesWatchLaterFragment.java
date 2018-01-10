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
import com.hussainmukadam.watchit.mainpage.model.TvSeries;
import com.hussainmukadam.watchit.mainpage.view.MainFragment;
import com.hussainmukadam.watchit.watchlaterpage.adapter.MoviesWatchLaterAdapter;
import com.hussainmukadam.watchit.watchlaterpage.adapter.TvSeriesWatchLaterAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Created by hussain on 26/11/17.
 */

public class TvSeriesWatchLaterFragment extends Fragment implements TvSeriesWatchLaterAdapter.OnClickTvSeriesItemListener{
    @BindView(R.id.rv_tvseries_watch_later)
    RecyclerView rvTvSeriesWatchLater;

    Realm realm;

    public static TvSeriesWatchLaterFragment newInstance(){
        return new TvSeriesWatchLaterFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tvseries_watch_later, container, false);
        ButterKnife.bind(this, view);

        realm = Realm.getDefaultInstance();

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rvTvSeriesWatchLater.setLayoutManager(layoutManager);

        rvTvSeriesWatchLater.setAdapter(new TvSeriesWatchLaterAdapter(realm.where(TvSeries.class)
                .equalTo("isWatchLater", true).findAllAsync(), true, this));

        return view;
    }

    @Override
    public void itemOnClick(TvSeries tvSeries) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("ITEM", tvSeries);
        WatchLaterActivity watchLaterActivity = (WatchLaterActivity) getActivity();
        watchLaterActivity.saveData(bundle);

        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container_watch_later, new DetailsFragment());
        ft.hide(TvSeriesWatchLaterFragment.this);
        ft.addToBackStack(null);
        ft.commit();
    }
}
