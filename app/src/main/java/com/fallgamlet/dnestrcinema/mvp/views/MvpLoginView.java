package com.fallgamlet.dnestrcinema.mvp.views;

import com.fallgamlet.dnestrcinema.mvp.presenters.MvpLoginPresenter;

/**
 * Created by fallgamlet on 03.07.17.
 */

public interface MvpLoginView
        extends MvpView <MvpLoginPresenter>
{

    void setLogin(CharSequence value);

    void setPassword(CharSequence value);

    void setLoginEnabled(boolean enabled);

    void setPasswordEnabled(boolean enabled);

    void setLoginButtonEnabled(boolean enabled);

    void setLoginVisible(boolean v);

    void setPasswordVisible(boolean v);

    void setLoginButtonVisible(boolean v);

    void setLoading(boolean v);

    void setErrorVisible(boolean v);

}
