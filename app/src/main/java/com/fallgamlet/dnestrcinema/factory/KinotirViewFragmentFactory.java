package com.fallgamlet.dnestrcinema.factory;

import androidx.fragment.app.Fragment;
import com.fallgamlet.dnestrcinema.mvp.factory.MvpViewFragmentFactory;
import com.fallgamlet.dnestrcinema.ui.about.AboutFragment;
import com.fallgamlet.dnestrcinema.ui.login.LoginFragment;
import com.fallgamlet.dnestrcinema.ui.movie.soon.SoonMoviesFragment;
import com.fallgamlet.dnestrcinema.ui.movie.today.TodayMoviesFragment;
import com.fallgamlet.dnestrcinema.ui.navigation.MvpNavigationFragment;
import com.fallgamlet.dnestrcinema.ui.news.NewsFragment;
import com.fallgamlet.dnestrcinema.ui.tickets.TicketsFragment;


public class KinotirViewFragmentFactory implements MvpViewFragmentFactory {
    @Override
    public Fragment createNavigationView() {
        return new MvpNavigationFragment();
    }

    @Override
    public Fragment createTodayView() {
        return new TodayMoviesFragment();
    }

    @Override
    public Fragment createSoonView() {
        return new SoonMoviesFragment();
    }

    @Override
    public Fragment createTicketsView() {
        return new TicketsFragment();
    }

    @Override
    public Fragment createLoginView() {
        return new LoginFragment();
    }

    @Override
    public Fragment createNewsView() {
        return new NewsFragment();
    }

    @Override
    public Fragment createAboutView() {
        return new AboutFragment();
    }

    @Override
    public Fragment createMovieDetailView() {
        return null;
    }
}
