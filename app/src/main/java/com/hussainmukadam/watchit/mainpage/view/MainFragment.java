package com.hussainmukadam.watchit.mainpage.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.hussainmukadam.watchit.R;
import com.hussainmukadam.watchit.intropage.model.Genre;
import com.hussainmukadam.watchit.mainpage.MainMVPContract;
import com.hussainmukadam.watchit.mainpage.adapter.MovieAdapter;
import com.hussainmukadam.watchit.mainpage.adapter.TvSeriesAdapter;
import com.hussainmukadam.watchit.mainpage.model.Movie;
import com.hussainmukadam.watchit.mainpage.model.TvSeries;
import com.hussainmukadam.watchit.mainpage.presenter.MainPresenter;
import com.hussainmukadam.watchit.util.CustomSharedPreference;
import com.hussainmukadam.watchit.util.Util;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hussain on 7/23/17.
 */

public class MainFragment extends Fragment implements MainMVPContract.View, View.OnClickListener {
    private static final String TAG = "MainFragment";
    private static final int PAGE_START = 1;
    @BindView(R.id.swiping_layout)
    SwipeFlingAdapterView swipeFlingAdapterView;
    @BindView(R.id.animated_progress_bar)
    LottieAnimationView progressBar;
    @BindView(R.id.ib_favorite)
    ImageButton imageButtonFavorite;
    @BindView(R.id.ib_cancel)
    ImageButton imageButtonCancel;

    private MovieAdapter movieAdapter;
    private TvSeriesAdapter tvSeriesAdapter;
    private MainMVPContract.Presenter presenter;
    private MainPresenter mainPresenter;
    private CustomSharedPreference prefs;
    private String genresList;
    private Boolean isMovies;
    private int TOTAL_PAGES;
    private int currentPage = PAGE_START;
    //private Boolean isFirstFetch;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        isMovies = getArguments().getBoolean("IS_MOVIES");
        Log.d(TAG, "onCreateView: Boolean " + isMovies);

        prefs = new CustomSharedPreference(getContext());
        mainPresenter = new MainPresenter(this);

        imageButtonFavorite.setOnClickListener(this);
        imageButtonCancel.setOnClickListener(this);

        imageButtonFavorite.setEnabled(false);
        imageButtonCancel.setEnabled(false);

        genresList = getGenres();

        if (Util.isConnected(getContext())) {
            if (isMovies) {
                mainPresenter.fetchFirstPageMoviesByGenres(genresList, currentPage);
            } else {
                mainPresenter.fetchFirstPageTvSeriesByGenres(genresList, currentPage);
            }
        } else {
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }


