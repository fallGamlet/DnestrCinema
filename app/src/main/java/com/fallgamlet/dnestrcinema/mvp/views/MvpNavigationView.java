package com.fallgamlet.dnestrcinema.mvp.views;

import com.fallgamlet.dnestrcinema.mvp.presenters.NavigationPresenter;

/**
 * Created by fallgamlet on 02.07.17.
 */

public interface MvpNavigationView extends MvpView<NavigationPresenter> {

    void selectToday();

    void selectSoon();

    void selectTickets();

    void selectAbout();

    void selectNews();

}
