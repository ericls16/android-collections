package com.ls.js;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.util.ArrayMap;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

/**
 * Created by Jiang Chen on 16/7/19.
 */
public class ServiceFactory {

    private static final Retrofit RETROFIT;

    private static final Map<Class<?>, Object> SERVICE_MAP = new ArrayMap<>();

    static {
        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
        RETROFIT = new Retrofit.Builder()
                .client(client)
                .baseUrl("http://116.228.234.237:3001/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private ServiceFactory() {
        throw new AssertionError("No instances.");
    }

    @SuppressWarnings("unchecked")
    @CheckResult
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
