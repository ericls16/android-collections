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
import com.ls.rxjava.vo.Student;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
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
 * -----------------------------------------------------------------
 * Observable的subscribeOn() 和 observeOn() 两个方法来对线程进行控制:
 * 1> subscribeOn(): 指定 subscribe() 所发生的线程，即 Observable.OnSubscribe 被激活时所处的线程。或者叫做事件产生的线程。
 * 2> observeOn(): 指定 Subscriber 所运行在的线程。或者叫做事件消费的线程。
 * -----------------------------------------------------------------
 * Func1类：它和 Action1 非常相似，也是 RxJava 的一个接口，用于包装含有一个参数的方法。
 *    Func1 和 Action 的区别在于， Func1 包装的是有返回值的方法。
 *    另外，和 ActionX 一样， FuncX 也有多个，用于不同参数个数的方法。
 * -----------------------------------------------------------------
 * 变换:
 * 1> map: 对事件进行加工转换，map是一对一转化。
 * 2> flatmap: 通过一组新创建的 Observable 将初始的对象『铺平』之后通过统一路径分发了下去。一对多的转化。
 * -----------------------------------------------------------------
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
        Observable.create(new Observable.OnSubscribe<Drawable>() {
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
    private void transformWithMap(){
        Toast.makeText(this, "结果请查看log打印", Toast.LENGTH_SHORT).show();
        Observable.just(1,2,3)
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) { // 参数类型 integer
                        Log.i(tag, "转换--"+integer);
                        if(integer==1){
                            return "one"; // 返回类型 String
                        }else if(integer==2){
                            return "two";
                        }else{
                            return "three";
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() { // 参数类型 String
                    @Override
                    public void call(String s) {
                        Log.i(tag, "转换结果--"+s);
                    }
                });
    }

    /**
     * flatMap前奏
     *
     * 利用map打印students数组里Student的名字
     */
    private void printNamesBeforeUseFlatMap(){
        Student[] students={new Student("小明"),new Student("张三")};
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
                        Log.i(tag,"打印学生："+s);
                    }
                });
    }

    /**
     * 打印每个学生的所有课程，这里是用了循环的方式打印
     */
    private void printCourseOfStudentBeforeUseFlatMap(){
        List<String> xiaoming=new ArrayList<>();
        xiaoming.add("语文");
        xiaoming.add("数学");

        List<String> zhangsan=new ArrayList<>();
        zhangsan.add("语文");
        zhangsan.add("英语");


        Student[] students={new Student("小明",xiaoming),new Student("张三",zhangsan)};
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
                        Log.i(tag, student.getName()+"的课程有：");
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
    private void printCourseOfStudentUseFlatMap(){
        List<String> xiaoming=new ArrayList<>();
        xiaoming.add("语文");
        xiaoming.add("数学");

        List<String> zhangsan=new ArrayList<>();
        zhangsan.add("语文");
        zhangsan.add("英语");


        Student[] students={new Student("小明",xiaoming),new Student("张三",zhangsan)};
        Observable.from(students)
            .flatMap(new Func1<Student, Observable<String>>() {
                @Override
                public Observable<String> call(Student student) {
                    Log.i(tag,student.getName()+"课程:");
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
                    Log.i(tag,s);
                }
            });
    }
}
























