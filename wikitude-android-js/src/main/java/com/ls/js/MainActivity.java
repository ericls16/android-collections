package com.ls.js;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.ls.js.databinding.ActivityMainBinding;
import com.ls.js.model.Article;
import com.ls.js.model.Categorie;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.components.RxActivity;

import java.util.Arrays;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class MainActivity extends RxActivity implements View.OnClickListener {

    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.cbAd.setPages(
                new CBViewHolderCreator<LocalImageHolderView>() {

                    @Override
                    public LocalImageHolderView createHolder() {
                        return new LocalImageHolderView();
                    }
                }, Arrays.asList(R.drawable.h1, R.drawable.h2))
                .setPageIndicator(new int[]{R.drawable.oval_unfocused, R.drawable.oval_focused})
                .startTurning(2000);
        
//        requestCategories();
    }

    private void requestCategories() {
        ServiceFactory.create(RetrofitService.class)
                .categories()
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Categorie>>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Categorie> categories) {

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinding.cbAd.stopTurning();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, ListActivity.class);
        switch (v.getId()) {
            case R.id.tv_1:
                intent.putExtra("id", 1);
                break;
            case R.id.tv_2:
                intent.putExtra("id", 2);
                break;
            case R.id.tv_3:
                intent.putExtra("id", 3);
                break;
            case R.id.tv_4:
                intent.putExtra("id", 4);
                break;
            case R.id.tv_5:
                intent.putExtra("id", 5);
                break;
            case R.id.tv_6:
                intent.putExtra("id", 6);
                break;
            case R.id.tv_ar:
                startActivity(new Intent(this, WikitudeActivity.class));
                return;
            default:
                return;
        }
        startActivity(intent);
    }

    class LocalImageHolderView implements Holder<Integer> {

        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, final int position, Integer data) {
            imageView.setImageResource(data);
        }
    }
}
