package com.fallgamlet.dnestrcinema.network;

import android.accounts.NetworkErrorException;
import android.support.annotation.NonNull;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by fallgamlet on 11.01.16.
 */
public class Network {

    private static OkHttpClient httpClient;

    public static synchronized OkHttpClient getHttpClient() {
        if (httpClient == null) {
            httpClient = new OkHttpClient();
        }

        return httpClient;
    }

    public static Observable<String> get(@NonNull String url) {
        return Observable.just(url)
                .map(new Function<String, String>() {
                    @Override
                    public String apply(@io.reactivex.annotations.NonNull String url) throws Exception {

                        Request request = new Request.Builder().url(url).build();
                        Response response = getHttpClient().newCall(request).execute();

                        if (!response.isSuccessful()) {
                            throw new NetworkErrorException(response.message());
                        }

                        return response.body().string();
                    }
                });
    }
}
