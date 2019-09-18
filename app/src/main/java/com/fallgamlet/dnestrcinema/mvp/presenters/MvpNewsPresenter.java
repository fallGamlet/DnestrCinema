package com.fallgamlet.dnestrcinema.mvp.presenters;

import com.fallgamlet.dnestrcinema.mvp.views.MvpNewsView;

/**
 * Created by fallgamlet on 03.07.17.
 */

@Deprecated
public interface MvpNewsPresenter extends MvpPresenter<MvpNewsView> {

    void onRefresh();

}
