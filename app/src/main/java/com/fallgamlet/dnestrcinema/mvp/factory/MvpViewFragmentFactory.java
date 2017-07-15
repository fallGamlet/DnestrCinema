package com.fallgamlet.dnestrcinema.mvp.factory;

import com.fallgamlet.dnestrcinema.mvp.views.Fragments;
import com.fallgamlet.dnestrcinema.mvp.views.MvpAboutView;
import com.fallgamlet.dnestrcinema.mvp.views.MvpLoginView;
import com.fallgamlet.dnestrcinema.mvp.views.MvpMovieDetailView;
import com.fallgamlet.dnestrcinema.mvp.views.MvpNavigationView;
import com.fallgamlet.dnestrcinema.mvp.views.MvpNewsView;
import com.fallgamlet.dnestrcinema.mvp.views.MvpSoonView;
import com.fallgamlet.dnestrcinema.mvp.views.MvpTicketsView;
import com.fallgamlet.dnestrcinema.mvp.views.MvpTodayView;

/**
 * Created by fallgamlet on 10.07.17.
 */

public interface MvpViewFragmentFactory {

    Fragments.MvpNavigationViewFragment createNavigationView();

    Fragments.MvpTodayViewFragment createTodayView();

    Fragments.MvpSoonViewFragment createSoonView();

    Fragments.MvpTicketsViewFragment createTicketsView();

    Fragments.MvpLoginViewFragment createLoginView();

    Fragments.MvpNewsViewFragment createNewsView();

    Fragments.MvpAboutViewFragment createAboutView();

    Fragments.MvpMovieDetailViewFragment createMovieDetailView();

}
