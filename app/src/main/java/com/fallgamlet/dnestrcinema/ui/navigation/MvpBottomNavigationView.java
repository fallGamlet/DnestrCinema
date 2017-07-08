package com.fallgamlet.dnestrcinema.ui.navigation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.fallgamlet.dnestrcinema.R;
import com.fallgamlet.dnestrcinema.mvp.presenters.MvpNavigationPresenter;
import com.fallgamlet.dnestrcinema.mvp.views.MvpNavigationView;

/**
 * Created by fallgamlet on 02.07.17.
 */

public class MvpBottomNavigationView
        implements
        MvpNavigationView,
        BottomNavigationView.OnNavigationItemSelectedListener,
        BottomNavigationView.OnNavigationItemReselectedListener
{

    private BottomNavigationView navigationView;
    private Context context;
    private MvpNavigationPresenter presenter;


    public MvpBottomNavigationView(BottomNavigationView view) {
        this.navigationView = view;
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

    @Override
    public Context getContext() {
        return this.context;
    }

    @Override
    public MvpNavigationPresenter getPresenter() {
        return this.presenter;
    }

    @Override
    public void setPresenter(MvpNavigationPresenter presenter) {
        this.presenter = presenter;
    }
}
