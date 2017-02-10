package com.ls.rxjava.ui;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ls.rxjava.R;
import com.ls.rxjava.databinding.ActivitySchedulerBinding;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 线程控制Scheduler(调度器，相当于线程控制器)
 *
 * Schedulers知识点：
 * 1> Schedulers.immediate(): 直接在当前线程运行，相当于不指定线程。这是默认的 Scheduler。
 * 2> Schedulers.newThread(): 总是启用新线程，并在新线程执行操作。
 * 3> Schedulers.io(): I/O 操作（读写文件、读写数据库、网络信息交互等）所使用的 Scheduler。
 *      > 行为模式和 newThread() 差不多，区别在于 io() 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率。
 *      > 不要把计算工作放在 io() 中，可以避免创建不必要的线程。
 * 4> Schedulers.computation(): 计算所使用的 Scheduler。
 *      > 这个计算指的是 CPU 密集型计算，即不会被 I/O 等操作限制性能的操作，例如图形的计算。
 *      > 这个 Scheduler 使用的固定的线程池，大小为 CPU 核数。
 *      > 不要把 I/O 操作放在 computation() 中，否则 I/O 操作的等待时间会浪费 CPU。
 * 5> Android 还有一个专用的 AndroidSchedulers.mainThread()，它指定的操作将在 Android 主线程运行。
 *
 * Observable的subscribeOn() 和 observeOn() 两个方法来对线程进行控制:
 * 1> subscribeOn(): 指定 subscribe() 所发生的线程，即 Observable.OnSubscribe 被激活时所处的线程。或者叫做事件产生的线程。
 * 2> observeOn(): 指定 Subscriber 所运行在的线程。或者叫做事件消费的线程。
 *
 * Created by liu song on 2017/2/9.
 */

public class SchedulerActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String tag=SchedulerActivity.class.getSimpleName();

    private ActivitySchedulerBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding= DataBindingUtil.setContentView(this, R.layout.activity_scheduler);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_observerOn_subscribeOn:
                useObserverOnAndSubscribeOn();
                break;
            case R.id.btn_observerOn_subscribeOn_show_image:
                useObserverOnAndSubscribeOnShowImage();
                break;
            default:
                break;
        }
    }

    /**
     * 由于 subscribeOn(Schedulers.io()) 的指定，被创建的事件的内容 1、2、3、4 将会在 IO 线程发出；
     * 而由于 observeOn(AndroidScheculers.mainThread()) 的指定，因此 subscriber 数字的打印将发生在主线程 。
     * 事实上，这种在 subscribe() 之前写上两句 subscribeOn(Scheduler.io()) 和 observeOn(AndroidSchedulers.mainThread()) 的使用方式非常常见，
     * 它适用于多数的 『后台线程取数据，主线程显示』的程序策略。
     */
    private void useObserverOnAndSubscribeOn(){
        Toast.makeText(this, "结果请查看log打印", Toast.LENGTH_SHORT).show();
        Observable.just(1, 2, 3, 4)
            .subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
            .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
            .subscribe(new Action1<Integer>() {
                @Override
                public void call(Integer number) {
                    Log.i(tag, "number:" + number);
                }
            });
    }

    /**
     * 订阅Observer:由id取得Drawable图片并显示在ImageView中
     *
     * 那么，加载图片将会发生在 IO 线程，而设置图片则被设定在了主线程。这就意味着，即使加载图片耗费了几十甚至几百毫秒的时间，也不会造成丝毫界面的卡顿。
     */
    private void useObserverOnAndSubscribeOnShowImage() {
        Toast.makeText(this, "结果请查看log打印", Toast.LENGTH_SHORT).show();
        Observable
            .create(new Observable.OnSubscribe<Drawable>() {
                @Override
                public void call(Subscriber<? super Drawable> subscriber) {
                    Drawable drawable = getTheme().getDrawable(R.mipmap.uselessl);
                    subscriber.onNext(drawable);
                    subscriber.onCompleted();
                }
            })
            .subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
            .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
            .subscribe(new Observer<Drawable>() {
                @Override
                public void onCompleted() {
                    Log.i(tag, "onCompleted");
                }

                @Override
                public void onError(Throwable e) {
                    Log.i(tag, "onError");
                    Toast.makeText(SchedulerActivity.this, "display image error!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNext(Drawable drawable) {
                    Log.i(tag, "onNext");
                    mBinding.ivImage.setImageDrawable(drawable);
                }
            });

    }

}
