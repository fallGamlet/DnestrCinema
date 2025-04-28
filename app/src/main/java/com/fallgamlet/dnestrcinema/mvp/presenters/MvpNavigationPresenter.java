package com.fallgamlet.dnestrcinema.mvp.presenters;

import com.fallgamlet.dnestrcinema.mvp.views.MvpNavigationView;

/**
 * Created by fallgamlet on 02.07.17.
 */

public interface MvpNavigationPresenter extends MvpPresenter<MvpNavigationView> {

    void onTodaySelected();

    void onSoonSelected();

    void onAboutSelected();

    void onNewsSelected();

}
