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

    void setLoginButtonEnabled(boolean enabled);

    void setLoginButtonVisible(boolean v);

    void setLoading(boolean v);

    void setErrorVisible(boolean v);

}
