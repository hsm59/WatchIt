package com.hussainmukadam.watchit.detailpage.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hussainmukadam.watchit.BuildConfig;
import com.hussainmukadam.watchit.R;
import com.hussainmukadam.watchit.mainpage.BaseActivity;
import com.hussainmukadam.watchit.mainpage.model.Movie;
import com.hussainmukadam.watchit.mainpage.model.TvSeries;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hussain on 23/10/17.
 */

public class DetailsFragment extends Fragment {
    private static final String TAG = "DetailsFragment";
    private Object dataObject;

    @BindView(R.id.iv_poster)
    ImageView ivDetailsPoster;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, view);

        BaseActivity baseActivity = (BaseActivity) getActivity();
        Bundle data = baseActivity.getData();



        dataObject = data.getParcelable("ITEM");
        initViews();

        return view;
    }

    private void initViews() {

        if(dataObject instanceof  Movie) {
            Movie movie = (Movie) dataObject;

            Picasso.with(getContext())
                    .load(BuildConfig.imageBaseUrl + movie.getPosterPath())
                    .error(R.drawable.ic_broken_image_black_24dp)
                    .into(ivDetailsPoster);
        } else {
            TvSeries tvSeries = (TvSeries) dataObject;

            Picasso.with(getContext())
                    .load(BuildConfig.imageBaseUrl + tvSeries.getTvPosterPath())
                    .error(R.drawable.ic_broken_image_black_24dp)
                    .into(ivDetailsPoster);
        }
    }
}
