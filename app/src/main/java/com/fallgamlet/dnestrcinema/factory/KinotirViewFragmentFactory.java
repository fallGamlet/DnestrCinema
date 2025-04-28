package com.fallgamlet.dnestrcinema.factory;

import androidx.fragment.app.Fragment;

import com.fallgamlet.dnestrcinema.mvp.factory.MvpViewFragmentFactory;
import com.fallgamlet.dnestrcinema.ui.about.AboutFragment;
import com.fallgamlet.dnestrcinema.ui.movie.soon.SoonMoviesFragment;
import com.fallgamlet.dnestrcinema.ui.movie.today.TodayMoviesFragment;
import com.fallgamlet.dnestrcinema.ui.news.NewsFragment;


public class KinotirViewFragmentFactory implements MvpViewFragmentFactory {

    @Override
    public Fragment createTodayView() {
        return new TodayMoviesFragment();
    }

    @Override
    public Fragment createSoonView() {
        return new SoonMoviesFragment();
    }

    @Override
    public Fragment createNewsView() {
        return new NewsFragment();
    }

    @Override
    public Fragment createAboutView() {
        return new AboutFragment();
    }

}
