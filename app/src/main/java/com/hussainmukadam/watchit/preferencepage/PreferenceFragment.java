package com.hussainmukadam.watchit.preferencepage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.hussainmukadam.watchit.BaseActivity;
import com.hussainmukadam.watchit.R;
import com.hussainmukadam.watchit.detailpage.view.DetailsFragment;
import com.hussainmukadam.watchit.mainpage.model.Movie;
import com.hussainmukadam.watchit.mainpage.view.MainFragment;
import com.hussainmukadam.watchit.notification.NotificationHelper;
import com.hussainmukadam.watchit.util.CustomSharedPreference;
import com.hussainmukadam.watchit.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by hussain on 25/03/18.
 */

public class PreferenceFragment extends Fragment {

    @BindView(R.id.toolbar_main)
    Toolbar toolbarMain;
    @BindView(R.id.switch_suggestion_notification)
    SwitchCompat switchSuggestionNotification;
    @BindView(R.id.switch_top_rated_only)
    SwitchCompat switchTopRatedOnly;

    CustomSharedPreference customSharedPreference;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preference, container, false);
        unbinder = ButterKnife.bind(this, view);

        toolbarMain.setTitle("Settings");
        toolbarMain.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        customSharedPreference = new CustomSharedPreference(getContext());
        toolbarMain.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null)
                    getActivity().onBackPressed();
            }
        });

        if (customSharedPreference.isTopRatedOnly())
            switchTopRatedOnly.setChecked(true);
        else
            switchTopRatedOnly.setChecked(false);

        if (customSharedPreference.suggestNotification())
            switchSuggestionNotification.setChecked(true);
        else
            switchSuggestionNotification.setChecked(false);

        switchSuggestionNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    NotificationHelper.scheduleRepeatingRTCNotification(getContext());
                    NotificationHelper.enableBootReceiver(getContext());
                    customSharedPreference.setSuggestNotificationFlag(true);
                } else {
                    NotificationHelper.cancelAlarmRTC();
                    NotificationHelper.disableBootReceiver(getContext());
                    customSharedPreference.setSuggestNotificationFlag(false);
                }
            }
        });

        switchTopRatedOnly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    customSharedPreference.setTopRatedUrl(true);
                else
                    customSharedPreference.setTopRatedUrl(false);
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.cl_genre_change_pref)
    void onClickChangeGenrePrefs() {
        GenreFragment genreFragment = new GenreFragment();

        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.hide(PreferenceFragment.this);
        ft.add(R.id.container, genreFragment, "GENRE_FRAGMENT");
        ft.addToBackStack(null);
        ft.commit();
    }


}
