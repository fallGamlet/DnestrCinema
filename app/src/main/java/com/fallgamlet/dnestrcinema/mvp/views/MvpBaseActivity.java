package com.fallgamlet.dnestrcinema.mvp.views;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.fallgamlet.dnestrcinema.mvp.presenters.MvpPresenter;
import com.fallgamlet.dnestrcinema.mvp.views.MvpView;

/**
 * Created by fallgamlet on 09.04.17.
 */

public abstract class MvpBaseActivity <P extends MvpPresenter>
        extends AppCompatActivity
        implements MvpView <P>
{
    private P presenter;


    @Override
    public Context getContext() {
        return this;
    }

    public P getPresenter() {
        return presenter;
    }

    @Override
    public void setPresenter(P presenter) {
        this.presenter = presenter;
    }
}
