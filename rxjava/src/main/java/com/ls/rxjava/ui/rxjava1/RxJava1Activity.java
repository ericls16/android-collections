package com.ls.rxjava.ui.rxjava1;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ls.rxjava.R;
import com.ls.rxjava.databinding.ActivityRxjava1Binding;

public class RxJava1Activity extends AppCompatActivity implements View.OnClickListener {

    private ActivityRxjava1Binding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_rxjava1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_basic_implement: //1.基本实现
                startActivity(new Intent(this, RxJava1BasicActivity.class));
                break;
            case R.id.btn_scheduler: //2.线程控制
                startActivity(new Intent(this, RxJava1SchedulerActivity.class));
                break;
        }
    }
}
