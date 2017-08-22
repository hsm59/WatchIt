package com.hussainmukadam.watchit.mainpage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.hussainmukadam.watchit.R;
import com.hussainmukadam.watchit.mainpage.view.MainFragment;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hussain on 7/21/17.
 */

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
    @BindView(R.id.main_toolbar)
    Toolbar mainToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mainToolbar);
        setupDrawer();

        Bundle bundle = new Bundle();
        bundle.putBoolean("IS_MOVIES", true);

        switchFragment(new MainFragment(), bundle);
    }

    private void setupDrawer(){
        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(mainToolbar)
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
                        new SecondaryDrawerItem().withName("Open Source Acknowledgement")
                                .withIcon(new IconicsDrawable(this).icon(MaterialDesignIconic.Icon.gmi_github_alt).sizeDp(24).colorRes(R.color.colorLightGray)))
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Log.d(TAG, "onItemClick: Position "+position);
                        switch(position) {
                            case 1:
                                //Movies Fragment
                                Bundle bundleMovies = new Bundle();
                                bundleMovies.putBoolean("IS_MOVIES", true);
                                switchFragment(new MainFragment(), bundleMovies);
                                break;
                            case 2:
                                //TV Fragment
                                Bundle bundleTv = new Bundle();
                                bundleTv.putBoolean("IS_MOVIES", false);
                                switchFragment(new MainFragment(), bundleTv);
                                break;
                            case 3:
                                //TODO: Watch Later Fragment
                                break;
                            case 5:
                                //TODO: Settings Activity
//                                Intent intent = new Intent(BaseActivity.this, SettingsActivity.class);
//                                startActivity(intent);
                                break;
                            case 6:
                                //TODO: Open Source Acknowledgement Fragment
                                break;
                        }
                        return false;
                    }
                })
                .build();
    }

    private void switchFragment(Fragment mFragment, Bundle bundle){
        mFragment.setArguments(bundle);
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .add(R.id.container, mFragment)
                .commit();
    }
}
