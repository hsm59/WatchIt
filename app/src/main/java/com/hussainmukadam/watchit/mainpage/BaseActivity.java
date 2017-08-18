package com.hussainmukadam.watchit.mainpage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.hussainmukadam.watchit.R;
import com.hussainmukadam.watchit.mainpage.view.MovieFragment;
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
    @BindView(R.id.main_toolbar)
    Toolbar mainToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mainToolbar);
        setupDrawer();

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .add(R.id.container, new MovieFragment())
                .commit();
    }

    private void setupDrawer(){
        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(mainToolbar)
                .addDrawerItems(
                        new SectionDrawerItem().withName("Explore").withDivider(false),
                        new PrimaryDrawerItem().withName("Movies").withIcon(R.drawable.ic_local_movies_black_24dp),
                        new PrimaryDrawerItem().withName("TV Series").withIcon(R.drawable.ic_tv_black_24dp),
                        new SectionDrawerItem().withName("About"),
                        new PrimaryDrawerItem().withName("Settings").withIcon(R.drawable.ic_settings_black_24dp),
                        new PrimaryDrawerItem().withName("Open Source Acknowledgement").withIcon(R.drawable.ic_public_black_24dp))
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        return false;
                    }
                })
                .build();

        result.setSelection(1);
    }
}
