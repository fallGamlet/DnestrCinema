package com.fallgamlet.dnestrcinema.mvp.routers;

import com.fallgamlet.dnestrcinema.mvp.models.MovieItem;

/**
 * Created by fallgamlet on 02.07.17.
 */

public interface NavigationRouter {

    void showToday();

    void showSoon();

    void showTickets();

    void showAbout();

    void showNews();

    void showMovieDetail(MovieItem movieItem);

    void showBuyTicket(MovieItem movieItem);

}
