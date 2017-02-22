package com.ls.retrofit.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.ls.retrofit.R;
import com.ls.retrofit.vo.ApiCommonVo;
import com.ls.retrofit.api.ApiService;
import com.ls.retrofit.api.ApiServiceFactory;
import com.ls.retrofit.databinding.ActivityMainBinding;
import com.ls.retrofit.vo.WeatherBean;
import com.ls.retrofit.vo.WeatherVo;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    }

    @Override

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_weather:
                requestWeatherInfo();
                break;
            case R.id.btn_skip:
                startActivity(new Intent(this,TestActivity.class));
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
                        Toast.makeText(MainActivity.this, "onCompleted", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MainActivity.this, "onError", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(WeatherVo dataSet) {
                        mBinding.tvResult.setText("预报时间：" + dataSet.getResult().getData().getWeather().get(0).getDate());
                    }
                });
    }

    /**
     * ？？？
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
                        Toast.makeText(MainActivity.this, "onCompleted", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MainActivity.this, "onError", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(WeatherBean weatherBean) {
                        mBinding.tvResult.setText("预报时间：" + weatherBean.getData().getWeather().get(0).getDate());
                    }
                });
    }
}
