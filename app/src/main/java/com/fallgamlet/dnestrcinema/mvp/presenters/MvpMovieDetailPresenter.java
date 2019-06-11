package com.fallgamlet.dnestrcinema.mvp.presenters;

import com.fallgamlet.dnestrcinema.domain.models.MovieItem;
import com.fallgamlet.dnestrcinema.mvp.views.MvpMovieDetailView;

/**
 * Created by fallgamlet on 09.04.17.
 */

public interface MvpMovieDetailPresenter extends MvpPresenter<MvpMovieDetailView> {

    void onTrailerButtonPressed();

    void onBuyTicketButtonPressed();

    void onRoomsPressed();

    void setData(MovieItem item);
}
