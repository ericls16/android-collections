package com.ls.rxjava.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ls.rxjava.R;
import com.ls.rxjava.databinding.ActivityMainBinding;
import com.ls.rxjava.ui.rxjava1.RxJava1Activity;
import com.ls.rxjava.ui.rxjava2.RxJava2Activity;

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
            case R.id.btn_rxjava1:
                startActivity(new Intent(this, RxJava1Activity.class));
                break;
            case R.id.btn_rxjava2:
                startActivity(new Intent(this, RxJava2Activity.class));
                break;
        }
    }
}
