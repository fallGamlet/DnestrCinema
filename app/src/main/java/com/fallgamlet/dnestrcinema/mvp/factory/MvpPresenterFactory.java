package com.fallgamlet.dnestrcinema.mvp.factory;

import com.fallgamlet.dnestrcinema.mvp.presenters.MvpLoginPresenter;
import com.fallgamlet.dnestrcinema.mvp.presenters.MvpTicketsPresenter;

@Deprecated
public interface MvpPresenterFactory {

    MvpTicketsPresenter createTicketPresenter();

    MvpLoginPresenter createLoginPresenter();

}
