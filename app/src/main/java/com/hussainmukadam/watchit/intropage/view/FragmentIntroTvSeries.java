package com.hussainmukadam.watchit.intropage.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.hussainmukadam.watchit.R;
import com.hussainmukadam.watchit.intropage.IntroMVPContract;
import com.hussainmukadam.watchit.intropage.adapter.TvGenreAdapter;
import com.hussainmukadam.watchit.intropage.adapter.TvGenreAdapter.TvListItemClickListener;
import com.hussainmukadam.watchit.intropage.model.Genre;
import com.hussainmukadam.watchit.intropage.presenter.IntroTvPresenter;
import com.hussainmukadam.watchit.util.CustomSharedPreference;
import com.hussainmukadam.watchit.util.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hussain on 18/10/17.
 */

public class FragmentIntroTvSeries extends Fragment implements IntroMVPContract.IntroTvView, TvListItemClickListener{
    private static final String TAG = "FragmentIntroTvSeries";
    private String title;
    private int page;
    private IntroMVPContract.IntroTvPresenter introTvPresenter;
    private IntroTvPresenter presenter;
    private TvGenreAdapter tvGenreAdapter;
    List<Genre> selectedTvGenres = new ArrayList<>();
    CustomSharedPreference customSharedPreference;

    @BindView(R.id.intro_title)
    TextView tvTitle;
    @BindView(R.id.intro_tv_progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.rv_genre_tv)
    RecyclerView rvGenreTv;


    public static FragmentIntroTvSeries newInstance(String title, int page){
        FragmentIntroTvSeries fragmentIntroTvSeries = new FragmentIntroTvSeries();
        Bundle args = new Bundle();
        args.putString("TITLE", title);
        args.putInt("PAGE", page);
        fragmentIntroTvSeries.setArguments(args);
        return fragmentIntroTvSeries;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!getArguments().containsKey("TITLE") && !getArguments().containsKey("PAGE")) {
            Toast.makeText(getContext(), "No Title or Page No. found", Toast.LENGTH_SHORT).show();
        } else {
            title = getArguments().getString("TITLE");
            page = getArguments().getInt("PAGE");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_intro_tvseries, container, false);
        ButterKnife.bind(this, view);

        view.setTag(page);

        presenter = new IntroTvPresenter(this);
        customSharedPreference = new CustomSharedPreference(getContext());

        if(Util.isConnected(getContext())) {
            setupRecyclerView();
            presenter.fetchGenresByTV();
        } else {
            Toast.makeText(getContext(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }

        tvTitle.setText(title);
        return view;
    }

    @Override
    public void setPresenter(IntroMVPContract.IntroTvPresenter presenter) {
        this.introTvPresenter = presenter;
    }

    @Override
    public void displayGenresByTV(List<Genre> genreTVList) {
        tvGenreAdapter = new TvGenreAdapter(genreTVList, this);
        rvGenreTv.setAdapter(tvGenreAdapter);
        tvGenreAdapter.notifyDataSetChanged();
        Toast.makeText(getContext(), "TVs Fetched", Toast.LENGTH_SHORT).show();
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
    public void showError(String errorMessage) {
        Toast.makeText(getContext(), "Some Error Occurred!", Toast.LENGTH_SHORT).show();
    }

    private void setupRecyclerView() {
        FlexboxLayoutManager tvLayoutManager = new FlexboxLayoutManager(getContext());
        tvLayoutManager.setJustifyContent(JustifyContent.SPACE_AROUND);
        rvGenreTv.setLayoutManager(tvLayoutManager);
    }

    @Override
    public void onTvListItemSelected(Genre genre) {
        selectedTvGenres.add(genre);
        customSharedPreference.setTvGenrePreference(selectedTvGenres);
        Log.d(TAG, "onTvListItemSelected: Selected Genre Size " + selectedTvGenres.size());
    }

    @Override
    public void onTvListItemUnSelected(Genre genre) {
        selectedTvGenres.remove(genre);
        customSharedPreference.setTvGenrePreference(selectedTvGenres);
        Log.d(TAG, "onTvListItemUnSelected: Selected Genre Size " + selectedTvGenres.size());
    }
}
