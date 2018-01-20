package com.hussainmukadam.watchit.intropage.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hussainmukadam.watchit.R;
import com.hussainmukadam.watchit.intropage.model.Genre;
import com.hussainmukadam.watchit.BaseActivity;
import com.hussainmukadam.watchit.notification.NotificationHelper;
import com.hussainmukadam.watchit.util.CustomSharedPreference;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hussain on 7/21/17.
 */

public class IntroActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{
    private static final String TAG = "IntroActivity";
    PagerAdapter pagerAdapter;
    CustomSharedPreference customSharedPreference;
    List<Genre> moviesGenresList = new ArrayList<>();
    List<Genre> tvGenresList = new ArrayList<>();

    @BindView(R.id.intro_viewpager)
    ViewPager introViewpager;
    @BindView(R.id.intro_tab_layout)
    TabLayout introTabLayout;
    @BindView(R.id.intro_button_right)
    ImageView introButtonRight;
    @BindView(R.id.intro_button_left)
    ImageView introButtonLeft;
    @BindView(R.id.intro_button_done)
    TextView introButtonDone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ButterKnife.bind(this);

        customSharedPreference = new CustomSharedPreference(this);
        enableSuggestionNotifications();

        if(customSharedPreference.isIntroDisplayed()) {
            Intent startMainActivity = new Intent(IntroActivity.this, BaseActivity.class);
            startActivity(startMainActivity);
            finish();
        } else {
            pagerAdapter = new PagerAdapter(getSupportFragmentManager());
            introViewpager.setAdapter(pagerAdapter);
            introTabLayout.setupWithViewPager(introViewpager);

            introViewpager.setOnPageChangeListener(this);

            introViewpager.setPageTransformer(false, new IntroPageTransformer());
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.d(TAG, "onPageSelected: Position of Page "+position);
        switch(position) {
            case 0:
                introButtonRight.setVisibility(View.VISIBLE);
                introButtonLeft.setVisibility(View.GONE);
                introButtonDone.setVisibility(View.GONE);
                break;
            case 1:
                introButtonRight.setVisibility(View.GONE);
                introButtonLeft.setVisibility(View.VISIBLE);
                introButtonDone.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public static class PagerAdapter extends FragmentPagerAdapter{

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    return IntroMoviesFragment.newInstance("MOVIES", 0);
                case 1:
                    return IntroTvSeriesFragment.newInstance("TV SERIES", 1);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    @OnClick(R.id.intro_button_right)
    public void nextPage(){
        introViewpager.setCurrentItem(introViewpager.getCurrentItem()+1);
    }

    @OnClick(R.id.intro_button_left)
    public void previousPage(){
        introViewpager.setCurrentItem(introViewpager.getCurrentItem()-1);
    }

    @OnClick(R.id.intro_button_done)
    public void donePage(){
        if(customSharedPreference.getMoviesGenrePreference()!=null && customSharedPreference.getTvGenrePreference()!=null) {
            if(customSharedPreference.getMoviesGenrePreference().size() >= 3 && customSharedPreference.getTvGenrePreference().size() >= 3) {
                customSharedPreference.setIntroDisplayed(true);
                Intent startMainActivity = new Intent(IntroActivity.this, BaseActivity.class);
                startActivity(startMainActivity);
                finish();
            } else {
                Toast.makeText(this, "Please select 3 genres in each category, for better suggestions!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please select 3 genres in each category, for better suggestions!", Toast.LENGTH_SHORT).show();
        }
    }

    private void enableSuggestionNotifications(){
        NotificationHelper.scheduleRepeatingRTCNotification(this);
        NotificationHelper.enableBootReceiver(this);
    }
}
