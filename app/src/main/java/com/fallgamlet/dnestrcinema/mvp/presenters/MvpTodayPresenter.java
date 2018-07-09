package com.fallgamlet.dnestrcinema.mvp.presenters;

import com.fallgamlet.dnestrcinema.mvp.models.MovieItem;
import com.fallgamlet.dnestrcinema.mvp.views.MvpTodayView;

/**
 * Created by fallgamlet on 03.07.17.
 */

public interface MvpTodayPresenter extends MvpPresenter<MvpTodayView> {

    void onRefresh();

    void onMovieSelected(MovieItem movieItem);

    void onTicketBuySelected(MovieItem movieItem);

}
