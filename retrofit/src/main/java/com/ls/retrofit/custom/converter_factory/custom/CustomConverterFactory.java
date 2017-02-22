package com.ls.retrofit.custom.converter_factory.custom;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by liu song on 2017/2/21.
 */

public class CustomConverterFactory extends Converter.Factory{

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        //根据type判断是否是自己能处理的类型，不能的话，return null ,交给后面的Converter.Factory.
        return new CustomResponseConverter<>(type);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return new CustomRequestBodyConverter();
    }
}
