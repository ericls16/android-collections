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
import com.ls.rxjava.databinding.ActivityRxjava1SchedulerBinding;
import com.ls.rxjava.vo.Student;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 线程控制Scheduler(调度器，相当于线程控制器)
 * <p>
 * Schedulers知识点：
 * 1> Schedulers.immediate(): 直接在当前线程运行，相当于不指定线程。这是默认的 Scheduler。
 * 2> Schedulers.newThread(): 总是启用新线程，并在新线程执行操作。
 * 3> Schedulers.io(): I/O 操作（读写文件、读写数据库、网络信息交互等）所使用的 Scheduler。
 * > 行为模式和 newThread() 差不多，区别在于 io() 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率。
 * > 不要把计算工作放在 io() 中，可以避免创建不必要的线程。
 * 4> Schedulers.computation(): 计算所使用的 Scheduler。
 * > 这个计算指的是 CPU 密集型计算，即不会被 I/O 等操作限制性能的操作，例如图形的计算。
 * > 这个 Scheduler 使用的固定的线程池，大小为 CPU 核数。
 * > 不要把 I/O 操作放在 computation() 中，否则 I/O 操作的等待时间会浪费 CPU。
 * 5> Android 还有一个专用的 AndroidSchedulers.mainThread()，它指定的操作将在 Android 主线程运行。
 * -----------------------------------------------------------------
 * Observable的subscribeOn() 和 observeOn() 两个方法来对线程进行控制:
 * 1> subscribeOn(): 指定 subscribe() 所发生的线程，即 Observable.OnSubscribe 被激活时所处的线程。或者叫做事件产生的线程。
 * 当使用了多个 subscribeOn() 的时候，只有第一个 subscribeOn() 起作用。
 * 2> observeOn(): 指定 Subscriber 所运行在的线程。或者叫做事件消费的线程。
 * observeOn() 指定的是它之后的操作所在的线程。因此如果有多次切换线程的需求，只要在每个想要切换线程的位置调用一次 observeOn() 即可。
 * <p>
 * 注：subscribeOn() 和 observeOn() 的内部实现，也是用的 lift().
 * subscribeOn只有第一次使用时起作用，observeOn可以多次使用，自由切换线程。
 * -----------------------------------------------------------------
 * Func1类：它和 Action1 非常相似，也是 RxJava 的一个接口，用于包装含有一个参数的方法。
 * Func1 和 Action 的区别在于， Func1 包装的是有返回值的方法。
 * 另外，和 ActionX 一样， FuncX 也有多个，用于不同参数个数的方法。
 * -----------------------------------------------------------------
 * 变换:
 * 1> map: 对事件进行加工转换，map是一对一转化。
 * 2> flatmap: 通过一组新创建的 Observable 将初始的对象『铺平』之后通过统一路径分发了下去。
 * 常用于一对多的转化、嵌套网络请求。
 * -----------------------------------------------------------------
 * throttleFirst(): 在每次事件触发后的一定时间间隔内丢弃新的事件。常用作去抖动过滤，
 * 例如按钮的点击监听器： RxView.clickEvents(button)
 * RxBinding 代码，后面的文章有解释 .throttleFirst(500, TimeUnit.MILLISECONDS) // 设置防抖间隔为 500ms .subscribe(subscriber);
 * -----------------------------------------------------------------
 * <p>
 * <p>
 * -----------------------------------------------------------------
 * RxJava变换的原理：lift()
 * 在 Observable 执行了 lift(Operator) 方法之后，会返回一个新的 Observable，
 * 这个新的 Observable 会像一个代理一样，负责接收原始的 Observable 发出的事件，并在处理后发送给 Subscriber。
 * <p>
 * lift() 是针对事件项和事件序列的.
 * <p>
 * xJava 都不建议开发者自定义 Operator 来直接使用 lift()，
 * 而是建议尽量使用已有的 lift() 包装方法（如 map() flatMap() 等）进行组合来实现需求，
 * 因为直接使用 lift() 非常容易发生一些难以发现的错误。
 * -----------------------------------------------------------------
 * compose: 对 Observable 整体的变换， 是针对 Observable 自身进行变换。
 * -----------------------------------------------------------------
 * doOnSubscribe()的后面跟一个 subscribeOn() ，就能指定准备工作的线程了
 * <p>
 * Observable.doOnSubscribe() 。
 * 它和 Subscriber.onStart() 同样是在 subscribe() 调用后而且在事件发送前执行，但区别在于它可以指定线程。
 * 默认情况下， doOnSubscribe() 执行在 subscribe() 发生的线程；
 * 而如果在 doOnSubscribe() 之后有 subscribeOn() 的话，它将执行在离它最近的 subscribeOn() 所指定的线程。
 * -----------------------------------------------------------------
 * <p>
 * Created by liu song on 2017/2/9.
 */

