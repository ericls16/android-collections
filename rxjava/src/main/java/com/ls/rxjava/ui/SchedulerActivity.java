package com.ls.rxjava.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.ls.rxjava.R;
import com.ls.rxjava.databinding.ActivitySchedulerBinding;

/**
 * Created by liu song on 2017/2/9.
 */

public class SchedulerActivity extends AppCompatActivity {
    private ActivitySchedulerBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding= DataBindingUtil.setContentView(this, R.layout.activity_scheduler);
    }
}
