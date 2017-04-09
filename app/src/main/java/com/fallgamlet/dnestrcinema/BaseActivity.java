package com.fallgamlet.dnestrcinema.mvp.views;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by fallgamlet on 09.04.17.
 */

abstract class BaseActivity extends AppCompatActivity implements IView {
    Context mContext = null;

    @Override
    public void attached() {

    }

    @Override
    public void detached() {

    }

    @Override
    public Context getContext() {
        return mContext;
    }
}
