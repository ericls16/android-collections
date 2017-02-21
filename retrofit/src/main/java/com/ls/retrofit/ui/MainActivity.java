package com.ls.retrofit.ui;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.ls.retrofit.R;
import com.ls.retrofit.api.ApiService;
import com.ls.retrofit.api.ApiServiceFactory;
import com.ls.retrofit.databinding.ActivityMainBinding;
import com.ls.retrofit.vo.WeatherVo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding= DataBindingUtil.setContentView(this, R.layout.activity_main);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
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
                .queryWeather("上海","c835721be56ed3b6e603c6873625d4d5")
                .enqueue(new Callback<WeatherVo>() {
                    @Override
                    public void onResponse(Call<WeatherVo> call, Response<WeatherVo> response) {
                        Toast.makeText(MainActivity.this, "onSuccess", Toast.LENGTH_SHORT).show();
                        mBinding.tvResult.setText("result="+response.body().getResult().getData().getWeather().get(0).getDate());
                    }

                    @Override
                    public void onFailure(Call<WeatherVo> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "onFailure", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
