package com.fallgamlet.dnestrcinema.ui.about;

import com.fallgamlet.dnestrcinema.mvp.factory.BaseFactory;
import com.fallgamlet.dnestrcinema.mvp.presenters.MvpAboutPresenter;
import com.fallgamlet.dnestrcinema.mvp.views.MvpAboutView;

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

        initRelations();
    }

    @Override
    public void initRelations() {
        this.view.setPresenter(this.presenter);
        this.presenter.bindView(this.view);
    }
}
