package com.ls.retrofit.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ls.retrofit.R;
import com.ls.retrofit.base.BaseActivity;
import com.ls.retrofit.databinding.ActivityRetrofitInFragmentBinding;
import com.ls.retrofit.ui.fragment.RetrofitFragment;

/**
 * Created by liu song on 2017/2/23.
 */

public class RetrofitInFragmentActivity extends BaseActivity {

    private ActivityRetrofitInFragmentBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_retrofit_in_fragment);
        initFragment();
    }

    private void initFragment() {
        getFragmentManager()
                .beginTransaction()
                .add(R.id.contain, new RetrofitFragment())
                .commitAllowingStateLoss();
    }
}
