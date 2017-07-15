package com.fallgamlet.dnestrcinema.factory;

import com.fallgamlet.dnestrcinema.mvp.factory.MvpViewFragmentFactory;
import com.fallgamlet.dnestrcinema.mvp.views.Fragments;
import com.fallgamlet.dnestrcinema.ui.about.AboutFragment;
import com.fallgamlet.dnestrcinema.ui.login.LoginFragment;
import com.fallgamlet.dnestrcinema.ui.movie.soon.SoonMoviesFragment;
import com.fallgamlet.dnestrcinema.ui.movie.today.TodayMoviesFragment;
import com.fallgamlet.dnestrcinema.ui.navigation.MvpNavigationFragment;
import com.fallgamlet.dnestrcinema.ui.news.NewsFragment;
import com.fallgamlet.dnestrcinema.ui.tickets.TicketsFragment;

/**
 * Created by fallgamlet on 16.07.17.
 */

public class KinotirViewFragmentFactory implements MvpViewFragmentFactory {
    @Override
    public Fragments.MvpNavigationViewFragment createNavigationView() {
        return new MvpNavigationFragment();
    }

    @Override
    public Fragments.MvpTodayViewFragment createTodayView() {
        return new TodayMoviesFragment();
    }

    @Override
    public Fragments.MvpSoonViewFragment createSoonView() {
        return new SoonMoviesFragment();
    }

    @Override
    public Fragments.MvpTicketsViewFragment createTicketsView() {
        return new TicketsFragment();
    }

    @Override
    public Fragments.MvpLoginViewFragment createLoginView() {
        return new LoginFragment();
    }

    @Override
    public Fragments.MvpNewsViewFragment createNewsView() {
        return new NewsFragment();
    }

    @Override
    public Fragments.MvpAboutViewFragment createAboutView() {
        return new AboutFragment();
    }

    @Override
    public Fragments.MvpMovieDetailViewFragment createMovieDetailView() {
        return null;
    }
}
