package com.hussainmukadam.watchit.detailpage.view;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.hussainmukadam.watchit.BuildConfig;
import com.hussainmukadam.watchit.R;
import com.hussainmukadam.watchit.detailpage.DetailsMVPContract;
import com.hussainmukadam.watchit.detailpage.adapter.GenreAdapter;
import com.hussainmukadam.watchit.detailpage.model.MovieDetails;
import com.hussainmukadam.watchit.detailpage.model.TvSeriesDetails;
import com.hussainmukadam.watchit.detailpage.presenter.DetailsPresenter;
import com.hussainmukadam.watchit.BaseActivity;
import com.hussainmukadam.watchit.mainpage.model.Movie;
import com.hussainmukadam.watchit.mainpage.model.TvSeries;
import com.hussainmukadam.watchit.util.Util;
import com.hussainmukadam.watchit.watchlaterpage.view.WatchLaterActivity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

/**
 * Created by hussain on 23/10/17.
 */

public class DetailsFragment extends Fragment implements DetailsMVPContract.DetailsView {
    private static final String TAG = "DetailsFragment";

    @BindView(R.id.iv_poster)
    ImageView ivDetailsPoster;
    @BindView(R.id.iv_backdrop)
    ImageView ivBackdropPoster;
    @BindView(R.id.tv_release_date)
    TextView tvReleaseDate;
    @BindView(R.id.tv_overview_placeholder)
    TextView tvOverviewPlaceholder;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindViews({R.id.tv_vote_average_details, R.id.tv_overview_details, R.id.tv_tagline, R.id.tv_runtime})
    List<TextView> textViewsList;
    @BindView(R.id.rv_genre)
    RecyclerView rvGenreList;

