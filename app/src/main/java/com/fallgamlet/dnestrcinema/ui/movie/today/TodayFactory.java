package com.fallgamlet.dnestrcinema.ui.movie.today;

import com.fallgamlet.dnestrcinema.mvp.factory.BaseFactory;
import com.fallgamlet.dnestrcinema.mvp.presenters.MvpTodayPresenter;
import com.fallgamlet.dnestrcinema.mvp.views.MvpTodayView;

/**
 * Created by fallgamlet on 03.07.17.
 */

public class TodayFactory
        extends BaseFactory<CinemaFragment, MvpTodayView, MvpTodayPresenter>
{

    public TodayFactory() {
        CinemaFragment fragment = new CinemaFragment();
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
