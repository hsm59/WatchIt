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
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by hussain on 26/11/17.
 */

public class MoviesWatchLaterAdapter extends RealmRecyclerViewAdapter<Movie, MoviesWatchLaterAdapter.MoviesWatchLaterViewHolder> {

    public MoviesWatchLaterAdapter(@Nullable OrderedRealmCollection<Movie> data, boolean autoUpdate) {
        super(data, autoUpdate);
    }

    @Override
    public MoviesWatchLaterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MoviesWatchLaterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.watch_later_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MoviesWatchLaterViewHolder holder, int position) {
        Movie movie = getItem(position);

        Picasso.with(holder.ivWatchLaterPoster.getContext())
                .load(BuildConfig.imageBaseUrl + movie.getPosterPath())
                .error(R.drawable.ic_broken_image_black_24dp)
                .into(holder.ivWatchLaterPoster);

        holder.tvWatchLaterTitle.setText(movie.getMovieTitle());
        holder.tvWatchLaterRelease.setText(movie.getReleaseDate());
    }

    public static class MoviesWatchLaterViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_watchlater_poster)
        ImageView ivWatchLaterPoster;
        @BindView(R.id.tv_watchlater_title)
        TextView tvWatchLaterTitle;
        @BindView(R.id.tv_watchlater_release)
        TextView tvWatchLaterRelease;

        public MoviesWatchLaterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
