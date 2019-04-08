package com.fallgamlet.dnestrcinema.ui.navigation;

import android.content.Context;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.Menu;
import android.view.MenuItem;

import com.fallgamlet.dnestrcinema.mvp.models.Config;
import com.fallgamlet.dnestrcinema.mvp.models.NavigationItem;
import com.fallgamlet.dnestrcinema.mvp.presenters.MvpNavigationPresenter;
import com.fallgamlet.dnestrcinema.mvp.views.MvpNavigationView;

/**
 * Created by fallgamlet on 02.07.17.
 */

public class MvpBottomNavigationView
        implements
        MvpNavigationView,
        BottomNavigationView.OnNavigationItemSelectedListener,
        BottomNavigationView.OnNavigationItemReselectedListener, MenuItem.OnMenuItemClickListener {

    private BottomNavigationView navigationView;
    private Context context;
    private MvpNavigationPresenter presenter;


    public MvpBottomNavigationView(BottomNavigationView view) {
        this.navigationView = view;
        this.context = view.getContext();

        initNavigationItems();
        initListeners();
    }

    private void initNavigationItems() {
        Menu menu = this.navigationView.getMenu();
        menu.clear();

        for (NavigationItem item: Config.getInstance().createNavigations()) {
            int groupId = Menu.NONE;
            int id = item.getId();
            int order = Menu.NONE;
            int title = item.getTitleId();
            MenuItem menuItem = menu.add(groupId, id, order, title);
            menuItem.setIcon(item.getIconResId());
            menuItem.setVisible(true);
            menuItem.setCheckable(true);
            menuItem.setEnabled(true);
            menuItem.setOnMenuItemClickListener(this);
        }
    }

    private void initListeners() {
        this.navigationView.setOnNavigationItemSelectedListener(this);
        this.navigationView.setOnNavigationItemReselectedListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        return onNavigationItemSelected(menuItem);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        boolean checked;

        switch (item.getItemId()) {
            case NavigationItem.NavigationId.TODAY:
                presenter.onTodaySelected();
                checked = true;
                break;
            case NavigationItem.NavigationId.SOON:
                presenter.onSoonSelected();
                checked = true;
                break;
            case NavigationItem.NavigationId.TICKETS:
                presenter.onTicketsSelected();
                checked = true;
                break;
            case NavigationItem.NavigationId.NEWS:
                presenter.onNewsSelected();
                checked = true;
                break;
            case NavigationItem.NavigationId.ABOUT:
                presenter.onAboutSelected();
                checked = true;
                break;
            default:
                checked = false;
                break;
        }

        item.setChecked(checked);

        return checked;
    }

    @Override
    public void onNavigationItemReselected(@NonNull MenuItem item) {

    }


    @Override
    public void selectToday() {
        selectItemById(NavigationItem.NavigationId.TODAY);
    }

    @Override
    public void selectSoon() {
        selectItemById(NavigationItem.NavigationId.SOON);
    }

    @Override
    public void selectTickets() {
        selectItemById(NavigationItem.NavigationId.TICKETS);
    }

    @Override
    public void selectAbout() {
        selectItemById(NavigationItem.NavigationId.ABOUT);
    }

    @Override
    public void selectNews() {
        selectItemById(NavigationItem.NavigationId.NEWS);
    }

    private void selectItemById(int id) {
        int curId = this.navigationView.getSelectedItemId();
        if (curId != id) {
            this.navigationView.setSelectedItemId(id);
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
