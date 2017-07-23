package com.hussainmukadam.watchit.mainpage.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.hussainmukadam.watchit.R;

/**
 * Created by hussain on 7/21/17.
 */

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .add(R.id.container, new MainFragment())
                .addToBackStack("f1")
                .commit();
    }
}
