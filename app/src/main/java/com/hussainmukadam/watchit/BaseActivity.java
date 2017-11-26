package com.hussainmukadam.watchit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;

import com.hussainmukadam.watchit.R;
import com.hussainmukadam.watchit.mainpage.view.MainFragment;
import com.hussainmukadam.watchit.opensourcepage.OpenSourceFragment;
import com.hussainmukadam.watchit.watchlaterpage.view.WatchLaterFragment;
import com.hussainmukadam.watchit.preferencepage.SettingsFragment;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

/**
 * Created by hussain on 7/21/17.
 */

public class BaseActivity extends AppCompatActivity implements MainFragment.OnMenuBarClicked{
    private static final String TAG = "BaseActivity";
    Bundle savedData = new Bundle();
    Drawer drawer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setupDrawer();

        Bundle bundle = new Bundle();
        bundle.putBoolean("IS_MOVIES", true);

        switchFragment(new MainFragment(), bundle, "MOVIES_FRAG");
    }

    private void setupDrawer() {
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(true)
                .withStickyFooter(R.layout.nav_footer)
                .addDrawerItems(
                        new SectionDrawerItem().withName("Explore").withDivider(false),
                        new PrimaryDrawerItem().withName("Movies")
                                .withIcon(new IconicsDrawable(this).icon(MaterialDesignIconic.Icon.gmi_movie_alt).sizeDp(24)),
                        new PrimaryDrawerItem().withName("TV Series")
                                .withIcon(new IconicsDrawable(this).icon(MaterialDesignIconic.Icon.gmi_tv_alt_play).sizeDp(24)),
                        new PrimaryDrawerItem().withName("Watch Later")
                                .withIcon(new IconicsDrawable(this).icon(MaterialDesignIconic.Icon.gmi_calendar_check).sizeDp(24)),
                        new SectionDrawerItem().withName("About"),
                        new SecondaryDrawerItem().withName("Settings")
                                .withIcon(new IconicsDrawable(this).icon(MaterialDesignIconic.Icon.gmi_settings).sizeDp(24).colorRes(R.color.colorLightGray)),
                        new SecondaryDrawerItem().withName("Open Source Acknowledgements")
                                .withIcon(new IconicsDrawable(this).icon(MaterialDesignIconic.Icon.gmi_github_alt).sizeDp(24).colorRes(R.color.colorLightGray)))
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Log.d(TAG, "onItemClick: Position " + position);
                        switch (position) {
                            case 1:
                                Bundle bundleMovies = new Bundle();
                                bundleMovies.putBoolean("IS_MOVIES", true);
                                switchFragment(new MainFragment(), bundleMovies, "MOVIES_FRAG");
                                break;
                            case 2:
                                Bundle bundleTv = new Bundle();
                                bundleTv.putBoolean("IS_MOVIES", false);
                                switchFragment(new MainFragment(), bundleTv, "TV_FRAG");
                                break;
                            case 3:
                                switchFragment(new WatchLaterFragment(), null, "WATCH_LATER_FRAG");
                                break;
                            case 5:
                                switchFragment(new SettingsFragment(), null, "SETTINGS_FRAG");
                                break;
                            case 6:
                                switchFragment(new OpenSourceFragment(), null, "OPEN_SOURCE_FRAG");
                                break;
                        }
                        return false;
                    }
                })
                .build();
    }

    private void switchFragment(Fragment mFragment, Bundle bundle, String tag) {
        FragmentManager manager = getSupportFragmentManager();

        if (bundle != null) {
            mFragment.setArguments(bundle);
            manager.beginTransaction()
                    .replace(R.id.container, mFragment, tag)
                    .commit();
        } else {
            manager.beginTransaction()
                    .replace(R.id.container, mFragment, tag)
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
        drawer.openDrawer();
    }
}
