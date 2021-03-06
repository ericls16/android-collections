package com.ls.rxjava.ui.rxjava1;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ls.rxjava.R;
import com.ls.rxjava.databinding.ActivityRxjava1BasicBinding;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;

/**
 * RxJava基本用法
 * <p>
 * 知识点包括：
 * 1> 创建Observer观察者;
 * 2> 创建Subscriber观察者;
 * 3> 创建Subscriber被观察者;
 * 4> 快捷创建被观察者事件队列的just和from方式;
 * 5> 观察者订阅被观察者，两者联系起来;
 * 6> Subscriber不完整定义回调;
 * <p>
 * 备注：
 * 1> 创建了Observable和Observer之后，再用 subscribe() 方法将它们联结起来，整条链子就可以工作了.
 * 2> 除了subscribe(Observer)和subscribe(Subscriber)，subscribe()还支持观察者的不完整定义的回调，RxJava会自动根据定义创建出Subscriber.
 * 3> Observer和Subscriber都是观察者，而且Observer在subscribe()过程中最终会被转换成Subscriber对象，因此，Subscriber来代替Observer更加严谨.
 * <p>
 * <p>
 * 总结：
 * 在RxJava的默认规则中，事件的发出和消费都是在同一个线程的。
 * 也就是说，如果只用上面的方法，实现出来的只是一个同步的观察者模式。
 * 观察者模式本身的目的就是『后台处理，前台回调』的异步机制，因此异步对于 RxJava 是至关重要的。
 * 而要实现异步，则需要用到 RxJava 的另一个概念： Scheduler。
 * Created by liusong on 2017/1/31.
 */

public class RxJava1BasicActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityRxjava1BasicBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_rxjava1_basic);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_observer:
                createObserver();
                showNothingMsg();
                break;
            case R.id.btn_subscriber:
                createSubscriber();
                showNothingMsg();
                break;
            case R.id.btn_observable:
                createObservable();
                showNothingMsg();
                break;
            case R.id.btn_quick_create_event_queue:
                createEventQueueQuickly();
                showNothingMsg();
                break;
            case R.id.btn_subscribe:
                subscribe();
                break;
            case R.id.btn_incomplete_definition:
                incompleteDefinitionCall();
                break;
            case R.id.btn_print_string_array:
                printStringArray();
                break;
            case R.id.btn_show_image:
                showImageBySubscribeObserver();
                break;
        }
    }

    /**
     * toast提示：无效果
     */
    private void showNothingMsg() {
        Toast.makeText(this, "无效果，主要是创建方法的使用！", Toast.LENGTH_SHORT).show();
    }

    /**
     * toast提示：查看log信息
     */
    private void showLogPrintMsg() {
        Toast.makeText(this, "查看log打印的信息！", Toast.LENGTH_SHORT).show();
    }

    /**
     * 创建Observer观察者
     */
    private void createObserver() {
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {

            }
        };
    }

    /**
     * 创建Subscriber观察者
     */
    private void createSubscriber() {
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {

            }
        };
    }

    /**
     * 创建Subscriber被观察者
     */
    private void createObservable() {
        Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("1");
                subscriber.onNext("2");
                subscriber.onNext("3");
                subscriber.onCompleted();
            }
        });
    }

    /**
     * 快捷创建事件队列
     */
    private void createEventQueueQuickly() {

        //just(T...): 将传入的参数依次发送出来。
        Observable observable_just = Observable.just("1", "2", "3");
        // 将会依次调用：
        // onNext("Hello");
        // onNext("Hi");
        // onNext("Aloha");
        // onCompleted();

        //from(T[]) / from(Iterable<? extends T>) : 将传入的数组或 Iterable 拆分成具体对象后，依次发送出来。
        String[] words = {"Hello", "Hi", "Aloha"};
        Observable observable_from = Observable.from(words);
        // 将会依次调用：
        // onNext("Hello");
        // onNext("Hi");
        // onNext("Aloha");
        // onCompleted();
    }

    /**
     * 订阅：
     * 创建了 Observable 和 Observer 之后，再用 subscribe() 方法将它们联结起来，整条链子就可以工作了.
     */
    private void subscribe() {
        showLogPrintMsg();

        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.i("LOG_CAT", "Observer::onCompleted");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.i("LOG_CAT", "Observer::onNext(" + s + ")");
            }
        };

        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.i("LOG_CAT", "Subscriber::onCompleted");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.i("LOG_CAT", "Subscriber::onNext(" + s + ")");
            }
        };

        Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("1");
                subscriber.onNext("2");
                subscriber.onNext("3");
                subscriber.onCompleted();
            }
        });

        //订阅关联
        observable.subscribe(observer);
        observable.subscribe(subscriber);
    }

    /**
     * 不完整定义回调：
     * 除了subscribe(Observer)和subscribe(Subscriber)，subscribe()还支持不完整定义的回调，RxJava会自动根据定义创建出Subscriber.
     */
    private void incompleteDefinitionCall() {
        showLogPrintMsg();

        Action1<String> onNextAction = new Action1<String>() {
            // onNext()
            @Override
            public void call(String s) {
                Log.i("LOG_CAT", "onNextAction::call(" + s + ")");
            }
        };
        Action1<Throwable> onErrorAction = new Action1<Throwable>() {
            // onError()
            @Override
            public void call(Throwable throwable) {
                // Error handling
            }
        };
        Action0 onCompletedAction = new Action0() {
            // onCompleted()
            @Override
            public void call() {
                Log.i("LOG_CAT", "onCompletedAction::call");
            }
        };

        Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                Log.d("LOG_CAT", "onCompletedAction::call");
                subscriber.onNext("1");
                subscriber.onNext("2");
                subscriber.onNext("3");
                subscriber.onCompleted();
            }
        });

        // 自动创建 Subscriber ，并使用 onNextAction来定义onNext().
        observable.subscribe(onNextAction);
        // 自动创建 Subscriber ，并使用 onNextAction和onErrorAction 来定义onNext()和onError().
        observable.subscribe(onNextAction, onErrorAction);
        // 自动创建 Subscriber ，并使用 onNextAction、 onErrorAction和onCompletedAction来定义onNext()、onError()和onCompleted().
        observable.subscribe(onNextAction, onErrorAction, onCompletedAction);
    }

    /**
     * 不完整定义回调：事件队列打印字符数组
     */
    private void printStringArray() {
        showLogPrintMsg();

        String[] array = {"welcome", "to", "here", "!"};
        Observable.from(array)
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.i("LOG_CAT", s);
                    }
                });
    }

    /**
     * 订阅Observer:由id取得Drawable图片并显示在ImageView中
     */
    private void showImageBySubscribeObserver() {

        Observable.create(new Observable.OnSubscribe<Drawable>() {
            @Override
            public void call(Subscriber<? super Drawable> subscriber) {
                Drawable drawable = getTheme().getDrawable(R.mipmap.uselessl);
                subscriber.onNext(drawable);
                subscriber.onCompleted();
            }
        }).subscribe(new Observer<Drawable>() {
            @Override
            public void onCompleted() {
                Log.i("LOG_CAT", "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.i("LOG_CAT", "onError");
                Toast.makeText(RxJava1BasicActivity.this, "display image error!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(Drawable drawable) {
                Log.i("LOG_CAT", "onNext");
                mBinding.ivImage.setImageDrawable(drawable);
            }
        });

    }

}
