public class RxJava1SchedulerActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String tag = RxJava1SchedulerActivity.class.getSimpleName();

    private ActivityRxjava1SchedulerBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_rxjava1_scheduler);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_observerOn_subscribeOn:
                useObserverOnAndSubscribeOn();
                break;
            case R.id.btn_observerOn_subscribeOn_show_image:
                useObserverOnAndSubscribeOnShowImage();
                break;
            case R.id.btn_transform_map:
                transformWithMap();
                break;
            case R.id.btn_flatmap_prelude_print_student_name:
                printNamesBeforeUseFlatMap();
                break;
            case R.id.btn_flatmap_prelude_print_student_course:
                printCourseOfStudentBeforeUseFlatMap();
                break;
            case R.id.btn_flatmap_print_student_course:
                printCourseOfStudentUseFlatMap();
                break;
            case R.id.btn_do_on_subscribe:
                useDoOnSubscriber();
                break;
            default:
                break;
        }
    }

    //-----------------------------------------------

    /** Scheduler **/

    /**
     * 由于 subscribeOn(Schedulers.io()) 的指定，被创建的事件的内容 1、2、3、4 将会在 IO 线程发出；
     * 而由于 observeOn(AndroidScheculers.mainThread()) 的指定，因此 subscriber 数字的打印将发生在主线程 。
     * 事实上，这种在 subscribe() 之前写上两句 subscribeOn(Scheduler.io()) 和 observeOn(AndroidSchedulers.mainThread()) 的使用方式非常常见，
     * 它适用于多数的 『后台线程取数据，主线程显示』的程序策略。
     */
    private void useObserverOnAndSubscribeOn() {
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
     * <p>
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
                        Toast.makeText(RxJava1SchedulerActivity.this, "display image error!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Drawable drawable) {
                        Log.i(tag, "onNext");
                        mBinding.ivImage.setImageDrawable(drawable);
                    }
                });

    }

    //-----------------------------------------------
    /**
     * 变换
     * RxJava 提供了对事件序列进行变换的支持，这是它的核心功能之一，也是大多数人说『RxJava 真是太好用了』的最大原因。
     * 所谓变换，就是将事件序列中的对象或整个序列进行"加工处理"，转换成不同的事件或事件序列。
     */

    /**
     * map()变换案例
     * 1> map() 方法将参数中的 String 对象转换成一个 Bitmap 对象后返回，而在经过 map() 方法后，事件的参数类型也由 String 转为了 Bitmap。
     * 这种直接变换对象并返回的，是最常见的也最容易理解的变换。不过 RxJava 的变换远不止这样，它不仅可以针对事件对象，还可以针对整个事件队列，这使得 RxJava 变得非常灵活。
     */
    private void transformWithMap() {
        Toast.makeText(this, "结果请查看log打印", Toast.LENGTH_SHORT).show();
        Observable.just(1, 2, 3)
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) { // 参数类型 integer
                        Log.i(tag, "转换--" + integer);
                        if (integer == 1) {
                            return "one"; // 返回类型 String
                        } else if (integer == 2) {
                            return "two";
                        } else {
                            return "three";
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() { // 参数类型 String
                    @Override
                    public void call(String s) {
                        Log.i(tag, "转换结果--" + s);
                    }
                });
    }

    /**
     * flatMap前奏
     * <p>
     * 利用map打印students数组里Student的名字
     */
    private void printNamesBeforeUseFlatMap() {
        Toast.makeText(this, "结果请查看log打印", Toast.LENGTH_SHORT).show();
        Student[] students = {new Student("小明"), new Student("张三")};
        Observable.from(students)
                .map(new Func1<Student, String>() {
                    @Override
                    public String call(Student student) {
                        return student.getName();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        Log.i(tag, "打印学生：" + s);
                    }
                });
    }

    /**
     * 打印每个学生的所有课程，这里是用了循环的方式打印
     */
    private void printCourseOfStudentBeforeUseFlatMap() {
        Toast.makeText(this, "结果请查看log打印", Toast.LENGTH_SHORT).show();
        List<String> xiaoming = new ArrayList<>();
        xiaoming.add("语文");
        xiaoming.add("数学");

        List<String> zhangsan = new ArrayList<>();
        zhangsan.add("语文");
        zhangsan.add("英语");

        Student[] students = {new Student("小明", xiaoming), new Student("张三", zhangsan)};
        Observable.from(students)
                .subscribe(new Subscriber<Student>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Student student) {
                        Log.i(tag, student.getName() + "的课程有：");
                        List<String> courses = student.getCourses();
                        for (int i = 0; i < courses.size(); i++) {
                            String course = courses.get(i);
                            Log.i(tag, course);
                        }
                    }
                });
    }

    /**
     * 打印每个学生的所有课程
     */
    private void printCourseOfStudentUseFlatMap() {
        Toast.makeText(this, "结果请查看log打印", Toast.LENGTH_SHORT).show();
        List<String> xiaoming = new ArrayList<>();
        xiaoming.add("语文");
        xiaoming.add("数学");

        List<String> zhangsan = new ArrayList<>();
        zhangsan.add("语文");
        zhangsan.add("英语");


        Student[] students = {new Student("小明", xiaoming), new Student("张三", zhangsan)};
        Observable.from(students)
                .flatMap(new Func1<Student, Observable<String>>() {
                    @Override
                    public Observable<String> call(Student student) {
                        Log.i(tag, student.getName() + "课程:");
                        //返回的Observable对象带有的是单个学生的课程数组事件队列
                        return Observable.from(student.getCourses());
                    }
                })
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        Log.i(tag, s);
                    }
                });
    }


    /**
     * flatMap() 也常用于嵌套的异步操作，例如嵌套的网络请求(可用retrofit+rxjava)
     * 通过 flatMap() ，可以把嵌套的请求写在一条链中，从而保持程序逻辑的清晰。
     */
    private void nestedNetworkRequestUseFlatMap() {
        Toast.makeText(this, "请查看嵌套网络请求的示例代码", Toast.LENGTH_SHORT).show();
//        networkClient.requestToken() // 返回 Observable<String>，在订阅时请求 token，并在响应后发送 token
//            .flatMap(new Func1<String, Observable<Messages>>() {
//                @Override
//                public Observable<Messages> call(String token) {
//                    // 返回 Observable<Messages>，在订阅时请求消息列表，并在响应后发送请求到的消息列表
//                    return networkClient.requestCommitToken();
//                }
//            })
//            .subscribe(new Action1<Messages>() {
//                @Override
//                public void call(Messages messages) {
//                    // 处理显示消息列表
//                    showMessages(messages);
//                }
//            });
    }

    /**
     * doOnSubscribe在subscribe()调用后,而且在事件发送前执行;
     */
    private void useDoOnSubscriber() {
        Toast.makeText(this, "请查看方法的使用", Toast.LENGTH_SHORT).show();
        Observable
                .create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {

                    }
                })
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {

                    }
                })
                //如果在 doOnSubscribe()之后有subscribeOn()的话，它将执行在离它最近的subscribeOn()所指定的线程。
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {

                    }
                });
    }


}
























