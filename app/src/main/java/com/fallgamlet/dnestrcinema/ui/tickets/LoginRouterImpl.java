package com.fallgamlet.dnestrcinema.ui.tickets;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentManager;

import com.fallgamlet.dnestrcinema.mvp.models.Config;
import com.fallgamlet.dnestrcinema.mvp.routers.LoginRouter;
import com.fallgamlet.dnestrcinema.mvp.views.Fragments;
import com.fallgamlet.dnestrcinema.utils.LogUtils;

/**
 * Created by fallgamlet on 30.07.17.
 */

public class LoginRouterImpl
        implements LoginRouter
{
    private FragmentManager manager;
    private boolean isLoginShown;
    private int replaceId;
    private Fragments.MvpLoginViewFragment fragment;
    private LoginRouterListener listener;

    public LoginRouterImpl(FragmentManager manager, @IdRes int replaceId) {
        isLoginShown = false;
        this.manager = manager;
        this.replaceId = replaceId;
    }

    public void setListener(LoginRouterListener listener) {
        this.listener = listener;
    }

    @Override
    public void showLogin() {
        if (!isLoginShown) {
            fragment = Config.getInstance().getFragmentFactory().createLoginView();
            fragment.getPresenter().setRouter(this);

            try {
                manager.beginTransaction()
//                        .add(fragment, fragment.getClass().getName())
                        .replace(replaceId, fragment)
                        .commit();
            }
            catch (Exception e) {
                LogUtils.log("", "", e);
            }
            isLoginShown = true;

            if (listener != null) {
                listener.onLoginShown();
            }
        }
    }

    @Override
    public void hideLogin() {
        if (isLoginShown) {
            manager.beginTransaction()
                    .remove(fragment)
                    .commit();

            fragment = null;
            isLoginShown = false;

            if (listener != null) {
                listener.onLoginHidden();
            }
        }
    }

}
