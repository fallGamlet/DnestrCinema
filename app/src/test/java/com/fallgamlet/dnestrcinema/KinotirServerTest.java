package com.fallgamlet.dnestrcinema;

import org.jsoup.HttpStatusException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class KinotirServerTest {
    @Test
    public void loginGetInfo() throws Exception {
        OkHttpClient httpClient = getHttpClient();

        Request request = getLoginRequest();

        Response response = httpClient.newCall(request).execute();

        if (!response.isSuccessful()) {
            throw new HttpStatusException(response.message(), response.code(), response.request().url().toString());
        }

        String responseBodyText = response.body().string();

        Assert.assertNotNull(responseBodyText);
    }

    @Test
    public void loginVerify() throws Exception {
        CookieJarImpl cookieJar = new CookieJarImpl();
        OkHttpClient httpClient = getHttpClient(cookieJar);

        Request request = getLoginRequest("fallgamlet@yandex.ru", "3nGggW3y");

        Response response = httpClient.newCall(request).execute();

        throwExceptionIfNotSuccessful(response);

        String responseBodyText = response.body().string();

        List<Cookie> cookies = cookieJar.getCookies();

        request = getPrivateCabinetRequest();
        response = httpClient.newCall(request).execute();

        throwExceptionIfNotSuccessful(response);

        responseBodyText = response.body().string();

        Assert.assertTrue(true);
    }

    private void throwExceptionIfNotSuccessful(Response response) throws HttpStatusException {
        if (!response.isSuccessful()) {
            throw new HttpStatusException(response.message(), response.code(), response.request().url().toString());
        }
    }


    private OkHttpClient getHttpClient() {
        return getHttpClient(null);
    }

    private OkHttpClient getHttpClient(CookieJar cookieJar) {
        if (cookieJar == null) {
            cookieJar = getEmptyCookieJar();
        }

        return new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .build();
    }

    private CookieJar getEmptyCookieJar() {
        return new CookieJar() {
            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {

            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                return new ArrayList<>();
            }
        };
    }

    private Request getLoginRequest() {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host("kinotir.md")
                .encodedPath("/ajax/user.php")
                .addQueryParameter("action", "login")
                .build();

        return new Request.Builder()
                .url(url)
                .get()
                .build();
    }

    private Request getLoginRequest(String email, String password) {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host("kinotir.md")
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

    private Request getRegistrationRequest(String name, String email, String password) {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host("kinotir.md")
                .encodedPath("/ajax/user.php")
                .addQueryParameter("name", name)
                .addQueryParameter("email", email)
                .addQueryParameter("password", password)
                .addQueryParameter("action", "registration")
                .addQueryParameter("submit", "true")
                .build();

        return new Request.Builder()
                .url(url)
                .get()
                .build();
    }

    private Request getPrivateCabinetRequest() {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host("kinotir.md")
                .encodedPath("/lc/")
                .build();

        return new Request.Builder()
                .url(url)
                .get()
                .build();
    }

    public class CookieJarImpl implements CookieJar {
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