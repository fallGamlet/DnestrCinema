package com.fallgamlet.dnestrcinema;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.fallgamlet.dnestrcinema.mvp.views.IView;

/**
 * Created by fallgamlet on 09.04.17.
 */

abstract class BaseActivity extends AppCompatActivity implements IView {
    @Override
    public Context getContext() {
        return null;
    }
}
