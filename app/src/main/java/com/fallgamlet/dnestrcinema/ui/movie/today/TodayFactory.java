package com.fallgamlet.dnestrcinema.ui.movie.today;

import com.fallgamlet.dnestrcinema.mvp.factory.BaseFactory;
import com.fallgamlet.dnestrcinema.mvp.presenters.MvpTodayPresenter;
import com.fallgamlet.dnestrcinema.mvp.views.MvpTodayView;

/**
 * Created by fallgamlet on 03.07.17.
 */

public class TodayFactory
        extends BaseFactory<TodayMoviesFragment, MvpTodayView, MvpTodayPresenter>
{

    public TodayFactory() {
        TodayMoviesFragment fragment = new TodayMoviesFragment();
        TodayPresenterImpl presenter = new TodayPresenterImpl();

        this.fragment = fragment;
        this.view = fragment;
        this.presenter = presenter;

        initRelations();
    }

    @Override
    public void initRelations() {
        this.view.setPresenter(this.presenter);
        this.presenter.bindView(this.view);
    }

}
