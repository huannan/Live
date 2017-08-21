package com.nan.live;

import android.app.Application;

/**
 * Created by huannan on 2017/4/8.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        System.loadLibrary("live-lib");
    }
}
