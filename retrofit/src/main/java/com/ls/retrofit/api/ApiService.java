package com.ls.retrofit.api;

import android.support.annotation.NonNull;

import com.ls.retrofit.vo.ApiCommonVo;
import com.ls.retrofit.vo.WeatherBean;
import com.ls.retrofit.vo.WeatherVo;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

;

/**
 * Created by liu song on 2017/2/21.
 */

public interface ApiService {

    @GET("http://op.juhe.cn/onebox/weather/query")
    Observable<WeatherVo> queryWeather(@NonNull @Query("cityname") String cityname,
                                       @NonNull @Query("key") String key);

    @GET("http://op.juhe.cn/onebox/weather/query")
    Observable<ApiCommonVo<WeatherBean>> queryWeatherBean(@NonNull @Query("cityname") String cityname,
                                                          @NonNull @Query("key") String key);


}
