package com.ls.rxjava.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ls.rxjava.R;
import com.ls.rxjava.databinding.ActivityMainBinding;

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
            case R.id.btn_basic_implement: //1.基本实现
                startActivity(new Intent(this, BasicImplementlActivity.class));
                break;
            case R.id.btn_scheduler: //1.基本实现
                startActivity(new Intent(this, SchedulerActivity.class));
                break;
        }
    }
}
