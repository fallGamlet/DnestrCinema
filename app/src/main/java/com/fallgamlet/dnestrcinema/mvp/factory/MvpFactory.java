package com.fallgamlet.dnestrcinema.mvp.factory;

import android.support.v4.app.Fragment;

import com.fallgamlet.dnestrcinema.mvp.presenters.MvpPresenter;
import com.fallgamlet.dnestrcinema.mvp.views.MvpView;

/**
 * Created by fallgamlet on 03.07.17.
 */

public interface MvpFactory<V extends MvpView, P extends MvpPresenter> {

    Fragment getFragment();

    V getView();

    P getPresenter();

    String getTag();

}
