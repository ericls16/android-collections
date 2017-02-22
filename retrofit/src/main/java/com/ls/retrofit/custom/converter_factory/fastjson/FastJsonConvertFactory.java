package com.ls.retrofit.custom.converter_factory.fastjson;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by liu song on 2017/2/22.
 */

public class FastJsonConvertFactory extends Converter.Factory {
    public static FastJsonConvertFactory create() {
        return new FastJsonConvertFactory();
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return new FastJsonRequestConverter<>();
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new FastJsonResponseConverter<>(type);
    }
}
