package com.fallgamlet.dnestrcinema.mvp.factory;

import android.support.v4.app.Fragment;

import com.fallgamlet.dnestrcinema.mvp.presenters.MvpPresenter;
import com.fallgamlet.dnestrcinema.mvp.views.MvpView;

/**
 * Created by fallgamlet on 03.07.17.
 */

public abstract class BaseFactory<F extends Fragment, V extends MvpView, P extends MvpPresenter>
        implements MvpFactory<V, P>
{

    protected F fragment;
    protected P presenter;
    protected V view;


    @Override
    public F getFragment() {
        return this.fragment;
    }

    @Override
    public V getView() {
        return this.view;
    }

    @Override
    public P getPresenter() {
        return this.presenter;
    }

    @Override
    public String getTag() {
        return null;
    }
}
