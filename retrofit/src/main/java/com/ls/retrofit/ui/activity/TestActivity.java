package com.ls.retrofit.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.ls.retrofit.R;
import com.ls.retrofit.base.BaseActivity;
import com.ls.retrofit.databinding.ActivityTestBinding;

/**
 * Created by liu song on 2017/2/22.
 */

public class TestActivity extends BaseActivity implements View.OnClickListener {

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
        /*ApiServiceFactory.getInstance().create(ApiService.class)
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
                });*/
    }

}
