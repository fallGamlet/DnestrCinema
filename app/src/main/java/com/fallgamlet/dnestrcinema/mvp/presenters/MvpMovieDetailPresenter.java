package com.fallgamlet.dnestrcinema.mvp.presenters;

import com.fallgamlet.dnestrcinema.mvp.views.MvpMovieDetailView;
import com.fallgamlet.dnestrcinema.mvp.models.MovieItem;

/**
 * Created by fallgamlet on 09.04.17.
 */

public interface MvpMovieDetailPresenter extends MvpPresenter<MvpMovieDetailView> {
    void onTrailerButtonPressed();
    void onBuyTicketButtonPressed();
    void onRoomsPressed();
    void setData(MovieItem item);
}
