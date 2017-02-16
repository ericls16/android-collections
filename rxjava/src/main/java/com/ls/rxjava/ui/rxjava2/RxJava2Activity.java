package com.ls.rxjava.ui.rxjava2;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ls.rxjava.R;
import com.ls.rxjava.databinding.ActivityRxjava2Binding;

public class RxJava2Activity extends AppCompatActivity implements View.OnClickListener {

    private ActivityRxjava2Binding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_rxjava2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
}
