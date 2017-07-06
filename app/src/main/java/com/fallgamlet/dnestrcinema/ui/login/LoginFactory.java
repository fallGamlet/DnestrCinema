package com.fallgamlet.dnestrcinema.ui.login;

import com.fallgamlet.dnestrcinema.mvp.factory.BaseFactory;

/**
 * Created by fallgamlet on 03.07.17.
 */

public class LoginFactory
        extends BaseFactory<LoginFragment, MvpLoginView, LoginPresenter>
{

    public LoginFactory() {
        LoginFragment fragment = new LoginFragment();
        LoginPresenterImpl presenter = new LoginPresenterImpl();

        this.fragment = fragment;
        this.view = fragment;
        this.presenter = presenter;

        this.view.setPresenter(this.presenter);
        this.presenter.bindView(this.view);
    }

}
