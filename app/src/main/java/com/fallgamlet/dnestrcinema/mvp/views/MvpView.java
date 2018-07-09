package com.fallgamlet.dnestrcinema.mvp.views;

import android.content.Context;

import com.fallgamlet.dnestrcinema.mvp.presenters.MvpPresenter;

/**
 * Created by fallgamlet on 09.04.17.
 */

public interface MvpView<P extends MvpPresenter> {

    Context getContext();

    P getPresenter();

    void setPresenter(P presenter);
}
