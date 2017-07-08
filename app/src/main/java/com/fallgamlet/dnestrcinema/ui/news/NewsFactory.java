package com.fallgamlet.dnestrcinema.ui.news;

import com.fallgamlet.dnestrcinema.mvp.factory.BaseFactory;
import com.fallgamlet.dnestrcinema.mvp.presenters.MvpNewsPresenter;
import com.fallgamlet.dnestrcinema.mvp.views.MvpNewsView;

/**
 * Created by fallgamlet on 03.07.17.
 */

public class NewsFactory
        extends BaseFactory<NewsFragment, MvpNewsView, MvpNewsPresenter>
{

    public NewsFactory() {
        NewsFragment fragment = new NewsFragment();
        NewsPresenterImpl presenter = new NewsPresenterImpl();

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
