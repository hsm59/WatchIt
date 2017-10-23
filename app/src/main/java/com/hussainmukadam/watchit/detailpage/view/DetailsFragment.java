package com.hussainmukadam.watchit.detailpage.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hussainmukadam.watchit.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hussain on 23/10/17.
 */

public class DetailsFragment extends Fragment {

    @BindView(R.id.iv_poster)
    ImageView ivDetailsPoster;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, view);

        return view;
    }
}
