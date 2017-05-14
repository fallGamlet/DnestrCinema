package com.fallgamlet.dnestrcinema.mvp.presenters;

import com.fallgamlet.dnestrcinema.mvp.views.IView;
import com.fallgamlet.dnestrcinema.network.MovieItem;

/**
 * Created by fallgamlet on 09.04.17.
 */

public interface ICinemaDetailPresenter<T extends IView> extends IPresenter<T> {
    void onTrailerButtonPressed();
    void onBuyTicketButtonPressed();
    void onRoomsPressed();
    void setData(MovieItem item);
}
