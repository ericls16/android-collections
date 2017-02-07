package com.ls.collections.ui.features;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.ls.collections.R;
import com.ls.collections.databinding.ActivityNotificationBinding;

/**
 * Notification
 * Created by liu song on 2017/2/7.
 */

public class NotificationActivity extends AppCompatActivity {

    private ActivityNotificationBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding= DataBindingUtil.setContentView(this,R.layout.activity_notification);
    }
}
