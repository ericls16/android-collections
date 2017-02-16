package com.ls.rxjava.ui.rxjava2;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.ls.rxjava.R;
import com.ls.rxjava.databinding.ActivityRxjava2BasicBinding;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by liu song on 2017/2/16.
 */

public class RxJava2BasicActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = "TAG";
    private ActivityRxjava2BasicBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_rxjava2_basic);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_basic_use:
                basicUseStyle();
                break;
        }
    }

    private void basicUseStyle() {
        Observable.create(new ObservableOnSubscribe<Integer>() { //创建被观察者。
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                //ObservableEmitter(emitter:发射器，用于发出三种事件onNext(T value)、onComplete()、onError())。
                //利用emitter可以发出无限个onNext。
                Log.i(TAG, "send onNext(1)");
                emitter.onNext(1);
                Log.i(TAG, "send onNext(2)");
                emitter.onNext(2);
                Log.i(TAG, "send onNext(3)");
                emitter.onNext(3);
                Log.i(TAG, "send onNext(4)");
                emitter.onNext(4);
                //被观察者发出了第一个onComplete后，onComplete之后的事件将继续发送，观察者收到第一个onComplete事件后将不在继续接收事件。
                //被观察者可以不发送onComplete或onError，但是如果发送，则只能发送一个onComplete或者一个onError,两者是互斥的，不能同时发送。
                Log.i(TAG, "send onComplete()");
                emitter.onComplete();
                Log.i(TAG, "send onNext(5)");
                emitter.onNext(5);
                Log.i(TAG, "send onNext(6)");
                emitter.onNext(6);
            }
        }).subscribe(new Observer<Integer>() { //被观察者与观察者简历联系(订阅)。

            private Disposable mDisposable;
            private int i;

            @Override
            public void onSubscribe(Disposable d) { //在订阅的所有动作开始之前会先调用onSubscribe。
                //Disposable:一次性的，用完丢弃。
                //Disposable对象调用dispose()方法，观察者就不会继续接收事件，但是被观察者还会继续发送剩余的事件。
                Log.i(TAG, "subscribe");
                mDisposable = d;
            }

            @Override
            public void onNext(Integer integer) {
                Log.i(TAG, "receive onNext: " + integer);
                i++;
                if (i == 2) {
                    Log.i(TAG, "call dispose()");
                    mDisposable.dispose(); //中断观察者接收事件。
                    Log.i(TAG, "isDisposed : " + mDisposable.isDisposed());
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "receive onError");
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "receive onComplete");
            }
        });

    }
}
