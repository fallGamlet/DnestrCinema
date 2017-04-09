package com.fallgamlet.dnestrcinema.mvp.views;

import android.content.Context;
import com.fallgamlet.dnestrcinema.mvp.presenters.IPresenter;

/**
 * Created by fallgamlet on 09.04.17.
 */

public interface IView <T extends IPresenter> {
    Context getContext();
}
