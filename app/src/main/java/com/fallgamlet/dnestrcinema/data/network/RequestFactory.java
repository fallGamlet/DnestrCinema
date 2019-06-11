package com.fallgamlet.dnestrcinema.data.network;

import okhttp3.Request;

/**
 * Created by fallgamlet on 08.07.17.
 */

public interface RequestFactory {

    String getBaseUrl();

    Request loginRequest(String login, String password);

    Request todayMoviesRequest();

    Request soonMoviesRequest();

    Request detailMovieRequest(String path);

    Request ticketsRequest();

    Request newsRequest();

}
