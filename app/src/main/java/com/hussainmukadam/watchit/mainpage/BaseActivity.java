package com.hussainmukadam.watchit.mainpage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.hussainmukadam.watchit.R;
import com.hussainmukadam.watchit.mainpage.view.MainFragment;

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

        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        mainToolbar.setNavigationIcon(R.drawable.nav_bar_icon);

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .add(R.id.container, new MainFragment())
                .commit();
    }
}
