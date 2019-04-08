package com.fallgamlet.dnestrcinema.app;

import androidx.multidex.MultiDexApplication;
import android.util.Log;

import com.jakewharton.threetenabp.AndroidThreeTen;

import io.reactivex.plugins.RxJavaPlugins;

public final class App extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
        initRx();
    }

    private void initRx() {
        RxJavaPlugins.setErrorHandler(throwable -> {
            Log.d("App", "RxJavaPlugins error handle", throwable);
        });
    }


}
