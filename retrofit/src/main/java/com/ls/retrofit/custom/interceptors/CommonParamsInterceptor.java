package com.ls.retrofit.custom.interceptors;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 网络请求拦截器：网络请求发送前，插入公共参数
 * （自行处理，以下仅仅是示范）
 * Created by liu song on 2017/2/22.
 */

public class CommonParamsInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        if (request.method().equals("GET")) {
            HttpUrl httpUrl = request.url().newBuilder()
                    .addQueryParameter("version", "xxx")
                    .addQueryParameter("device", "Android")
                    .addQueryParameter("timestamp", String.valueOf(System.currentTimeMillis()))
                    .build();
            request = request.newBuilder().url(httpUrl).build();
        } else if (request.method().equals("POST")) {
            if (request.body() instanceof FormBody) {
                FormBody.Builder bodyBuilder = new FormBody.Builder();
                FormBody formBody = (FormBody) request.body();
                for (int i = 0; i < formBody.size(); i++) {
                    bodyBuilder.addEncoded(formBody.encodedName(i), formBody.encodedValue(i));
                }
                formBody = bodyBuilder
                        .addEncoded("version", "xxx")
                        .addEncoded("device", "Android")
                        .addEncoded("timestamp", String.valueOf(System.currentTimeMillis()))
                        .build();
                request = request.newBuilder().post(formBody).build();
            }
        }
        return chain.proceed(request);
    }
}
