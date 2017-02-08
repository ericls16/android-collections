package com.ls.collections.ui.features;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.ls.collections.R;
import com.ls.collections.databinding.ActivityNotificationBinding;

/**
 * Notification
 * Created by liu song on 2017/2/7.
 */

public class NotificationActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityNotificationBinding mBinding;

    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;
    private Notification notification;
    private int id = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_notification);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_general_notification:
                sendNotification();
                break;
            case R.id.btn_update_notification:
                updateNotification();
                break;
            case R.id.btn_cancel_notification:
                cancelNotification();
                break;
        }

    }

    /**
     * 发通知
     */
    private void sendNotification() {
        //获取NotificationManager实例
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //获取PendingIntent,用于点击通知跳转
        Intent intent = new Intent(this, NotificationActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //实例化NotificationCompat.Builde并设置相关属性
        builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.android))
                .setAutoCancel(true) //点击通知后自动清除
                .setContentTitle("标题")
                .setContentText("内容:点我跳转到NotificationActivity")
                .setContentIntent(pendingIntent);

        //通过builder.build()方法生成Notification对象,并发送通知,id=1
        notificationManager.notify(id, builder.build());
    }

    /**
     * 更新上一条通知信息(更改置顶id通知的内容)
     */
    private void updateNotification() {
        builder.setContentTitle("标题被更改");
        notificationManager.notify(id,builder.build());
    }

    /**
     * 删除一条通知栏里的消息
     */
    public void cancelNotification() {
        //根据id来取消
        notificationManager.cancel(id);
    }
}
