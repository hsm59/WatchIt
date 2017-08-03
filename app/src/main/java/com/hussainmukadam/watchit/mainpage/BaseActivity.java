package com.hussainmukadam.watchit.mainpage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.hussainmukadam.watchit.R;
import com.hussainmukadam.watchit.mainpage.view.MainFragment;

/**
 * Created by hussain on 7/21/17.
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .add(R.id.container, new MainFragment())
                .commit();
    }
}
