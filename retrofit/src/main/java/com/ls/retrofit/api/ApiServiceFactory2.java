package com.ls.retrofit.api;

import android.support.annotation.NonNull;
import android.util.ArrayMap;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.ls.retrofit.app.App;
import com.ls.retrofit.app.Constants;
import com.ls.retrofit.custom.cookie.CookieManager;

import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * ApiServiceFactory2
 *
 * 本类采用静态代码块来初始化retrofit
 * Created by liu song on 2017/2/22.
 */

public class ApiServiceFactory2 {

    private static final Retrofit RETROFIT;
    private static final Map<Class<?>, Object> SERVICE_MAP = new ArrayMap<>();
    //volatile关键字禁止JVM指令重排序优化
    private volatile static ApiServiceFactory2 INSTANCE;

    static {
        //静态代码块只有在类被第一次加载的时候执行一次，new实例也不会再次执行。
        OkHttpClient okhttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .cookieJar(new CookieManager(App.getInstance())) //自动管理cookie
                .build();
        RETROFIT = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(okhttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) //添加对Rxjava的适配
                .build();
    }

    public static ApiServiceFactory2 getInstance() {
        if (INSTANCE == null) {
            synchronized (ApiServiceFactory2.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ApiServiceFactory2();
                }
            }
        }
        return INSTANCE;
    }

    public static <T> T create(@NonNull Class<T> service) {
        Object o = SERVICE_MAP.get(service);
        if (o != null) {
            return (T) o;
        } else {
            T t = RETROFIT.create(service);
            SERVICE_MAP.put(service, t);
            return t;
        }
    }
}
