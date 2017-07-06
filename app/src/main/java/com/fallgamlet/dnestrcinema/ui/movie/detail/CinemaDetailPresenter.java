package com.fallgamlet.dnestrcinema.ui.movie.detail;

import com.fallgamlet.dnestrcinema.mvp.presenters.MvpPresenter;
import com.fallgamlet.dnestrcinema.mvp.views.MvpView;
import com.fallgamlet.dnestrcinema.mvp.models.MovieItem;

/**
 * Created by fallgamlet on 09.04.17.
 */

public interface CinemaDetailPresenter<T extends MvpView> extends MvpPresenter<T> {
    void onTrailerButtonPressed();
    void onBuyTicketButtonPressed();
    void onRoomsPressed();
    void setData(MovieItem item);
}
