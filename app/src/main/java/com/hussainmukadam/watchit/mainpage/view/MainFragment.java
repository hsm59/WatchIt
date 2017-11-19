package com.hussainmukadam.watchit.mainpage.view;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.transition.Fade;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.hussainmukadam.watchit.R;
import com.hussainmukadam.watchit.detailpage.view.DetailsFragment;
import com.hussainmukadam.watchit.detailpage.view.DetailsTransition;
import com.hussainmukadam.watchit.intropage.model.Genre;
import com.hussainmukadam.watchit.mainpage.BaseActivity;
import com.hussainmukadam.watchit.mainpage.MainMVPContract;
import com.hussainmukadam.watchit.mainpage.adapter.MovieAdapter;
import com.hussainmukadam.watchit.mainpage.adapter.TvSeriesAdapter;
import com.hussainmukadam.watchit.mainpage.model.Movie;
import com.hussainmukadam.watchit.mainpage.model.TvSeries;
import com.hussainmukadam.watchit.mainpage.presenter.MainPresenter;
import com.hussainmukadam.watchit.pages.DetailDevFragment;
import com.hussainmukadam.watchit.util.CustomSharedPreference;
import com.hussainmukadam.watchit.util.Util;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hussain on 7/23/17.
 */

public class MainFragment extends Fragment implements MainMVPContract.View {
    private static final String TAG = "MainFragment";
    private static final int PAGE_START = 1;
    @BindView(R.id.swiping_layout)
    SwipeFlingAdapterView swipeFlingAdapterView;
    @BindView(R.id.animated_progress_bar)
    LottieAnimationView progressBar;
    @BindView(R.id.fab_favorite)
    FloatingActionButton imageButtonFavorite;
    @BindView(R.id.fab_cancel)
    FloatingActionButton imageButtonCancel;
    @BindView(R.id.menu_bar)
    ImageView ivMenuBar;
    @BindView(R.id.three_dots)
    ImageView ivMenuPopup;

    private MovieAdapter movieAdapter;
    private TvSeriesAdapter tvSeriesAdapter;
    private MainMVPContract.Presenter presenter;
    private OnMenuBarClicked onMenuBarClicked;
    private MainPresenter mainPresenter;
    private CustomSharedPreference prefs;
    private String genresList;
    private Boolean isMovies;
    private int TOTAL_PAGES;
    private int currentPage = PAGE_START;
    //private Boolean isFirstFetch;

    public interface OnMenuBarClicked {
        void menuBarClicked();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        isMovies = getArguments().getBoolean("IS_MOVIES");
        Log.d(TAG, "onCreateView: Boolean " + isMovies);

        prefs = new CustomSharedPreference(getContext());
        mainPresenter = new MainPresenter(this);

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

    @OnClick(R.id.menu_bar)
    public void menuBarClicked() {
        onMenuBarClicked = (OnMenuBarClicked) getContext();
        onMenuBarClicked.menuBarClicked();
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

    @OnClick(R.id.fab_favorite)
    public void itemFavorited() {
        if (isMovies) {
            if (!movieAdapter.isEmpty()) {
                swipeFlingAdapterView.getTopCardListener().selectRight();
            }
        } else {
            if (!tvSeriesAdapter.isEmpty()) {
                swipeFlingAdapterView.getTopCardListener().selectRight();
            }
        }
    }

    @OnClick(R.id.fab_cancel)
    public void itemCancelled() {
        if (isMovies) {
            if (!movieAdapter.isEmpty()) {
                swipeFlingAdapterView.getTopCardListener().selectLeft();
            }
        } else {
            if (!tvSeriesAdapter.isEmpty()) {
                swipeFlingAdapterView.getTopCardListener().selectLeft();
            }
        }
    }

    @OnClick(R.id.three_dots)
    public void popupMenu() {
        PopupMenu popupMenu = new PopupMenu(getContext(), ivMenuPopup);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (isMovies) {
                    movieAdapter.clear();
                    mainPresenter.fetchFirstPageMoviesByGenres(getGenres(), PAGE_START);
                } else {
                    tvSeriesAdapter.clear();
                    mainPresenter.fetchFirstPageTvSeriesByGenres(getGenres(), PAGE_START);
                }
                return true;
            }
        });

        popupMenu.show();
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

                if (itemsInAdapter == 0) {
                    if (Util.isConnected(getContext())) {
                        currentPage = PAGE_START;
                        mainPresenter.fetchFirstPageMoviesByGenres(getGenres(), currentPage);
                    } else {
                        Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
            }


            @Override
            public void onScroll(float scrollProgressPercent) {
                View view = swipeFlingAdapterView.getSelectedView();
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });


