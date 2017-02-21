package com.ls.retrofit.app;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by liu song on 2017/2/21.
 */

public class App extends Application {

    private static App instance;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        //chrome 拦截url工具
        Stetho.initializeWithDefaults(this);
    }
}
