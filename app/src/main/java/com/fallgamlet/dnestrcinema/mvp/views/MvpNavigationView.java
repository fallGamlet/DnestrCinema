package com.fallgamlet.dnestrcinema.mvp.views;

import com.fallgamlet.dnestrcinema.mvp.presenters.MvpNavigationPresenter;

/**
 * Created by fallgamlet on 02.07.17.
 */

public interface MvpNavigationView extends MvpView<MvpNavigationPresenter> {

    void selectToday();

    void selectSoon();

    void selectAbout();

    void selectNews();

}
