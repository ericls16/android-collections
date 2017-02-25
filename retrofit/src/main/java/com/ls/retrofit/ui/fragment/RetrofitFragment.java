package com.ls.retrofit.ui.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ls.retrofit.R;
import com.ls.retrofit.api.ApiService;
import com.ls.retrofit.api.ApiServiceFactory;
import com.ls.retrofit.base.BaseFragment;
import com.ls.retrofit.databinding.FragmentRetrofitBinding;
import com.ls.retrofit.vo.WeatherVo;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liu song on 2017/2/22.
 */

public class RetrofitFragment extends BaseFragment implements View.OnClickListener {

    private FragmentRetrofitBinding mBinding;

    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_retrofit, container, false);
        mBinding.setOnClickListener(this);
        return mBinding.getRoot();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_fragment:
                requestWeatherInfo();
                break;
        }
    }

    /**
     * 请求天气信息
     */
    private void requestWeatherInfo() {
        ApiServiceFactory.getInstance().create(ApiService.class)
                .queryWeather("上海", "c835721be56ed3b6e603c6873625d4d5")
                .compose(this.<WeatherVo>bindToLifecycle())
//                .compose(this.<WeatherVo>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WeatherVo>() {

                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onCompleted() {
//                        Toast.makeText(mContext, "onCompleted", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(mContext, "onError", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(WeatherVo dataSet) {
                        Toast.makeText(mContext, "onNext=预报时间：" + dataSet.getResult().getData().getWeather().get(0).getDate(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
