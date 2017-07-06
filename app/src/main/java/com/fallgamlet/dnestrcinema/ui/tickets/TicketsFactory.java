package com.fallgamlet.dnestrcinema.ui.tickets;

import com.fallgamlet.dnestrcinema.mvp.factory.BaseFactory;

/**
 * Created by fallgamlet on 03.07.17.
 */

public class TicketsFactory
        extends BaseFactory<TicketsFragment, MvpTicketsView, MvpTicketsPresenter>
{

    public TicketsFactory() {
        TicketsFragment fragment = new TicketsFragment();
        TicketsPresenterImpl presenter = new TicketsPresenterImpl();

        this.fragment = fragment;
        this.view = fragment;
        this.presenter = presenter;

        this.view.setPresenter(this.presenter);
        this.presenter.bindView(this.view);
    }
}
