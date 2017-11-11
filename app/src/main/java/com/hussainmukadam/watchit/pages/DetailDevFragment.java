package com.hussainmukadam.watchit.pages;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.hussainmukadam.watchit.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hussain on 28/10/17.
 */

public class DetailDevFragment extends Fragment {
    @BindView(R.id.building_animation_watch_later)
    LottieAnimationView buildingAnimation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_watch_later, container, false);
        ButterKnife.bind(this, view);

        return view;
    }
}
