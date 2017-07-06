package com.fallgamlet.dnestrcinema.mvp.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.fallgamlet.dnestrcinema.R;
import com.fallgamlet.dnestrcinema.mvp.presenters.NavigationPresenter;

/**
 * Created by fallgamlet on 02.07.17.
 */

public class MvpNavigationViewImpl
        implements
        MvpNavigationView,
        BottomNavigationView.OnNavigationItemSelectedListener,
        BottomNavigationView.OnNavigationItemReselectedListener
{

    private BottomNavigationView navigationView;
    private Context context;
    private NavigationPresenter presenter;


    public MvpNavigationViewImpl(BottomNavigationView view, NavigationPresenter presenter) {
        this.navigationView = view;
        this.presenter = presenter;
        this.context = view.getContext();

        initListeners();
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
                presenter.onTodaySelected();
                checked = true;
                break;
            case R.id.actionSoon:
                presenter.onSoonSelected();
                checked = true;
                break;
            case R.id.actionTickets:
                presenter.onTicketsSelected();
                checked = true;
                break;
            case R.id.actionNews:
                presenter.onNewsSelected();
                checked = true;
                break;
            case R.id.actionAbout:
                presenter.onAboutSelected();
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
        this.navigationView.setSelectedItemId(R.id.actionToday);
    }

    @Override
    public void selectSoon() {
        this.navigationView.setSelectedItemId(R.id.actionSoon);
    }

    @Override
    public void selectTickets() {
        this.navigationView.setSelectedItemId(R.id.actionTickets);
    }

    @Override
    public void selectAbout() {
        this.navigationView.setSelectedItemId(R.id.actionAbout);
    }

    @Override
    public void selectNews() {
        this.navigationView.setSelectedItemId(R.id.actionNews);
    }

    @Override
    public Context getContext() {
        return this.context;
    }

    @Override
    public NavigationPresenter getPresenter() {
        return this.presenter;
    }

    @Override
    public void setPresenter(NavigationPresenter presenter) {
        this.presenter = presenter;
    }
}
