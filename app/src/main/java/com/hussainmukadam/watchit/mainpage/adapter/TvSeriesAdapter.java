package com.hussainmukadam.watchit.mainpage.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.text.TextUtilsCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hussainmukadam.watchit.BuildConfig;
import com.hussainmukadam.watchit.R;
import com.hussainmukadam.watchit.mainpage.model.TvSeries;
import com.hussainmukadam.watchit.util.Util;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by hussain on 8/19/17.
 */

public class TvSeriesAdapter extends ArrayAdapter<TvSeries> {
    private static final String TAG = "TvSeriesAdapter";
    ViewHolder viewHolder;

    public TvSeriesAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<TvSeries> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.main_item, parent, false);

            viewHolder = new TvSeriesAdapter.ViewHolder();


            viewHolder.tvTvSeriesTitle = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.ivPosterImage = (ImageView) convertView.findViewById(R.id.iv_poster);
            viewHolder.tvTvSeriesRelease = (TextView) convertView.findViewById(R.id.tv_release);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (TvSeriesAdapter.ViewHolder) convertView.getTag();
        }

        TvSeries item = getItem(position);
        if (item != null) {
            // My layout has only one TextView
            // do whatever you want with your string and long
            Util.debugLog(TAG, "getView: " + item.getTvBackdropPath());
            viewHolder.tvTvSeriesTitle.setText(item.getTvTitle());
            if (!TextUtils.isEmpty(item.getTvReleaseDate())) {
                viewHolder.tvTvSeriesRelease.setText(item.getTvReleaseDate().substring(0, 4));
            }
            Picasso.with(getContext())
                    .load(BuildConfig.imageBaseUrl + item.getTvPosterPath())
                    .error(R.drawable.ic_broken_image_black_24dp)
                    .into(viewHolder.ivPosterImage);

        }

        return convertView;
    }

    static class ViewHolder {
        private TextView tvTvSeriesTitle;
        private TextView tvTvSeriesRelease;
        private ImageView ivPosterImage;
    }
}
