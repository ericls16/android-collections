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

import com.ls.collections.R;
import com.ls.collections.databinding.ActivityNotificationBinding;

/**
 * Notification
 * Created by liu song on 2017/2/7.
 */

public class NotificationActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityNotificationBinding mBinding;

    private NotificationManager notificationManager;
    private NotificationCompat.Builder myBuilder;
    private int id = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_notification);

        //获取NotificationManager实例
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
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
            case R.id.btn_flag_auto_cancel:
                sendFlagAutoCancelNotification();
                break;
            case R.id.btn_flag_no_clear:
                sendFlagNoClearNotification();
                break;
            case R.id.btn_flag_on_going:
                sendFlagOngoingEventNotification();
                break;
        }
    }

    /**
     * 发通知
     */
    private void sendNotification() {
        //获取PendingIntent,用于点击通知跳转
        Intent intent = new Intent(this, NotificationActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //实例化NotificationCompat.Builde并设置相关属性
        myBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.android))
                .setAutoCancel(true) //点击通知后一定会自动清除
                .setContentTitle("标题")
                .setContentText("内容:点我跳转到NotificationActivity")
                .setContentIntent(pendingIntent);

        //通过builder.build()方法生成Notification对象,并发送通知,id=1
        notificationManager.notify(id, myBuilder.build());
    }

    /**
     * 更新上一条通知信息(更改置顶id通知的内容)
     */
    private void updateNotification() {
        myBuilder.setContentTitle("标题被更改");
        notificationManager.notify(id, myBuilder.build());
    }

    /**
     * 删除一条通知栏里的消息
     */
    public void cancelNotification() {
        //根据id来取消
        notificationManager.cancel(id);
//        notificationManager.cancelAll(); //清除所有通知
    }

    //--------------------------------------------------

    // Notification FLAG

    /**
     * notification设置FLAG_AUTO_CANCEL
     * 该 flag 表示该通知能被状态栏的清除按钮给清除掉
     */
    private void sendFlagAutoCancelNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Send Notification Use FLAG_AUTO_CLEAR")
                .setContentText("Hi,My id is 1,i can be clear.");
        Notification notification = builder.build();
        //设置 Notification 的 flags = FLAG_NO_CLEAR
        //FLAG_AUTO_CANCEL 表示该通知能被状态栏的清除按钮给清除掉
        //等价于 builder.setAutoCancel(true);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(id, notification);
    }

    /**
     * notification设置FLAG_NO_CLEAR
     * 该 flag 表示该通知不能被状态栏的清除按钮给清除掉,也不能被手动清除,但能通过 cancel() 方法清除
     * Notification.flags属性可以通过 |= 运算叠加效果
     */
    private void sendFlagNoClearNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Send Notification Use FLAG_NO_CLEAR")
                .setContentText("Hi,My id is 1,i can't be clear.");
        Notification notification = builder.build();
        //设置 Notification 的 flags = FLAG_NO_CLEAR
        //FLAG_NO_CLEAR 表示该通知不能被状态栏的清除按钮给清除掉,也不能被手动清除,但能通过 cancel() 方法清除
        //flags 可以通过 |= 运算叠加效果
        notification.flags |= Notification.FLAG_NO_CLEAR;
        notificationManager.notify(id, notification);
    }

    /**
     * 设置FLAG_ONGOING_EVENT
     * 该 flag 表示该通知通知放置在正在运行,不能被手动清除,但能通过 cancel() 方法清除
     */
    private void sendFlagOngoingEventNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Send Notification Use FLAG_ONGOING_EVENT")
                .setContentText("Hi,My id is 1,i can't be clear.");
        Notification notification = builder.build();
        //设置 Notification 的 flags = FLAG_NO_CLEAR
        //FLAG_ONGOING_EVENT 表示该通知通知放置在正在运行,不能被手动清除,但能通过 cancel() 方法清除
        //等价于 builder.setOngoing(true);
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        notificationManager.notify(id, notification);
    }

    //--------------------------------------------------
}
