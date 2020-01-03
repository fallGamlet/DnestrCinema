package com.fallgamlet.dnestrcinema.factory;

import com.fallgamlet.dnestrcinema.mvp.factory.MvpPresenterFactory;
import com.fallgamlet.dnestrcinema.mvp.presenters.MvpLoginPresenter;
import com.fallgamlet.dnestrcinema.mvp.presenters.MvpTicketsPresenter;
import com.fallgamlet.dnestrcinema.ui.login.LoginPresenterImpl;
import com.fallgamlet.dnestrcinema.ui.tickets.TicketsPresenterImpl;

@Deprecated
public class KinotirPresenterFactory implements MvpPresenterFactory {

    @Override
    public MvpTicketsPresenter createTicketPresenter() {
        return new TicketsPresenterImpl();
    }

    @Override
    public MvpLoginPresenter createLoginPresenter() {
        return new LoginPresenterImpl();
    }

}
