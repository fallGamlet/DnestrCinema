package com.fallgamlet.dnestrcinema.mvp.views;

import android.support.v4.app.Fragment;

import com.fallgamlet.dnestrcinema.mvp.presenters.MvpPresenter;

/**
 * Created by fallgamlet on 09.04.17.
 */

public abstract class MvpBaseFragment<P extends MvpPresenter>
        extends Fragment
        implements MvpView <P>
{
    private P presenter;

    public P getPresenter() {
        return presenter;
    }

    @Override
    public void setPresenter(P presenter) {
        this.presenter = presenter;
    }

    public boolean isPresenterExist() {
        return this.presenter != null;
    }
}
