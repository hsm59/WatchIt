package com.hussainmukadam.watchit;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hussainmukadam.watchit.R;
import com.hussainmukadam.watchit.detailpage.view.DetailsFragment;
import com.hussainmukadam.watchit.mainpage.model.Movie;
import com.hussainmukadam.watchit.mainpage.view.MainFragment;
import com.hussainmukadam.watchit.notification.NotificationHelper;
import com.hussainmukadam.watchit.opensourcepage.OpenSourceFragment;
import com.hussainmukadam.watchit.preferencepage.GenreFragment;
import com.hussainmukadam.watchit.preferencepage.PreferenceFragment;
import com.hussainmukadam.watchit.util.Util;
import com.hussainmukadam.watchit.watchlaterpage.view.WatchLaterActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hussain on 7/21/17.
 */

public class BaseActivity extends AppCompatActivity implements MainFragment.OnMenuBarClicked {
    private static final String TAG = "BaseActivity";
    Bundle savedData = new Bundle();
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.navi_view)
    NavigationView navigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        ButterKnife.bind(this);


        if (getIntent().hasExtra("NOTIFICATION_DETAIL")) {
            DetailsFragment detailsFragment = new DetailsFragment();
            Bundle bundleData = new Bundle();
            bundleData.putParcelable("NOTIFICATION_ITEM", getIntent().getParcelableExtra("NOTIFICATION_DETAIL"));
            detailsFragment.setArguments(bundleData);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, detailsFragment, "DetailsFragment")
                    .commitAllowingStateLoss();
        } else {
            Bundle bundle = new Bundle();
            bundle.putBoolean("IS_MOVIES", true);
            switchFragment(new MainFragment(), bundle, "MOVIES_FRAG");
        }

        handleNavigationDrawerItems();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("NOTIFICATION_DETAIL")) {
                Log.d(TAG, "onNewIntent: Inside new Intent");
                setContentView(R.layout.activity_main);
                // extract the extra-data in the Notification

                DetailsFragment detailsFragment = new DetailsFragment();
                Bundle bundleData = new Bundle();
                bundleData.putParcelable("NOTIFICATION_ITEM", extras.getParcelable("NOTIFICATION_DETAIL"));
                detailsFragment.setArguments(bundleData);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, detailsFragment, "DetailsFragment")
                        .commitAllowingStateLoss();
            }
        }
    }

    private void switchFragment(Fragment mFragment, Bundle bundle, String tag) {
        FragmentManager manager = getSupportFragmentManager();
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.container);

        if (currentFragment != null)
            Log.d(TAG, "switchFragment: current Fragment " + currentFragment.getTag());

        if (bundle != null) {
            mFragment.setArguments(bundle);
            manager.beginTransaction()
                    .replace(R.id.container, mFragment, tag)
                    .commit();
        } else {
            manager.beginTransaction()
                    .add(R.id.container, mFragment, tag)
                    .hide(currentFragment)
                    .addToBackStack(null)
                    .commit();
        }


    }

    public void saveData(Bundle bundle) {
        savedData = bundle;
    }

    public Bundle getData() {
        return savedData;
    }

    @Override
    public void menuBarClicked() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    private void handleNavigationDrawerItems() {
        final TextView tvNavMovies = findViewById(R.id.tv_nav_movies);
        final TextView tvNavTvSeries = findViewById(R.id.tv_nav_tv_series);
        SwitchCompat switchCompat = findViewById(R.id.switch_movies_tv);
        LinearLayout llFavorites = findViewById(R.id.ll_favorites);
        LinearLayout llPreference = findViewById(R.id.ll_preference);
        LinearLayout llOpenSource = findViewById(R.id.ll_open_source);

        tvNavMovies.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked) {
                    tvNavMovies.setTextColor(getResources().getColor(R.color.colorLighterGray));
                    tvNavTvSeries.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    Bundle bundleTv = new Bundle();
                    bundleTv.putBoolean("IS_MOVIES", false);
                    switchFragment(new MainFragment(), bundleTv, "TV_FRAG");
                } else {
                    tvNavMovies.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    tvNavTvSeries.setTextColor(getResources().getColor(R.color.colorLighterGray));
                    Bundle bundleMovies = new Bundle();
                    bundleMovies.putBoolean("IS_MOVIES", true);
                    switchFragment(new MainFragment(), bundleMovies, "MOVIES_FRAG");
                }
                drawerLayout.closeDrawers();
            }
        });

        llFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent watchLaterIntent = new Intent(BaseActivity.this, WatchLaterActivity.class);
                startActivity(watchLaterIntent);
                drawerLayout.closeDrawers();
            }
        });


        llPreference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFragment(new PreferenceFragment(), null, "SETTINGS_FRAG");
                drawerLayout.closeDrawers();
            }
        });

        llOpenSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFragment(new OpenSourceFragment(), null, "OPEN_SOURCE_FRAG");
                drawerLayout.closeDrawers();
            }
        });
    }
}
