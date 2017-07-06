package com.fallgamlet.dnestrcinema.ui.movie.soon;

import com.fallgamlet.dnestrcinema.mvp.factory.BaseFactory;

/**
 * Created by fallgamlet on 03.07.17.
 */

public class SoonFactory
        extends BaseFactory<CinemaFragmentSoon, MvpSoonView, MvpSoonPresenter>
{

    public SoonFactory() {
        CinemaFragmentSoon fragment = new CinemaFragmentSoon();
        SoonPresenterImpl presenter = new SoonPresenterImpl();

        this.fragment = fragment;
        this.view = fragment;
        this.presenter = presenter;

        this.view.setPresenter(this.presenter);
        this.presenter.bindView(this.view);
    }
}
