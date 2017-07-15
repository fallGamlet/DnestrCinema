package com.fallgamlet.dnestrcinema.network;

import com.fallgamlet.dnestrcinema.mvp.models.MovieItem;
import com.fallgamlet.dnestrcinema.mvp.models.NewsItem;
import com.fallgamlet.dnestrcinema.mvp.models.TicketItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import okhttp3.Call;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by fallgamlet on 09.07.17.
 */

public class NetClient {

    private OkHttpClient httpClient;
    private CookieJarImpl cookieJar;
    private String login;
    private String password;
    private RequestFactory requestFactory;
    private MapperFactory mapperFactory;
    private volatile Date endSession;
    private volatile int sessionTimeoutSeconds;


    public NetClient (RequestFactory requestFactory, MapperFactory mapperFactory) {
        this.requestFactory = requestFactory;
        this.mapperFactory = mapperFactory;

        sessionTimeoutSeconds = 15*60;

        endSession = new Date(0);

        createHttpClient();

    }

    private void createHttpClient() {
        this.cookieJar = new CookieJarImpl();

        this.httpClient = new OkHttpClient.Builder()
                                        .cookieJar(cookieJar)
                                        .build();
    }

    public List<Cookie> getCookies() {
        return this.cookieJar.getCookies();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getSessionTimeout() {
        return sessionTimeoutSeconds;
    }

    public void setSessionTimeout(int seconds) {
        this.sessionTimeoutSeconds = seconds;
    }


    public Observable<List<MovieItem>> getTodayMovies() {
        Request request = requestFactory.todayMoviesRequest();

        return createObservable(request)
                .map(new Function<String, List<MovieItem>>() {
                    @Override
                    public List<MovieItem> apply(@NonNull String html) throws Exception {
                        return mapperFactory.todayMoviesMapper().map(html);
                    }
                });
    }

    public Observable<List<MovieItem>> getSoonMovies() {
        Request request = requestFactory.soonMoviesRequest();

        return createObservable(request)
                .map(new Function<String, List<MovieItem>>() {
                    @Override
                    public List<MovieItem> apply(@NonNull String html) throws Exception {
                        return mapperFactory.soonMoviesMapper().map(html);
                    }
                });
    }

    public Observable<MovieItem> getDetailMovies(String path) {
        Request request = requestFactory.detailMovieRequest(path);

        return createObservable(request)
                .map(new Function<String, MovieItem>() {
                    @Override
                    public MovieItem apply(@NonNull String html) throws Exception {
                        return mapperFactory.detailMoviesMapper().map(html);
                    }
                });
    }

    public Observable<List<NewsItem>> getNews() {
        Request request = requestFactory.newsRequest();

        return createObservable(request)
                .map(new Function<String, List<NewsItem>>() {
                    @Override
                    public List<NewsItem> apply(@NonNull String html) throws Exception {
                        return mapperFactory.newsMapper().map(html);
                    }
                });
    }

    public Observable<List<TicketItem>> getTickets() {
        Request request = requestFactory.ticketsRequest();

        return createObservable(request)
                .map(new Function<String, List<TicketItem>>() {
                    @Override
                    public List<TicketItem> apply(@NonNull String html) throws Exception {
                        return mapperFactory.ticketsMapper().map(html);
                    }
                });
    }

    public Observable<Boolean> login() {
        return this.login(this.login, this.password);
    }

    public Observable<Boolean> login(String login, String password) {
        Request request = requestFactory.loginRequest(login, password);

        return createObservable(request)
                .map(new Function<String, Boolean>() {
                    @Override
                    public Boolean apply(@NonNull String text) throws Exception {
                        boolean check = mapperFactory.loginMapper().map(text);
                        prolongSession();
                        return check;
                    }
                });
    }


    private Observable<String> createObservable(Request request) {
        return createObservableCall(request)
                .map(new Function<Call, String>() {
                    @Override
                    public String apply(@NonNull Call call) throws Exception {
                        return getResponseBody(call);
                    }
                });
    }

    private Observable<Call> createObservableCall(Request request) {
        Call call = this.httpClient.newCall(request);
        return Observable.just(call);
    }

    private String getResponseBody(Call call) throws IOException {
        Response response = call.execute();

        if (!response.isSuccessful()) {
            throw new IOException("Not success");
        }

        tryProlongSession();

        if (response.body() == null) {
            throw new IOException("response body not exists");
        }

        return response.body().string();
    }


    private boolean tryProlongSession() {
        boolean check = isLogin();

        if (check) {
            prolongSession();
        }

        return check;
    }

    private void prolongSession() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, this.sessionTimeoutSeconds);

        this.endSession = calendar.getTime();
    }

    public boolean isLogin() {
        return endSession.after(new Date());
    }


    public static class CookieJarImpl implements CookieJar {
        private List<Cookie> cookies = new ArrayList<>();

        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            this.cookies.clear();
            this.cookies.addAll(cookies);
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            return this.cookies;
        }

        public List<Cookie> getCookies() {
            return this.cookies;
        }
    }
}