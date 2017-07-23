package com.hussainmukadam.watchit.mainpage.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.hussainmukadam.watchit.R;
import com.hussainmukadam.watchit.mainpage.MainMVPContract;
import com.hussainmukadam.watchit.mainpage.adapter.MovieAdapter;
import com.hussainmukadam.watchit.mainpage.model.Movie;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hussain on 7/23/17.
 */

public class MainFragment extends Fragment implements MainMVPContract.View{
    private static final String TAG = "MainFragment";
    @BindView(R.id.swiping_layout)
    SwipeFlingAdapterView swipeFlingAdapterView;

    private ArrayList<String> al;
    private ArrayAdapter<String> arrayAdapter;
    private List<Movie> movieList = new ArrayList<>();
    private MovieAdapter movieAdapter;
    private int i;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_movies, container, false);
        ButterKnife.bind(this, view);

        al = new ArrayList<>();
        al.add("php");
        al.add("c");
        al.add("python");
        al.add("java");
        al.add("html");
        al.add("c++");
        al.add("css");
        al.add("javascript");

//        arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.movie_item, R.id.tv_movie_item, al );
        Movie movie = new Movie();
        movie.setMovieId(12);
        movie.setMovieTitle("First Title");
        movieList.add(movie);

        movieAdapter = new MovieAdapter(getContext(), R.layout.movie_item, R.id.tv_movie_item, movieList);

        swipeFlingAdapterView.setAdapter(movieAdapter);
        swipeFlingAdapterView.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                al.remove(0);
                movieAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                makeToast(getContext(), "Left!");
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                makeToast(getContext(), "Right!");
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
                al.add("XML ".concat(String.valueOf(i)));
                movieAdapter.notifyDataSetChanged();
                Log.d("LIST", "notified");
                i++;
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
                makeToast(getContext(), "Clicked!");
            }
        });

        return view;
    }

    static void makeToast(Context ctx, String s){
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(MainMVPContract.Presenter presenter) {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void displayMoviesCards(List<Movie> movieList) {

    }

    @Override
    public void showError(String errorMessage) {

    }
}
