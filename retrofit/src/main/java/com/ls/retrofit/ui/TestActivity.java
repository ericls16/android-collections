package com.ls.retrofit.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.ls.retrofit.R;
import com.ls.retrofit.api.ApiService;
import com.ls.retrofit.api.ApiServiceFactory;
import com.ls.retrofit.databinding.ActivityTestBinding;
import com.ls.retrofit.vo.WeatherVo;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liu song on 2017/2/22.
 */

public class TestActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityTestBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_test);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_weather:
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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WeatherVo>() {

                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onCompleted() {
                        Toast.makeText(TestActivity.this, "onCompleted", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(TestActivity.this, "onError", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(WeatherVo dataSet) {
                        mBinding.tvResult.setText("预报时间：" + dataSet.getResult().getData().getWeather().get(0).getDate());
                    }
                });
    }

}