        return view;
    }

    private String getGenres() {
       // isFirstFetch = true;
        if (isMovies) {
            List<Genre> tempList = prefs.getMoviesGenrePreference();
            if (tempList.size() != 0) {

                List<Genre> moviesList = new ArrayList<>();
                Random r = new Random();
                for (int i = 0; i < 3; i++) {
                    moviesList.add(tempList.get(r.nextInt(prefs.getMoviesGenrePreference().size())));
                }

                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < moviesList.size(); i++) {
                    builder.append(moviesList.get(i).getGenreId());

                    if (i < moviesList.size() - 1) {
                        builder.append(",");
                    }

                    Log.d(TAG, "onCreateView: Movie String " + builder.toString());
                }
                return builder.toString();
            } else {
                return null;
            }
        } else {
            List<Genre> tempList = prefs.getTvGenrePreference();
            if (tempList.size() != 0) {

                List<Genre> tvList = new ArrayList<>();
                Random r = new Random();
                for (int i = 0; i < 3; i++) {
                    tvList.add(tempList.get(r.nextInt(prefs.getTvGenrePreference().size())));
                }

                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < tvList.size(); i++) {
                    builder.append(tvList.get(i).getGenreId());

                    if (i < tvList.size() - 1) {
                        builder.append(",");
                    }

                    Log.d(TAG, "onCreateView: TV String " + builder.toString());
                }
                return builder.toString();
            } else {
                return null;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_favorite:
                if (isMovies) {
                    if (!movieAdapter.isEmpty()) {
                        swipeFlingAdapterView.getTopCardListener().selectRight();
                    }
                } else {
                    if (!tvSeriesAdapter.isEmpty()) {
                        swipeFlingAdapterView.getTopCardListener().selectRight();
                    }
                }
                break;
            case R.id.ib_cancel:
                if (isMovies) {
                    if (!movieAdapter.isEmpty()) {
                        swipeFlingAdapterView.getTopCardListener().selectLeft();
                    }
                } else {
                    if (!tvSeriesAdapter.isEmpty()) {
                        swipeFlingAdapterView.getTopCardListener().selectLeft();
                    }
                }
                break;
        }
    }


    private void setupSwipeFlingAdapterViewForMovies(final List<Movie> movieList) {
        Log.d(TAG, "displayMoviesCards: Movies List " + movieList.size());

        movieAdapter = new MovieAdapter(getContext(), R.layout.main_item, movieList);

        imageButtonCancel.setEnabled(true);
        imageButtonFavorite.setEnabled(true);

        swipeFlingAdapterView.setAdapter(movieAdapter);
        movieAdapter.notifyDataSetChanged();

        swipeFlingAdapterView.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                if (movieList.size() != 0) {
                    movieList.remove(0);
                    movieAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                // Log.d(TAG, "onLeftCardExit: dataObject " + dataObject.getClass().getSimpleName());
                Movie movie = (Movie) dataObject;
                mainPresenter.storeMovieData(false, movie);
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Log.d(TAG, "onRightCardExit: dataObject " + dataObject.getClass().getSimpleName());
                Movie movie = (Movie) dataObject;
                mainPresenter.storeMovieData(true, movie);
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
                Log.d(TAG, "onAdapterAboutToEmpty: ItemsInAdapter " + itemsInAdapter + " currentPage " + currentPage + " TOTAL PAGES " + TOTAL_PAGES);


                //MANOJ'S CHANGES
                if (itemsInAdapter == 0) {
                    if (Util.isConnected(getContext())) {
                        currentPage = PAGE_START;
                        mainPresenter.fetchFirstPageMoviesByGenres(getGenres(), currentPage);
                    } else {
                        Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
                //END



                /*if (itemsInAdapter == 0 && currentPage < TOTAL_PAGES && isFirstFetch) {
                    Log.d(TAG, "onAdapterAboutToEmpty: Inside if ItemsInAdapter " + itemsInAdapter + " currentPage " + currentPage + " TOTAL PAGES " + TOTAL_PAGES);

                    if (Util.isConnected(getContext())) {
                        currentPage = getPage(TOTAL_PAGES);
                        isFirstFetch = false;
                        mainPresenter.fetchNextPageMoviesByGenres(genresList, currentPage);
                    } else {
                        Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                } else if (itemsInAdapter == 0) {
                    Log.d(TAG, "onAdapterAboutToEmpty: Inside else");

                    if (Util.isConnected(getContext())) {
                        currentPage = PAGE_START;
                        mainPresenter.fetchFirstPageMoviesByGenres(getGenres(), currentPage);
                    } else {
                        Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }*/
            }


            @Override
            public void onScroll(float scrollProgressPercent) {
//                View view = swipeFlingAdapterView.getSelectedView();
//                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
//                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });


        // Optionally add an OnItemClickListener
        swipeFlingAdapterView.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Log.d(TAG, "onItemClicked: Item clicked");
            }
        });
    }

    private void setupSwipeFlingAdapterViewForTv(final List<TvSeries> tvSeriesList) {
        Log.d(TAG, "displayMoviesCards: Tv Series List " + tvSeriesList.size());

        tvSeriesAdapter = new TvSeriesAdapter(getContext(), R.layout.main_item, tvSeriesList);

        imageButtonCancel.setEnabled(true);
        imageButtonFavorite.setEnabled(true);

        swipeFlingAdapterView.setAdapter(tvSeriesAdapter);
        tvSeriesAdapter.notifyDataSetChanged();

        swipeFlingAdapterView.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                if (tvSeriesList.size() != 0) {
                    tvSeriesList.remove(0);
                    tvSeriesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                Log.d(TAG, "onLeftCardExit: dataObject " + dataObject.getClass().getSimpleName());
                TvSeries tvSeries = (TvSeries) dataObject;
                mainPresenter.storeTvData(false, tvSeries);
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Log.d(TAG, "onRightCardExit: dataObject " + dataObject.getClass().getSimpleName());
                TvSeries tvSeries = (TvSeries) dataObject;
                mainPresenter.storeTvData(true, tvSeries);
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
                Log.d(TAG, "onAdapterAboutToEmpty: ItemsInAdapter " + itemsInAdapter + " currentPage " + currentPage + " TOTAL PAGES " + TOTAL_PAGES);

                //MANOJ'S CHANGES
                if (itemsInAdapter == 0) {
                    if (Util.isConnected(getContext())) {
                        currentPage = PAGE_START;
                        mainPresenter.fetchFirstPageTvSeriesByGenres(getGenres(), currentPage);
                    } else {
                        Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
                //END

                /*if (itemsInAdapter == 0 && currentPage < TOTAL_PAGES && isFirstFetch) {
                    Log.d(TAG, "onAdapterAboutToEmpty: Inside if ItemsInAdapter " + itemsInAdapter + " currentPage " + currentPage + " TOTAL PAGES " + TOTAL_PAGES);

                    if (Util.isConnected(getContext())) {
                        currentPage = getPage(TOTAL_PAGES);
                        isFirstFetch = false;
                        mainPresenter.fetchNextPageTvSeriesByGenres(genresList, currentPage);
                    } else {
                        Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                } else if (itemsInAdapter == 0) {
                    Log.d(TAG, "onAdapterAboutToEmpty: Inside else");

                    if (Util.isConnected(getContext())) {
                        currentPage = PAGE_START;
                        mainPresenter.fetchFirstPageTvSeriesByGenres(getGenres(), currentPage);
                    } else {
                        Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }*/
            }


            @Override
            public void onScroll(float scrollProgressPercent) {
//                View view = swipeFlingAdapterView.getSelectedView();
//                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
//                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });


        // Optionally add an OnItemClickListener
        swipeFlingAdapterView.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Log.d(TAG, "onItemClicked: Item clicked");
            }
        });
    }

    /*private int getPage(int totalPage) {
        Random random = new Random();
        int n = random.nextInt(totalPage) + 1;
        Log.d(TAG, " NextPageNo: " + n);
        return n;
    }
*/
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void setPresenter(MainMVPContract.Presenter presenter) {
        this.presenter = presenter;
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
    public void displayFirstPageMovies(List<Movie> movieList, int totalPages) {
        setupSwipeFlingAdapterViewForMovies(movieList);
        this.TOTAL_PAGES = totalPages;
    }

    @Override
    public void displayNextPageMovies(List<Movie> movieList, int totalPages) {
        setupSwipeFlingAdapterViewForMovies(movieList);
        this.TOTAL_PAGES = totalPages;
    }

    @Override
    public void displayFirstPageTvSeries(List<TvSeries> tvSeriesList, int totalPages) {
        setupSwipeFlingAdapterViewForTv(tvSeriesList);
        this.TOTAL_PAGES = totalPages;
    }

    @Override
    public void displayNextPageTvSeries(List<TvSeries> tvSeriesList, int totalPages) {
        setupSwipeFlingAdapterViewForTv(tvSeriesList);
        this.TOTAL_PAGES = totalPages;
    }

    @Override
    public void showMovieResponseError(String errorMessage) {
        Toast.makeText(getContext().getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
        if (Util.isConnected(getContext())) {
            currentPage = PAGE_START;
            mainPresenter.fetchFirstPageMoviesByGenres(getGenres(), currentPage);
        } else {
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
//        if(Util.isConnected(getContext())) {
//            currentPage = PAGE_START;
//            mainPresenter.fetchFirstPageMoviesByGenres(getGenres(), currentPage);
//        } else {
//            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public void showTvSeriesResponseError(String errorMessage) {
        Toast.makeText(getContext().getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
        if (Util.isConnected(getContext())) {
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
        if(Util.isConnected(getContext())) {
            currentPage = PAGE_START;
            mainPresenter.fetchFirstPageTvSeriesByGenres(getGenres(), currentPage);
        } else {
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }
}
