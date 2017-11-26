package com.hussainmukadam.watchit.watchlaterpage.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.hussainmukadam.watchit.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hussain on 27/10/17.
 */

public class WatchLaterFragment extends Fragment {
    @BindView(R.id.watchlater_viewpager)
    ViewPager watchLaterViewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_watch_later, container, false);
        ButterKnife.bind(this, view);

        watchLaterViewPager.setAdapter(new WatchLaterAdapter(getChildFragmentManager()));

        return view;
    }

    static class WatchLaterAdapter extends FragmentPagerAdapter {

        public WatchLaterAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return MoviesWatchLaterFragment.newInstance();
                case 1:
                    return TvSeriesWatchLaterFragment.newInstance();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch(position){
                case 0:
                    return "MOVIES";
                case 1:
                    return "TV SERIES";
                default:
                    return null;
            }
        }
    }
}