        // Optionally add an OnItemClickListener
        swipeFlingAdapterView.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {

//                final FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.replace(R.id.container, new DetailDevFragment());
//                ft.addToBackStack(null);
//                ft.commit();

                //TODO: Until completed the detail page
                View view = swipeFlingAdapterView.getSelectedView();
                ImageView ivPoster = (ImageView) view.findViewById(R.id.iv_poster);
                TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
                TextView tvReleaseDate = (TextView) view.findViewById(R.id.tv_release);

                ViewCompat.setTransitionName(ivPoster, "posterImage");
                ViewCompat.setTransitionName(tvTitle, "titleName");
                ViewCompat.setTransitionName(tvReleaseDate, "releaseDate");

                Log.d(TAG, "onItemClicked: Item clicked " + ivPoster);
                if (dataObject instanceof Movie) {
                    DetailsFragment detailsFragment = new DetailsFragment();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        detailsFragment.setSharedElementEnterTransition(new DetailsTransition());
                        detailsFragment.setEnterTransition(new android.transition.Fade());
                        setExitTransition(new android.transition.Fade());
                        detailsFragment.setSharedElementReturnTransition(new DetailsTransition());
                    }

                    Log.d(TAG, "onItemClicked: The dataObject is Movie " + ((Movie) dataObject).getBackdropPath());
                    Movie movie = (Movie) dataObject;

                    Bundle bundle = new Bundle();
                    bundle.putParcelable("ITEM", movie);
                    BaseActivity baseActivity = (BaseActivity) getActivity();
                    baseActivity.saveData(bundle);

                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.addSharedElement(ivPoster, "posterImage");
                    ft.addSharedElement(tvTitle, "titleName");
                    ft.addSharedElement(tvReleaseDate, "releaseDate");
                    ft.replace(R.id.container, detailsFragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }
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

                if (itemsInAdapter == 0) {
                    if (Util.isConnected(getContext())) {
                        currentPage = PAGE_START;
                        mainPresenter.fetchFirstPageTvSeriesByGenres(getGenres(), currentPage);
                    } else {
                        Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
            }


            @Override
            public void onScroll(float scrollProgressPercent) {
                View view = swipeFlingAdapterView.getSelectedView();
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });


        // Optionally add an OnItemClickListener
        swipeFlingAdapterView.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Log.d(TAG, "onItemClicked: Item clicked");

//                final FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.replace(R.id.container, new DetailDevFragment());
//                ft.addToBackStack(null);
//                ft.commit();

                //TODO: Until detail fragment is completed
                View view = swipeFlingAdapterView.getSelectedView();
                ImageView ivPoster = (ImageView) view.findViewById(R.id.iv_poster);

                ViewCompat.setTransitionName(ivPoster, "posterImage");

                Log.d(TAG, "onItemClicked: Item clicked " + ivPoster);
                if (dataObject instanceof TvSeries) {
                    DetailsFragment detailsFragment = new DetailsFragment();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        detailsFragment.setSharedElementEnterTransition(new DetailsTransition());
                        detailsFragment.setEnterTransition(new android.transition.Fade());
                        setExitTransition(new android.transition.Fade());
                        detailsFragment.setSharedElementReturnTransition(new DetailsTransition());
                    }

                    Log.d(TAG, "onItemClicked: The dataObject is Movie " + ((TvSeries) dataObject).getTvPosterPath());
                    TvSeries tvSeries = (TvSeries) dataObject;

                    Bundle bundle = new Bundle();
                    bundle.putParcelable("ITEM", tvSeries);
                    BaseActivity baseActivity = (BaseActivity) getActivity();
                    baseActivity.saveData(bundle);

                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.addSharedElement(ivPoster, "posterImage");
                    ft.replace(R.id.container, detailsFragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            }
        });
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
        if (getContext() != null) {
            setupSwipeFlingAdapterViewForMovies(movieList);
            this.TOTAL_PAGES = totalPages;
        }
    }

    @Override
    public void displayNextPageMovies(List<Movie> movieList, int totalPages) {
        if (getContext() != null) {
            setupSwipeFlingAdapterViewForMovies(movieList);
            this.TOTAL_PAGES = totalPages;
        }
    }

    @Override
    public void displayFirstPageTvSeries(List<TvSeries> tvSeriesList, int totalPages) {
        if (getContext() != null) {
            setupSwipeFlingAdapterViewForTv(tvSeriesList);
            this.TOTAL_PAGES = totalPages;
        }
    }

    @Override
    public void displayNextPageTvSeries(List<TvSeries> tvSeriesList, int totalPages) {
        if (getContext() != null) {
            setupSwipeFlingAdapterViewForTv(tvSeriesList);
            this.TOTAL_PAGES = totalPages;
        }
    }

    @Override
    public void showMovieResponseError(String errorMessage) {
        if (Util.isConnected(getContext())) {
            currentPage = PAGE_START;
            mainPresenter.fetchFirstPageMoviesByGenres(getGenres(), currentPage);
        } else {
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showTvSeriesResponseError(String errorMessage) {
        if (Util.isConnected(getContext())) {
            currentPage = PAGE_START;
            mainPresenter.fetchFirstPageTvSeriesByGenres(getGenres(), currentPage);
        } else {
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
    }
}
