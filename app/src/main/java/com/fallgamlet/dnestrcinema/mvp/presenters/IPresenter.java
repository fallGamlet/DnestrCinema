package com.fallgamlet.dnestrcinema.mvp.presenters;

import android.os.Bundle;

import com.fallgamlet.dnestrcinema.mvp.views.IView;

/**
 * Created by fallgamlet on 09.04.17.
 */

public interface IPresenter <T extends IView> {
    void setView(T view);
    T getView();
    void onStart();
    void onStop();
    void onPause(Bundle bundle);
    void onResume(Bundle bundle);
}
