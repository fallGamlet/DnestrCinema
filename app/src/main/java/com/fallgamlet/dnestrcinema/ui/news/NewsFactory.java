package com.fallgamlet.dnestrcinema.ui.news;

import com.fallgamlet.dnestrcinema.mvp.factory.BaseFactory;

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

        this.view.setPresenter(this.presenter);
        this.presenter.bindView(this.view);
    }
}
