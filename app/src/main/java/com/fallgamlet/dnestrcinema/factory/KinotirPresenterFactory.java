package com.fallgamlet.dnestrcinema.factory;

import com.fallgamlet.dnestrcinema.mvp.factory.MvpPresenterFactory;
import com.fallgamlet.dnestrcinema.mvp.presenters.MvpLoginPresenter;
import com.fallgamlet.dnestrcinema.mvp.presenters.MvpNewsPresenter;
import com.fallgamlet.dnestrcinema.mvp.presenters.MvpSoonPresenter;
import com.fallgamlet.dnestrcinema.mvp.presenters.MvpTicketsPresenter;
import com.fallgamlet.dnestrcinema.mvp.presenters.MvpTodayPresenter;
import com.fallgamlet.dnestrcinema.ui.login.LoginPresenterImpl;
import com.fallgamlet.dnestrcinema.ui.movie.soon.SoonPresenterImpl;
import com.fallgamlet.dnestrcinema.ui.movie.today.TodayPresenterImpl;
import com.fallgamlet.dnestrcinema.ui.news.NewsPresenterImpl;
import com.fallgamlet.dnestrcinema.ui.tickets.TicketsPresenterImpl;

/**
 * Created by fallgamlet on 16.07.17.
 */

public class KinotirPresenterFactory implements MvpPresenterFactory {

    @Override
    public MvpTodayPresenter createTodayPresenter() {
        return new TodayPresenterImpl();
    }

    @Override
    public MvpSoonPresenter createSoonPresenter() {
        return new SoonPresenterImpl();
    }

    @Override
    public MvpTicketsPresenter createTicketPresenter() {
        return new TicketsPresenterImpl();
    }

    @Override
    public MvpLoginPresenter createLoginPresenter() {
        return new LoginPresenterImpl();
    }

    @Override
    public MvpNewsPresenter createNewsPresenter() {
        return new NewsPresenterImpl();
    }

}
