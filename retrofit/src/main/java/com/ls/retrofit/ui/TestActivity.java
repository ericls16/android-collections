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
import com.ls.retrofit.vo.CommonVo;
import com.ls.retrofit.vo.WeatherVo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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
                .getUsers("supplier")
                .enqueue(new Callback<CommonVo>() {
                    @Override
                    public void onResponse(Call<CommonVo> call, Response<CommonVo> response) {
                        Toast.makeText(TestActivity.this, "onResponse", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<CommonVo> call, Throwable t) {
                        Toast.makeText(TestActivity.this, "onFailure", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
