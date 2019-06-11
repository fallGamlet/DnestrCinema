package com.fallgamlet.dnestrcinema.ui.login;

import android.text.TextUtils;

import com.fallgamlet.dnestrcinema.data.localstore.AccountLocalRepository;
import com.fallgamlet.dnestrcinema.domain.models.AccountItem;
import com.fallgamlet.dnestrcinema.app.AppFacade;
import com.fallgamlet.dnestrcinema.mvp.presenters.BasePresenter;
import com.fallgamlet.dnestrcinema.mvp.presenters.MvpLoginPresenter;
import com.fallgamlet.dnestrcinema.mvp.routers.LoginRouter;
import com.fallgamlet.dnestrcinema.mvp.views.MvpLoginView;
import com.fallgamlet.dnestrcinema.utils.StringUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

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
    private LoginRouter router;


    public LoginPresenterImpl() {
        accountItem = new AccountItem();
        fillData();
    }

    private void fillData() {
        AccountItem configAccount = AppFacade.getInstance().getAccountItem();
        if (configAccount != null) {
            accountItem.setLogin(configAccount.getLogin());
            accountItem.setPassword(configAccount.getPassword());
        }
    }


    @Override
    public void onLoginChanged(String value) {
        if (!StringUtils.INSTANCE.isEqual(accountItem.getLogin(), value)) {
            accountItem.setLogin(value);
            updateLoginEnabled();
        }
    }

    @Override
    public void onPasswordChanged(String value) {
        if (!StringUtils.INSTANCE.isEqual(accountItem.getPassword(), value)) {
            accountItem.setPassword(value);
            updateLoginEnabled();
        }
    }

    @Override
    public void onLogin() {
        if (isLoginEnable()) {
            showLoading(true);

            AppFacade.getInstance()
                    .getNetClient()
                    .login(accountItem.getLogin(), accountItem.getPassword())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                            new Consumer<Boolean>() {
                                @Override
                                public void accept(@NonNull Boolean check) throws Exception {
                                    if (check) {
                                        loginSuccess();
                                    }
                                    else {
                                        loginFail();
                                    }
                                }
                            },
                            new Consumer<Throwable>() {
                                @Override
                                public void accept(@NonNull Throwable throwable) throws Exception {
                                    loginFail();
                                }
                            }
                    );
        }
    }

    @Override
    public void setRouter(LoginRouter router) {
        this.router = router;
    }

    private boolean isRouterExist() {
        return router != null;
    }

    private void loginSuccess() {
        saveAccount();
        showError(false);
        showLoading(false);
        if (isRouterExist()) {
            router.hideLogin();
        }
    }

    private void saveAccount() {
        AccountItem configAccount = AppFacade.getInstance().getAccountItem();
        configAccount.setLogin(accountItem.getLogin());
        configAccount.setPassword(accountItem.getPassword());
        configAccount.setCinemaId(AppFacade.getInstance().getCinemaItem().getId());

        AccountLocalRepository repository;
        repository = new AccountLocalRepository(getView().getContext());

        repository.save(configAccount);
    }

    private void loginFail() {
        showError(true);
        showLoading(false);
    }

    private void showLoading(boolean val) {
        if (isViewBinded()) {
            getView().setLoading(val);
        }
    }

    private void showError(boolean val) {
        if (isViewBinded()) {
            getView().setErrorVisible(val);
        }
    }

    @Override
    public void bindView(MvpLoginView view) {
        super.bindView(view);
        updateViewData();
    }

    private void updateViewData() {
        if (isViewBinded()) {
            getView().setErrorVisible(false);
            getView().setLogin(accountItem.getLogin());
            getView().setPassword(accountItem.getPassword());
            updateLoginEnabled();
            getView().setLoading(false);
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
