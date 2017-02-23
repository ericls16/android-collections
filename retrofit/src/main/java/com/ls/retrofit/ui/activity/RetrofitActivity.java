package com.ls.retrofit.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.ls.retrofit.R;
import com.ls.retrofit.api.ApiService;
import com.ls.retrofit.api.ApiServiceFactory;
import com.ls.retrofit.base.BaseActivity;
import com.ls.retrofit.databinding.ActivityRetrofitBinding;
import com.ls.retrofit.vo.ApiCommonVo;
import com.ls.retrofit.vo.WeatherBean;
import com.ls.retrofit.vo.WeatherVo;
import com.trello.rxlifecycle.android.ActivityEvent;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by liu song on 2017/2/23.
 */

public class RetrofitActivity extends BaseActivity implements View.OnClickListener {

    private ActivityRetrofitBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_retrofit);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_weather:
//                requestWeatherBean(); //???这个有问题，待调试。
                requestWeatherInfo();
                break;
            default:
                break;
        }
    }

    /**
     * 请求天气信息
     */
    private void requestWeatherInfo() {
        ApiServiceFactory.getInstance().create(ApiService.class)
                .queryWeather("上海", "c835721be56ed3b6e603c6873625d4d5")
//                .compose(this.<WeatherVo>bindToLifecycle()) //自动绑定生命周期
                .compose(this.<WeatherVo>bindUntilEvent(ActivityEvent.DESTROY)) // 手动设置在Activity onDestroy的时候取消订阅
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WeatherVo>() {

                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onCompleted() {
                        Toast.makeText(RetrofitActivity.this, "onCompleted", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(RetrofitActivity.this, "onError", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(WeatherVo dataSet) {
                        Toast.makeText(RetrofitActivity.this, "天气预报时间：" + dataSet.getResult().getData().getWeather().get(0).getDate(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * ？？？
     * 报错
     * 回调信息统一封装类
     */
    private void requestWeatherBean() {
        ApiServiceFactory.getInstance().create(ApiService.class)
                .queryWeatherBean("上海", "c835721be56ed3b6e603c6873625d4d5")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<ApiCommonVo<WeatherBean>, WeatherBean>() {
                    @Override
                    public WeatherBean call(ApiCommonVo<WeatherBean> dataSet) {
                        return dataSet.getData();
                    }
                })
                .subscribe(new Subscriber<WeatherBean>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onCompleted() {
                        Toast.makeText(RetrofitActivity.this, "onCompleted", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(RetrofitActivity.this, "onError", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(WeatherBean weatherBean) {
                        Toast.makeText(RetrofitActivity.this, "天气预报时间：" + weatherBean.getData().getWeather().get(0).getDate(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
