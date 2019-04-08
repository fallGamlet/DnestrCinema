package com.fallgamlet.dnestrcinema.mvp.views;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;

import com.fallgamlet.dnestrcinema.mvp.presenters.MvpPresenter;

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
