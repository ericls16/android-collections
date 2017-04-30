package com.ls.retrofit.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.ls.retrofit.R;
import com.ls.retrofit.api.ApiService;
import com.ls.retrofit.api.ApiServiceFactory;
import com.ls.retrofit.base.BaseActivity;
import com.ls.retrofit.databinding.ActivityTestBinding;
import com.ls.retrofit.vo.ApiCommonVo;
import com.ls.retrofit.vo.CommonVo;
import com.ls.retrofit.vo.WeatherBean;
import com.trello.rxlifecycle.android.ActivityEvent;

import okhttp3.MultipartBody;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

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

        MultipartBody.Builder mbBuilder=new MultipartBody.Builder();
        mbBuilder.setType(MultipartBody.FORM);



        ApiServiceFactory.getInstance().create(ApiService.class)
                .register("zzz")
                .compose(this.<CommonVo>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommonVo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(CommonVo vo) {

                    }
                });
    }

}
