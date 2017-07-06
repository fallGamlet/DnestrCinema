package com.fallgamlet.dnestrcinema.mvp.presenters;

import android.os.Bundle;

import com.fallgamlet.dnestrcinema.mvp.views.MvpView;

/**
 * Created by fallgamlet on 09.04.17.
 */

public abstract class BasePresenter<T extends MvpView> implements MvpPresenter<T> {
    private T view;

    @Override
    public void bindView(T view) {
        this.view = view;
    }

    @Override
    public void unbindView() {
        bindView(null);
    }

    @Override
    public T getView() {
        return view;
    }

    @Override
    public void onDestroy() {
        unbindView();
    }

    @Override
    public void onPause(Bundle bundle) {

    }

    @Override
    public void onResume(Bundle bundle) {

    }

    public boolean isViewBinded() {
        return view != null;
    }
}
