package com.hussainmukadam.watchit.watchlaterpage.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.hussainmukadam.watchit.BaseActivity;
import com.hussainmukadam.watchit.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hussain on 27/10/17.
 */

public class WatchLaterActivity extends AppCompatActivity {
    @BindView(R.id.watchlater_viewpager)
    ViewPager watchLaterViewPager;
    @BindView(R.id.sliding_tabs)
    TabLayout slidingTabs;
    @BindView(R.id.toolbar_main)
    Toolbar toolbarMain;

    Bundle savedData = new Bundle();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_later);
        ButterKnife.bind(this);

        watchLaterViewPager.setAdapter(new WatchLaterAdapter(getSupportFragmentManager()));
        slidingTabs.setupWithViewPager(watchLaterViewPager);

        toolbarMain.setTitle("Watch Later");
        toolbarMain.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        toolbarMain.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    public static class WatchLaterAdapter extends FragmentPagerAdapter {

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
            switch (position) {
                case 0:
                    return "MOVIES";
                case 1:
                    return "TV SERIES";
                default:
                    return null;
            }
        }
    }

    public void saveData(Bundle bundle) {
        savedData = bundle;
    }

    public Bundle getData() {
        return savedData;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }
}
