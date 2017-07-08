package com.fallgamlet.dnestrcinema.ui.login;

import com.fallgamlet.dnestrcinema.mvp.factory.BaseFactory;
import com.fallgamlet.dnestrcinema.mvp.presenters.MvpLoginPresenter;
import com.fallgamlet.dnestrcinema.mvp.views.MvpLoginView;

/**
 * Created by fallgamlet on 03.07.17.
 */

public class LoginFactory
        extends BaseFactory<LoginFragment, MvpLoginView, MvpLoginPresenter>
{

    public LoginFactory() {
        LoginFragment fragment = new LoginFragment();
        LoginPresenterImpl presenter = new LoginPresenterImpl();

        this.fragment = fragment;
        this.view = fragment;
        this.presenter = presenter;

        initRelations();
    }

    @Override
    public void initRelations() {
        this.view.setPresenter(this.presenter);
        this.presenter.bindView(this.view);
    }

}
