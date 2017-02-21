package com.ls.retrofit.custom.converter_factory;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

/**
 * Created by liu song on 2017/2/21.
 */

public class CustomRequestBodyConverter<T> implements Converter<T, RequestBody> {

    private Gson gson = new Gson();

    @Override
    public RequestBody convert(T value) throws IOException {
        String string = gson.toJson(value);
        return RequestBody.create(MediaType.parse("application/json; charset=UTF-8"), string);
    }
}
