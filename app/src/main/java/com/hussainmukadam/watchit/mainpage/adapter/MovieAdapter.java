package com.hussainmukadam.watchit.mainpage.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hussainmukadam.watchit.BuildConfig;
import com.hussainmukadam.watchit.R;
import com.hussainmukadam.watchit.mainpage.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by hussain on 7/23/17.
 */

public class MovieAdapter extends ArrayAdapter<Movie> {
    private static final String TAG = "MovieAdapter";
    ViewHolder viewHolder;

    public MovieAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Movie> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.main_item, parent, false);

            viewHolder = new ViewHolder();


            viewHolder.tvMovieTitle = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.ivPosterImage = (ImageView) convertView.findViewById(R.id.iv_poster);
            viewHolder.tvMovieRelease = (TextView) convertView.findViewById(R.id.tv_release);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Movie item = getItem(position);
        if (item != null) {
            // My layout has only one TextView
            // do whatever you want with your string and long
            Log.d(TAG, "getView: " + item.getBackdropPath());
            viewHolder.tvMovieTitle.setText(item.getMovieTitle());
            viewHolder.tvMovieRelease.setText(item.getReleaseDate());
            Picasso.with(getContext()).load(BuildConfig.imageBaseUrl + item.getPosterPath())
                    .into(viewHolder.ivPosterImage);

        }

        return convertView;
    }

    static class ViewHolder {
        private TextView tvMovieTitle;
        private TextView tvMovieRelease;
        private ImageView ivPosterImage;
    }
}
