package com.fallgamlet.dnestrcinema.mvp.presenters;

import android.os.Bundle;

import com.fallgamlet.dnestrcinema.mvp.views.IView;

/**
 * Created by fallgamlet on 09.04.17.
 */

abstract class BasePresenter<T extends IView> implements IPresenter<T> {
    protected T mView;

    @Override
    public void setView(T view) {
        mView = view;
    }

    @Override
    public T getView() {
        return mView;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {
        setView(null);
    }

    @Override
    public void onPause(Bundle bundle) {

    }

    @Override
    public void onResume(Bundle bundle) {

    }
}
