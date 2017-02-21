package com.ls.retrofit.custom.converter_factory;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by liu song on 2017/2/21.
 */

public class CustomResponseConverter <T> implements Converter<ResponseBody, T> {

    private Type type;
    Gson gson = new Gson();

    public CustomResponseConverter(Type type) {
        this.type = type;
    }

    @Override
    public T convert(ResponseBody responseBody) throws IOException {
        return gson.fromJson(responseBody.string(), type);
    }
}
