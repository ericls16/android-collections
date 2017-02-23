package com.ls.retrofit.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.ls.retrofit.R;
import com.ls.retrofit.base.BaseActivity;
import com.ls.retrofit.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_retrofit:
                startActivity(new Intent(this, RetrofitActivity.class));
                break;
            case R.id.btn_retrofit_in_fragment:
                startActivity(new Intent(this, RetrofitInFragmentActivity.class));
                break;
            case R.id.btn_test:
                startActivity(new Intent(this, TestActivity.class));
                break;
            default:
                break;
        }
    }


}
