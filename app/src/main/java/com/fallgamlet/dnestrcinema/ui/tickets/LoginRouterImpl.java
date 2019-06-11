package com.fallgamlet.dnestrcinema.ui.tickets;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentManager;

import com.fallgamlet.dnestrcinema.app.AppFacade;
import com.fallgamlet.dnestrcinema.mvp.routers.LoginRouter;
import com.fallgamlet.dnestrcinema.mvp.views.Fragments;
import com.fallgamlet.dnestrcinema.utils.LogUtils;


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
            fragment = (Fragments.MvpLoginViewFragment)
                    AppFacade.Companion.getInstance().getFragmentFactory().createLoginView();
            fragment.getPresenter().setRouter(this);

            try {
                manager.beginTransaction()
//                        .add(fragment, fragment.getClass().getName())
                        .replace(replaceId, fragment)
                        .commit();
            }
            catch (Exception e) {
                LogUtils.INSTANCE.log("", "", e);
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
