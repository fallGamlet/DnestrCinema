package com.fallgamlet.dnestrcinema.data.network.kinotir;

import com.fallgamlet.dnestrcinema.data.network.RequestFactory;

import okhttp3.HttpUrl;
import okhttp3.Request;

/**
 * Created by fallgamlet on 09.07.17.
 */

public class KinotirRequestFactory implements RequestFactory {

    private String scheme = "http";
    private String domain = "kinotir.md";

    @Override
    public String getBaseUrl() {
        return scheme +"://"+ domain;
    }

    @Override
    public Request loginRequest(String email, String password) {
        HttpUrl url = getUrlBuilder()
                        .encodedPath("/ajax/user.php")
                        .addQueryParameter("email", email)
                        .addQueryParameter("password", password)
                        .addQueryParameter("action", "login")
                        .addQueryParameter("submit", "true")
                        .build();

        return new Request.Builder()
                        .url(url)
                        .get()
                        .build();
    }

    @Override
    public Request todayMoviesRequest() {
        HttpUrl url = getUrlBuilder()
                .encodedPath("/")
                .build();

        return new Request.Builder()
                .url(url)
                .get()
                .build();
    }

    @Override
    public Request soonMoviesRequest() {
        HttpUrl url = getUrlBuilder()
                .encodedPath("/skoro-v-kino")
                .build();

        return new Request.Builder()
                .url(url)
                .get()
                .build();
    }

    @Override
    public Request detailMovieRequest(String path) {
        HttpUrl url = getUrlBuilder()
                .encodedPath(path)
                .build();

        return new Request.Builder()
                .url(url)
                .get()
                .build();
    }

    @Override
    public Request ticketsRequest() {
        HttpUrl url = getUrlBuilder()
                .encodedPath("/lc/")
                .build();

        return new Request.Builder()
                .url(url)
                .get()
                .build();
    }

    @Override
    public Request newsRequest() {
        HttpUrl url = getUrlBuilder()
                .encodedPath("/novosti")
                .build();

        return new Request.Builder()
                .url(url)
                .get()
                .build();
    }

    private HttpUrl.Builder getUrlBuilder() {
        return new HttpUrl.Builder()
                .scheme(scheme)
                .host(domain);
    }
}
