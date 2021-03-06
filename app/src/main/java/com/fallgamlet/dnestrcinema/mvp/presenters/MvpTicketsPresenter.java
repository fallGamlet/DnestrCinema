package com.fallgamlet.dnestrcinema.mvp.presenters;

import com.fallgamlet.dnestrcinema.mvp.routers.LoginRouter;
import com.fallgamlet.dnestrcinema.mvp.views.MvpTicketsView;

/**
 * Created by fallgamlet on 03.07.17.
 */

public interface MvpTicketsPresenter extends MvpPresenter<MvpTicketsView>
{

    void onRefresh();

    void onLogout();

    void setRouter(LoginRouter router);

}
