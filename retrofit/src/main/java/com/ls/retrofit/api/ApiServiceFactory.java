package com.ls.retrofit.api;

import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.ls.retrofit.app.App;
import com.ls.retrofit.app.Constants;
import com.ls.retrofit.custom.cookie_jar.CookieManager;

import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

/**
 * Created by liu song on 2017/2/21.
 */

public class ApiServiceFactory {


    private static final Map<Class<?>, Object> SERVICE_MAP = new ArrayMap<>();
    //volatile关键字禁止JVM指令重排序优化
    private volatile static ApiServiceFactory INSTANCE;
    private static Retrofit RETROFIT;

    private ApiServiceFactory() {
        initRetrofit();
    }

    public static ApiServiceFactory getInstance() {
        if (INSTANCE == null) {
            synchronized (ApiServiceFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ApiServiceFactory();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 初始化Retrofit(可以放在静态代码块里确保全局创建一次，详见ApiServiceFactory2)
     * -------------------
     * okhttpClient相关设置：
     * 超时设置：
     * connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS)
     * readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS)
     * writeTimeout(DEFAULT_WRITE_TIMEOUT, TimeUnit.SECONDS)
     * -------------------
     */
    private static void initRetrofit() {
        //PersistentCookieJar第三方库来管理cookie
//        ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(App.getInstance()));

        OkHttpClient okhttpClient = new OkHttpClient.Builder()
                //--------------------------------------
                //拦截请求，添加header请求头，重新发送，可以添加自定义拦截器。
                /*.addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        Request request = original.newBuilder()
                                .header("User-Agent", "Android, xxx")
                                .header("Accept", "application/json")
                                .header("Content-type", "application/json")
                                .method(original.method(), original.body())
                                .build();
                        return chain.proceed(request);
                    }
                })*/
                //--------------------------------------
                .addNetworkInterceptor(new StethoInterceptor())
                .cookieJar(new CookieManager(App.getInstance())) //自动管理cookie(手动实现CookieManager )
//                .cookieJar(cookieJar) //第三方库管理cookie
                .build();
        RETROFIT = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(okhttpClient)
                .addConverterFactory(GsonConverterFactory.create()) //Gson
//                .addConverterFactory(FastJsonConvertFactory.create()) //FastJson
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) //添加对Rxjava的适配
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io())) //让请求发生在io线程
                .build();
    }

    public <T> T create(@NonNull Class<T> service) {
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
