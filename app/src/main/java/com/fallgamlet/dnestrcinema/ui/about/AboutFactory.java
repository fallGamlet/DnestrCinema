package com.fallgamlet.dnestrcinema.ui.about;

import com.fallgamlet.dnestrcinema.mvp.factory.BaseFactory;

/**
 * Created by fallgamlet on 03.07.17.
 */

public class AboutFactory
        extends BaseFactory<AboutFragment, MvpAboutView, MvpAboutPresenter>
{

    public AboutFactory() {
        AboutFragment fragment = new AboutFragment();
        AboutPresenterImpl presenter = new AboutPresenterImpl();

        this.fragment = fragment;
        this.view = fragment;
        this.presenter = presenter;

        this.view.setPresenter(this.presenter);
        this.presenter.bindView(this.view);
    }
}
