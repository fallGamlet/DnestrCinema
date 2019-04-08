package com.fallgamlet.dnestrcinema.ui.navigation;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.fallgamlet.dnestrcinema.R;
import com.fallgamlet.dnestrcinema.mvp.views.Fragments;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MvpNavigationFragment
        extends
            Fragments.MvpNavigationViewFragment
        implements
            BottomNavigationView.OnNavigationItemSelectedListener,
            BottomNavigationView.OnNavigationItemReselectedListener
{

    @BindView(R.id.bottomNavigationView)
    BottomNavigationView navigationView;
    View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.bottom_navigation, container, false);

        ButterKnife.bind(this, rootView);

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

        switch (item.getItemId()) {
            case R.id.actionToday:
                getPresenter().onTodaySelected();
                checked = true;
                break;
            case R.id.actionSoon:
                getPresenter().onSoonSelected();
                checked = true;
                break;
            case R.id.actionTickets:
                getPresenter().onTicketsSelected();
                checked = true;
                break;
            case R.id.actionNews:
                getPresenter().onNewsSelected();
                checked = true;
                break;
            case R.id.actionAbout:
                getPresenter().onAboutSelected();
                checked = true;
                break;
            default:
                checked = false;
                break;
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
    public void selectTickets() {
        if (this.navigationView.getSelectedItemId() != R.id.actionTickets) {
            this.navigationView.setSelectedItemId(R.id.actionTickets);
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
