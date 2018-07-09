package com.fallgamlet.dnestrcinema.mvp.presenters;

import com.fallgamlet.dnestrcinema.mvp.routers.LoginRouter;
import com.fallgamlet.dnestrcinema.mvp.views.MvpLoginView;

/**
 * Created by fallgamlet on 03.07.17.
 */

public interface MvpLoginPresenter
        extends MvpPresenter <MvpLoginView>
{

    void onLoginChanged(String value);

    void onPasswordChanged(String value);

    void onLogin();

    void setRouter(LoginRouter router);

}
