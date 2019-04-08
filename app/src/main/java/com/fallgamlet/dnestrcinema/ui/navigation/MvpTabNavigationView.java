package com.fallgamlet.dnestrcinema.ui.navigation;

import android.content.Context;
import com.google.android.material.tabs.TabLayout;
import android.view.View;

import com.fallgamlet.dnestrcinema.mvp.presenters.MvpNavigationPresenter;
import com.fallgamlet.dnestrcinema.mvp.views.MvpNavigationView;

/**
 * Created by fallgamlet on 02.07.17.
 */

public class MvpTabNavigationView
        implements
        MvpNavigationView, TabLayout.OnTabSelectedListener {

    private TabLayout navigationView;
    private Context context;
    private MvpNavigationPresenter presenter;


    public MvpTabNavigationView(TabLayout view) {
        this.navigationView = view;
        this.context = view.getContext();

        int pos = view.getSelectedTabPosition();
        TabLayout.Tab tab = view.getTabAt(pos);


        initListeners();
    }

    private void initListeners() {
        this.navigationView.addOnTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        View view = tab.getCustomView();
        int pos = tab.getPosition();
//        if (tabItemToday.isSelected()) {
//            presenter.onTodaySelected();
//        }
//        else if (tabItemSoon.isSelected()) {
//            presenter.onSoonSelected();
//        }
//        else if (tabItemTickets.isSelected()) {
//            presenter.onTicketsSelected();
//        }
//        else if (tabItemNews.isSelected()) {
//            presenter.onNewsSelected();
//        }
//        else if (tabItemAbout.isSelected()) {
//            presenter.onAboutSelected();
//        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


    @Override
    public void selectToday() {
//        this.tabItemToday.setSelected(true);
    }

    @Override
    public void selectSoon() {
//        this.tabItemSoon.setSelected(true);
    }

    @Override
    public void selectTickets() {
//        this.tabItemTickets.setSelected(true);
    }

    @Override
    public void selectAbout() {
//        this.tabItemAbout.setSelected(true);
    }

    @Override
    public void selectNews() {
//        this.tabItemNews.setSelected(true);
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
