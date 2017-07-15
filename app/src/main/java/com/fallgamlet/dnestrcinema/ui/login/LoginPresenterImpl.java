package com.fallgamlet.dnestrcinema.ui.login;

import android.text.TextUtils;

import com.fallgamlet.dnestrcinema.mvp.models.AccountItem;
import com.fallgamlet.dnestrcinema.mvp.presenters.BasePresenter;
import com.fallgamlet.dnestrcinema.mvp.presenters.MvpLoginPresenter;
import com.fallgamlet.dnestrcinema.mvp.views.MvpLoginView;

/**
 * Created by fallgamlet on 03.07.17.
 */

public class LoginPresenterImpl
        extends
            BasePresenter<MvpLoginView>
        implements
            MvpLoginPresenter
{

    private AccountItem accountItem;


    public LoginPresenterImpl() {
        accountItem = new AccountItem();
    }


    @Override
    public void onLoginChanged(String value) {
        accountItem.setLogin(value);
        updateLoginEnabled();
    }

    @Override
    public void onPasswordChanged(String value) {
        accountItem.setPassword(value);
        updateLoginEnabled();
    }

    @Override
    public void onLogin() {
        if (isLoginEnable()) {

        }
    }

    @Override
    public void bindView(MvpLoginView view) {
        super.bindView(view);
        updateViewData();
    }

    private void updateViewData() {
        if (isViewBinded()) {
            getView().setLogin(accountItem.getLogin());
            getView().setPassword(accountItem.getPassword());
            updateLoginEnabled();
        }
    }

    private void updateLoginEnabled() {
        if (isViewBinded()) {
            getView().setLoginButtonEnabled(isLoginEnable());
        }
    }

    private boolean isLoginEnable() {
        boolean check = !TextUtils.isEmpty(accountItem.getLogin());
        check &= !TextUtils.isEmpty(accountItem.getPassword());

        return check;
    }
}
