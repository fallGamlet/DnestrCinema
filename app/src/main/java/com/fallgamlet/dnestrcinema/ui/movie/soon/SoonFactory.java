package com.fallgamlet.dnestrcinema.ui.movie.soon;

import com.fallgamlet.dnestrcinema.mvp.factory.BaseFactory;
import com.fallgamlet.dnestrcinema.mvp.presenters.MvpSoonPresenter;
import com.fallgamlet.dnestrcinema.mvp.views.MvpSoonView;

/**
 * Created by fallgamlet on 03.07.17.
 */

public class SoonFactory
        extends BaseFactory<SoonMoviesFragment, MvpSoonView, MvpSoonPresenter>
{

    public SoonFactory() {
        SoonMoviesFragment fragment = new SoonMoviesFragment();
        SoonPresenterImpl presenter = new SoonPresenterImpl();

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
