package com.fallgamlet.dnestrcinema.mvp.factory;

import com.fallgamlet.dnestrcinema.mvp.presenters.MvpAboutPresenter;
import com.fallgamlet.dnestrcinema.mvp.presenters.MvpLoginPresenter;
import com.fallgamlet.dnestrcinema.mvp.presenters.MvpMovieDetailPresenter;
import com.fallgamlet.dnestrcinema.mvp.presenters.MvpNavigationPresenter;
import com.fallgamlet.dnestrcinema.mvp.presenters.MvpNewsPresenter;
import com.fallgamlet.dnestrcinema.mvp.presenters.MvpSoonPresenter;
import com.fallgamlet.dnestrcinema.mvp.presenters.MvpTicketsPresenter;
import com.fallgamlet.dnestrcinema.mvp.presenters.MvpTodayPresenter;

/**
 * Created by fallgamlet on 08.07.17.
 */

public interface MvpPresenterFactory {

    MvpNavigationPresenter createNavigationPresenter();

    @Deprecated
    MvpTodayPresenter createTodayPresenter();
    @Deprecated
    MvpSoonPresenter createSoonPresenter();

    MvpTicketsPresenter createTicketPresenter();

    MvpLoginPresenter createLoginPresenter();

    MvpNewsPresenter createNewsPresenter();

    MvpAboutPresenter createAboutPresenter();

    MvpMovieDetailPresenter createMovieDetailPresenter();

}
