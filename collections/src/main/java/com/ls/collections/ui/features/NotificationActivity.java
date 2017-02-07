package com.ls.collections.ui.features;

import android.app.NotificationManager;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.View;

import com.ls.collections.R;
import com.ls.collections.databinding.ActivityNotificationBinding;

/**
 * Notification
 * Created by liu song on 2017/2/7.
 */

public class NotificationActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityNotificationBinding mBinding;

    private NotificationManager notificationManager;
    private int id = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_notification);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_general:
                sendNotification();
                break;
            case R.id.btn_cancel:
                cancelNotification();
                break;
        }

    }

    private void sendNotification() {
        //获取NotificationManager实例
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //实例化NotificationCompat.Builde并设置相关属性
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("标题");
        builder.setContentText("内容");

        //通过builder.build()方法生成Notification对象,并发送通知,id=1
        notificationManager.notify(id++, builder.build());
    }

    //取消消息的单击事件；
    public void cancelNotification() {
        notificationManager.cancel(--id); //按收到消息的先后从后向前点一次取消一条；
    }
}
