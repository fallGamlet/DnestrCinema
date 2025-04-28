package com.fallgamlet.dnestrcinema.ui.navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fallgamlet.dnestrcinema.R;
import com.fallgamlet.dnestrcinema.mvp.views.Fragments;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MvpNavigationFragment
        extends
            Fragments.MvpNavigationViewFragment
        implements
            BottomNavigationView.OnNavigationItemSelectedListener,
            BottomNavigationView.OnNavigationItemReselectedListener
{

    BottomNavigationView navigationView;
    View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.bottom_navigation, container, false);
        navigationView = rootView.findViewById(R.id.bottomNavigationView);
        initListeners();

        return rootView;
    }

    private void initListeners() {
        this.navigationView.setOnNavigationItemSelectedListener(this);
        this.navigationView.setOnNavigationItemReselectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        boolean checked;

        int itemId = item.getItemId();
        if (itemId == R.id.actionToday) {
            getPresenter().onTodaySelected();
            checked = true;
        } else if (itemId == R.id.actionSoon) {
            getPresenter().onSoonSelected();
            checked = true;
        } else if (itemId == R.id.actionNews) {
            getPresenter().onNewsSelected();
            checked = true;
        } else if (itemId == R.id.actionAbout) {
            getPresenter().onAboutSelected();
            checked = true;
        } else {
            checked = false;
        }

        return checked;
    }

    @Override
    public void onNavigationItemReselected(@NonNull MenuItem item) {

    }


    @Override
    public void selectToday() {
        if (this.navigationView.getSelectedItemId() != R.id.actionToday) {
            this.navigationView.setSelectedItemId(R.id.actionToday);
        }
    }

    @Override
    public void selectSoon() {
        if (this.navigationView.getSelectedItemId() != R.id.actionSoon) {
            this.navigationView.setSelectedItemId(R.id.actionSoon);
        }
    }

    @Override
    public void selectAbout() {
        if (this.navigationView.getSelectedItemId() != R.id.actionAbout) {
            this.navigationView.setSelectedItemId(R.id.actionAbout);
        }
    }

    @Override
    public void selectNews() {
        if (this.navigationView.getSelectedItemId() != R.id.actionNews) {
            this.navigationView.setSelectedItemId(R.id.actionNews);
        }
    }
}
