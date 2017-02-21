package com.ls.retrofit.api;

import com.android.annotations.NonNull;
import com.ls.retrofit.vo.WeatherVo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by liu song on 2017/2/21.
 */

public interface ApiService {

    @GET("http://op.juhe.cn/onebox/weather/query")
    Call<WeatherVo> queryWeather(@NonNull @Query("cityname") String cityname,
                                 @NonNull @Query("key") String key);
}
