package com.ls.collections.ui.features;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
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
            case R.id.btn_cancel_all_notification:
                cancelAllNotification();
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
            case R.id.btn_effect:
                showNotifyWithRing();
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
    }

    /**
     * 清除所有通知
     */
    private void cancelAllNotification() {
        notificationManager.cancelAll();
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
    //通知效果

    /**
     * 展示有自定义铃声效果的通知
     * 补充:使用系统自带的铃声效果:Uri.withAppendedPath(Audio.Media.INTERNAL_CONTENT_URI, "6");
     */
    private void showNotifyWithRing() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("我是伴有铃声效果的通知")
                .setContentText("美妙么?安静听~")
                //调用自己提供的铃声
                //.setSound(Uri.parse("file:///sdcard/xx/xx.mp3"))
                //.setSound(getSystemDefultRingtoneUri())
                //调用系统多媒体裤内的铃声
                //.setSound(Uri.withAppendedPath(MediaStore.Audio.Media.INTERNAL_CONTENT_URI,"1"))
                //调用系统默认响铃,设置此属性后setSound()会无效
                //.setDefaults(Notification.DEFAULT_SOUND)
                .setSound(getSystemDefultRingtoneUri());
        //另一种设置铃声的方法
        //Notification notify = builder.build();
        //调用系统默认铃声
        //notify.defaults = Notification.DEFAULT_SOUND;
        //调用自己提供的铃声
        //notify.sound = Uri.parse("android.resource://com.littlejie.notification/"+R.raw.sound);
        //调用系统自带的铃声
        //notify.sound = Uri.withAppendedPath(MediaStore.Audio.Media.INTERNAL_CONTENT_URI,"2");
        //mManager.notify(2,notify);
        notificationManager.notify(2, builder.build());
    }

    /**
     * 获取系统默认铃声的Uri
     */
    private Uri getSystemDefultRingtoneUri() {
        return RingtoneManager.getActualDefaultRingtoneUri(this,
                RingtoneManager.TYPE_RINGTONE);
    }

    /**
     * 展示有震动效果的通知,需要在AndroidManifest.xml中申请震动权限
     * <uses-permission android:name="android.permission.VIBRATE" />
     * 补充:测试震动的时候,手机的模式一定要调成铃声+震动模式,否则你是感受不到震动的
     */
    private void showNotifyWithVibrate() {
        //震动也有两种设置方法,与设置铃声一样,在此不再赘述
        long[] vibrate = new long[]{0, 500, 1000, 1500};
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("我是伴有震动效果的通知")
                .setContentText("颤抖吧,凡人~")
                //使用系统默认的震动参数,会与自定义的冲突
                //.setDefaults(Notification.DEFAULT_VIBRATE)
                //自定义震动效果
                .setVibrate(vibrate);
        //另一种设置震动的方法
        //Notification notify = builder.build();
        //调用系统默认震动
        //notify.defaults = Notification.DEFAULT_VIBRATE;
        //调用自己设置的震动
        //notify.vibrate = vibrate;
        //mManager.notify(3,notify);
        notificationManager.notify(3, builder.build());
    }

    /**
     * 显示带有呼吸灯效果的通知,但是不知道为什么,自己这里测试没成功
     */
    private void showNotifyWithLights() {
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("我是带有呼吸灯效果的通知")
                .setContentText("一闪一闪亮晶晶~")
                //ledARGB 表示灯光颜色、 ledOnMS 亮持续时间、ledOffMS 暗的时间
                .setLights(0xFF0000, 3000, 3000);
        Notification notify = builder.build();
        //只有在设置了标志符Flags为Notification.FLAG_SHOW_LIGHTS的时候，才支持呼吸灯提醒。
        notify.flags = Notification.FLAG_SHOW_LIGHTS;
        //设置lights参数的另一种方式
        //notify.ledARGB = 0xFF0000;
        //notify.ledOnMS = 500;
        //notify.ledOffMS = 5000;
        //使用handler延迟发送通知,因为连接usb时,呼吸灯一直会亮着
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                notificationManager.notify(4, builder.build());
            }
        }, 10000);
    }

    /**
     * 显示带有默认铃声、震动、呼吸灯效果的通知
     * 如需实现自定义效果,请参考前面三个例子
     */
    private void showNotifyWithMixed() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("我是有铃声+震动+呼吸灯效果的通知")
                .setContentText("我是最棒的~")
                //等价于setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);
                .setDefaults(Notification.DEFAULT_ALL);
        notificationManager.notify(5, builder.build());
    }

    /**
     * 通知无限循环,直到用户取消或者打开通知栏(其实触摸就可以了),效果与FLAG_ONLY_ALERT_ONCE相反
     * 注:这里没有给Notification设置PendingIntent,也就是说该通知无法响应,所以只能手动取消
     */
    private void showInsistentNotify() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("我是一个死循环,除非你取消或者响应")
                .setContentText("啦啦啦~")
                .setDefaults(Notification.DEFAULT_ALL);
        Notification notify = builder.build();
        notify.flags |= Notification.FLAG_INSISTENT;
        notificationManager.notify(6, notify);
    }

    /**
     * 通知只执行一次,与默认的效果一样
     */
    private void showAlertOnceNotify() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("仔细看,我就执行一遍")
                .setContentText("好了,已经一遍了~")
                .setDefaults(Notification.DEFAULT_ALL);
        Notification notify = builder.build();
        notify.flags |= Notification.FLAG_ONLY_ALERT_ONCE;
        notificationManager.notify(7, notify);
    }

}
