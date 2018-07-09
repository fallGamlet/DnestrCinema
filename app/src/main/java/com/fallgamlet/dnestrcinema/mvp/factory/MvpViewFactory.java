package com.fallgamlet.dnestrcinema.mvp.factory;

import com.fallgamlet.dnestrcinema.mvp.views.MvpAboutView;
import com.fallgamlet.dnestrcinema.mvp.views.MvpLoginView;
import com.fallgamlet.dnestrcinema.mvp.views.MvpMovieDetailView;
import com.fallgamlet.dnestrcinema.mvp.views.MvpNavigationView;
import com.fallgamlet.dnestrcinema.mvp.views.MvpNewsView;
import com.fallgamlet.dnestrcinema.mvp.views.MvpSoonView;
import com.fallgamlet.dnestrcinema.mvp.views.MvpTicketsView;
import com.fallgamlet.dnestrcinema.mvp.views.MvpTodayView;

/**
 * Created by fallgamlet on 08.07.17.
 */

public interface MvpViewFactory {

    MvpNavigationView createNavigationView();

    MvpTodayView createTodayView();

    MvpSoonView createSoonView();

    MvpTicketsView createTicketsView();

    MvpLoginView createLoginView();

    MvpNewsView createNewsView();

    MvpAboutView createAboutView();

    MvpMovieDetailView createMovieDetailView();

}