    private DetailsMVPContract.DetailsBasePresenter detailsBasePresenter;
    DetailsPresenter detailsPresenter;
    Movie movie;
    TvSeries tvSeries;
    Target backdropTarget;
    GenreAdapter genreAdapter;
    WatchLaterActivity watchLaterActivity;
    BaseActivity baseActivity;
    Bundle data;
    boolean isMovie = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, view);

        if(getActivity() instanceof BaseActivity) {
            baseActivity = (BaseActivity) getActivity();
            data = baseActivity.getData();
        } else {
            watchLaterActivity = (WatchLaterActivity) getActivity();
            data = watchLaterActivity.getData();
        }

        detailsPresenter = new DetailsPresenter(this);
        setupRecyclerView();

        Object dataObject = data.getParcelable("ITEM");
        if (dataObject instanceof Movie) {
            isMovie = true;
            this.movie = (Movie) dataObject;
        } else {
            this.tvSeries = (TvSeries) dataObject;
        }

        backdropTarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Log.d(TAG, "onBitmapLoaded: ");
                ivBackdropPoster.setImageBitmap(bitmap);
                createPaletteAsync(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Log.d(TAG, "onBitmapFailed: ");
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                Log.d(TAG, "onPrepareLoad: ");
            }
        };

        initTransitionViews();

        if (Util.isConnected(getContext())) {
            if (isMovie) {
                detailsPresenter.fetchMovieDetails(movie.getMovieId());
            } else {
                detailsPresenter.fetchTvSeriesDetails(tvSeries.getTvId());
            }
        } else {
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void initTransitionViews() {

        if (isMovie) {
            collapsingToolbarLayout.setTitle(movie.getMovieTitle());

            Picasso.with(getContext())
                    .load(BuildConfig.imageBaseUrl + movie.getPosterPath())
                    .error(getActivity().getResources().getDrawable(R.drawable.ic_broken_image_black_24dp))
                    .into(ivDetailsPoster);

            Picasso.with(getContext())
                    .load(BuildConfig.imageBaseUrl + movie.getBackdropPath())
                    .error(getActivity().getResources().getDrawable(R.drawable.ic_broken_image_black_24dp))
                    .into(backdropTarget);

            tvReleaseDate.setText(movie.getReleaseDate());
            textViewsList.get(0).setText(String.valueOf(movie.getMovieVoteAverage()));
            if (!TextUtils.isEmpty(movie.getMovieOverview())) {
                textViewsList.get(1).setText(movie.getMovieOverview());
            } else {
                tvOverviewPlaceholder.setText("No Overview");
            }

        } else {
            collapsingToolbarLayout.setTitle(tvSeries.getTvTitle());

            Picasso.with(getContext())
                    .load(BuildConfig.imageBaseUrl + tvSeries.getTvPosterPath())
                    .error(getActivity().getResources().getDrawable(R.drawable.ic_broken_image_black_24dp))
                    .into(ivDetailsPoster);

            Picasso.with(getContext())
                    .load(BuildConfig.imageBaseUrl + tvSeries.getTvBackdropPath())
                    .error(getActivity().getResources().getDrawable(R.drawable.ic_broken_image_black_24dp))
                    .into(backdropTarget);

            tvReleaseDate.setText(tvSeries.getTvReleaseDate());
            textViewsList.get(0).setText(String.valueOf(tvSeries.getTvVoteAverage()));
            if(!TextUtils.isEmpty(tvSeries.getTvOverview())) {
                textViewsList.get(1).setText(tvSeries.getTvOverview());
            } else {
                tvOverviewPlaceholder.setText("No Overview");
            }
        }
    }

    @Override
    public void setPresenter(DetailsMVPContract.DetailsBasePresenter presenter) {
        this.detailsBasePresenter = presenter;
    }

    @Override
    public void showProgress(boolean isLoading) {
        if (isLoading) {
            Log.d(TAG, "showProgress: Loading Started");
        } else {
            Log.d(TAG, "showProgress: Loading Stopped");
        }
    }

    @Override
    public void showError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayMovieDetails(MovieDetails movieDetails) {
        loadFetchedViews(movieDetails, null);
    }

    @Override
    public void displayTvDetails(TvSeriesDetails tvSeriesDetails) {
        loadFetchedViews(null, tvSeriesDetails);
    }


    private void loadFetchedViews(MovieDetails movieDetails, TvSeriesDetails tvSeriesDetails) {
        if (isMovie && movieDetails != null) {

            if (!TextUtils.isEmpty(movieDetails.getTagline())) {
                textViewsList.get(2).setText(movieDetails.getTagline());
            } else {
                textViewsList.get(2).setText("No Tagline");
            }

            if (!TextUtils.isEmpty(String.valueOf(movieDetails.getRuntime()))) {
                textViewsList.get(3).setText(String.valueOf(movieDetails.getRuntime() + " mins"));
            } else {
                textViewsList.get(3).setText("-");
            }

            genreAdapter = new GenreAdapter(movieDetails.getGenres());
            rvGenreList.setAdapter(genreAdapter);
            genreAdapter.notifyDataSetChanged();
        } else if (tvSeriesDetails != null) {

            if (tvSeriesDetails.getEpisodeRunTime().size() != 0) {
                textViewsList.get(3).setText(String.valueOf(Double.valueOf((Double) tvSeriesDetails.getEpisodeRunTime().get(0))) + " mins");
            } else {
                textViewsList.get(3).setText("-");
            }

            if (!TextUtils.isEmpty(tvSeriesDetails.getStatus())) {
                textViewsList.get(2).setText(tvSeriesDetails.getStatus());
            } else {
                textViewsList.get(2).setText("No Status");
            }


            genreAdapter = new GenreAdapter(tvSeriesDetails.getGenres());
            rvGenreList.setAdapter(genreAdapter);
            genreAdapter.notifyDataSetChanged();
        }
    }

    // Generate palette asynchronously and use it on a different
    // thread using onGenerated()
    public void createPaletteAsync(Bitmap bitmap) {
        Log.d(TAG, "createPaletteAsync: ");
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public void onGenerated(Palette p) {
                // Use generated instance
                Palette.Swatch darkVibrantSwatch = p.getDarkVibrantSwatch();
                Palette.Swatch vibrantSwatch = p.getVibrantSwatch();

                if (darkVibrantSwatch != null && vibrantSwatch != null) {
                    Window window = getActivity().getWindow();

                    if (Build.VERSION.SDK_INT >= 21) {
                        window.setStatusBarColor(darkVibrantSwatch.getRgb());
                        collapsingToolbarLayout.setContentScrimColor(vibrantSwatch.getRgb());
                    }
                }
            }
        });
    }

    private void setupRecyclerView() {
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        rvGenreList.setLayoutManager(layoutManager);
    }
}
