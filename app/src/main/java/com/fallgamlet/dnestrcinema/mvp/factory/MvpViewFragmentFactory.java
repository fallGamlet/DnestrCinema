package com.fallgamlet.dnestrcinema.mvp.factory;

import com.fallgamlet.dnestrcinema.mvp.views.Fragments;

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
