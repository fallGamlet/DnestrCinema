package com.fallgamlet.dnestrcinema.mvp.routers;

/**
 * Created by fallgamlet on 30.07.17.
 */

public interface LoginRouter {

    void showLogin();

    void hideLogin();

    void setListener(LoginRouterListener listener);

    interface LoginRouterListener {
        void onLoginShown();
        void onLoginHidden();
    }

}
