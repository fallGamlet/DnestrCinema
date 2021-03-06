package com.fallgamlet.dnestrcinema.mvp.presenters;

import android.os.Bundle;

import com.fallgamlet.dnestrcinema.mvp.views.MvpView;

/**
 * Created by fallgamlet on 09.04.17.
 */

public interface MvpPresenter<T extends MvpView> {

    T getView();

    void bindView(T view);

    void unbindView();

    void onSave(Bundle bundle);

    void onRestore(Bundle bundle);

    void loadData();

}
