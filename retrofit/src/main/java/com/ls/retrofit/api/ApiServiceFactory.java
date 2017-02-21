package com.ls.retrofit.api;

import android.support.annotation.NonNull;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.ls.retrofit.app.App;
import com.ls.retrofit.app.Constants;
import com.ls.retrofit.custom.cookie.CookieManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by liu song on 2017/2/21.
 */

public class ApiServiceFactory {

    private static ApiServiceFactory instance;

    public static ApiServiceFactory getInstance() {
        if (instance == null) {
            instance = new ApiServiceFactory();
        }
        return instance;
    }

    public <T> T create(@NonNull Class<T> service) {
        return getRetrofit().create(service);
    }

    private Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private OkHttpClient getOkHttpClient() {
        return new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .cookieJar(new CookieManager(App.getInstance())) //自动管理cookie
                .build();
    }
}
