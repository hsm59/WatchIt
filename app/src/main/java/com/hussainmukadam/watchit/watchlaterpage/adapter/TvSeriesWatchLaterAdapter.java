package com.hussainmukadam.watchit.watchlaterpage.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hussainmukadam.watchit.BuildConfig;
import com.hussainmukadam.watchit.R;
import com.hussainmukadam.watchit.mainpage.model.Movie;
import com.hussainmukadam.watchit.mainpage.model.TvSeries;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by hussain on 26/11/17.
 */

public class TvSeriesWatchLaterAdapter extends RealmRecyclerViewAdapter<TvSeries, TvSeriesWatchLaterAdapter.TvSeriesWatchLaterViewHolder> {

    public TvSeriesWatchLaterAdapter(@Nullable OrderedRealmCollection<TvSeries> data, boolean autoUpdate) {
        super(data, autoUpdate);
    }

    @Override
    public TvSeriesWatchLaterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TvSeriesWatchLaterAdapter.TvSeriesWatchLaterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.watch_later_item, parent, false));
    }

    @Override
    public void onBindViewHolder(TvSeriesWatchLaterViewHolder holder, int position) {
        TvSeries tvSeries = getItem(position);

        Picasso.with(holder.ivWatchLaterPoster.getContext())
                .load(BuildConfig.imageBaseUrl + tvSeries.getTvPosterPath())
                .error(R.drawable.ic_broken_image_black_24dp)
                .into(holder.ivWatchLaterPoster);

        holder.tvWatchLaterTitle.setText(tvSeries.getTvTitle());
        holder.tvWatchLaterRelease.setText(tvSeries.getTvReleaseDate());
    }

    public static class TvSeriesWatchLaterViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_watchlater_poster)
        ImageView ivWatchLaterPoster;
        @BindView(R.id.tv_watchlater_title)
        TextView tvWatchLaterTitle;
        @BindView(R.id.tv_watchlater_release)
        TextView tvWatchLaterRelease;

        public TvSeriesWatchLaterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
